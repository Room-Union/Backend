package org.codeit.roomunion.meeting.adapter.out.sse;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codeit.roomunion.meeting.application.port.out.notification.NotificationSsePort;
import org.codeit.roomunion.meeting.domain.model.Notification;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationSseAdapter implements NotificationSsePort {

    // SSE Emitter 저장소
    private final SseEmitterRegistry registry;

    //emitter별 keep-alive 스케줄링 작업
    private final Map<SseEmitter, ScheduledFuture<?>> keepAliveTasks = new ConcurrentHashMap<>();

    // 공용 스케줄러 (keep-alive 전송용)
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(
        Math.max(2, Runtime.getRuntime().availableProcessors() / 2)
    );

    // SSE 연결 타임아웃 (0 = 무제한)
    private static final long SSE_TIMEOUT_MS = 0L;

    // heartbeat 주기 (초)
    private static final long HEARTBEAT_SECONDS = 15L;

    /**
     * SSE 연결을 생성하고 서버에 등록한다.
     * 이 메서드는 클라이언트가 알림을 실시간으로 받기 위해
     * 가장 먼저 호출해야 하는 메서드이다.
     */
    @Override
    public SseEmitter connect(Long userId) {

        // 클라이언트와 서버를 연결해주는 SSE 객체를 생성한다.
        // timeout을 0으로 두면 서버가 연결을 강제로 끊지 않는다.
        SseEmitter emitter = new SseEmitter(SSE_TIMEOUT_MS);

        // 생성한 emitter를 사용자 기준으로 저장한다.
        // 이후 알림을 보낼 때 userId로 emitter를 찾기 위해 필요하다.
        registry.add(userId, emitter);

        // 클라이언트가 연결을 정상적으로 종료했을 때 호출된다.
        emitter.onCompletion(() -> cleanup(userId, emitter, "completion"));

        // 연결이 타임아웃으로 종료될 경우 호출된다.
        emitter.onTimeout(() -> cleanup(userId, emitter, "timeout"));

        // 네트워크 오류 등으로 에러가 발생했을 때 호출된다.
        emitter.onError(e -> {
            log.debug("SSE emitter onError 콜백 실행 for user {}: {}", userId, e.getMessage());
            cleanup(userId, emitter, "error");
        });

        // 클라이언트에게 연결이 정상적으로 되었음을 알리는 이벤트를 전송한다.
        // 프론트엔드에서 SSE 연결 성공 여부를 확인하는 용도로 사용된다.
        try {
            emitter.send(
                SseEmitter.event()
                    .name("INIT")
                    .id(String.valueOf(Instant.now().toEpochMilli()))
                    .data("connected as " + userId)
            );
            log.debug("SSE INIT 이벤트 전송 성공 for user {}", userId);
        } catch (IOException e) {
            // 초기 이벤트 전송에 실패했다면 이미 연결이 끊어진 상태이므로
            // emitter를 종료하고 정리 작업을 수행한다.
            log.error("SSE INIT 이벤트 전송 실패 for user {}: {}", userId, e.getMessage());
            emitter.completeWithError(e);
            cleanup(userId, emitter, "init failed");
            return emitter;
        } catch (Exception e) {
            // 기타 예외 처리
            log.error("SSE 연결 중 예외 발생 for user {}: {}", userId, e.getMessage());
            emitter.completeWithError(e);
            cleanup(userId, emitter, "init exception");
            return emitter;
        }

        // INIT 이벤트 전송 성공 후에만 keep-alive 스케줄러를 등록한다.
        // 첫 PING은 HEARTBEAT_SECONDS 후에 전송 (즉시 전송하지 않음)
        ScheduledFuture<?> future = scheduler.scheduleAtFixedRate(() -> {
            try {
                emitter.send(
                    SseEmitter.event()
                        .name("PING")
                        .data("keep-alive")
                );
                log.trace("SSE PING 전송 성공 for user {}", userId);
            } catch (IOException io) {
                // IOException = 클라이언트가 이미 연결을 끊음 (Broken pipe)
                // complete()를 호출하면 오히려 에러가 발생하므로 그냥 정리만 수행
                log.debug("Keep-alive 전송 실패 (IOException) for user {} - 클라이언트 연결 종료", userId);
                cleanup(userId, emitter, "keep-alive io");
            } catch (Exception ex) {
                // 기타 예외
                log.debug("Keep-alive 전송 실패 (Exception) for user {}: {}", userId, ex.getMessage());
                cleanup(userId, emitter, "keep-alive error");
            }
        }, HEARTBEAT_SECONDS, HEARTBEAT_SECONDS, TimeUnit.SECONDS); // 첫 실행도 15초 후

        // emitter와 해당 emitter의 keep-alive 작업을 함께 관리한다.
        // emitter 종료 시 heartbeat 작업도 같이 중단하기 위해 필요하다.
        keepAliveTasks.put(emitter, future);

        log.debug("SSE 연결 완료 for user {}", userId);
        
        // 컨트롤러에서 이 emitter를 반환하면
        // 클라이언트와 서버 간 SSE 연결이 완성된다.
        return emitter;
    }


    /**
     * 해당 사용자에게 현재 활성화된 SSE 연결이 있는지 확인한다.
     * 실시간 알림을 보낼 수 있는 상태인지 판단할 때 사용된다.
     */
    @Override
    public boolean hasActiveConnections(Long userId) {
        return registry.hasAny(userId);
    }

    /**
     * 특정 사용자에게 연결된 모든 SSE emitter로 알림을 전송한다.
     * 여러 탭이나 여러 기기로 접속한 경우를 모두 처리한다.
     */
    @Override
    public void sendToAll(Long userId, Notification notification) {
        List<SseEmitter> emitters = registry.getAll(userId);

        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(
                    SseEmitter.event()
                        .name("NOTIFICATION")
                        .id(String.valueOf(notification.getId()))
                        .data(notification)
                );
            } catch (IOException io) {
                // IOException = 클라이언트가 이미 연결을 끊음
                log.debug("Notification 전송 실패 (IOException) for user {} - 클라이언트 연결 종료", userId);
                cleanup(userId, emitter, "send failed - io");
            } catch (Exception e) {
                // 기타 예외
                log.debug("Notification 전송 실패 (Exception) for user {}: {}", userId, e.getMessage());
                cleanup(userId, emitter, "send failed");
            }
        }
    }

    /**
     * SSE 최초 연결 시, 이전에 읽지 않은 알림들을 한 번에 전송한다.
     * 연결되기 전에 생성된 알림이 누락되지 않도록 하기 위한 메서드이다.
     */
    @Override
    public void sendUnreadOnConnect(Long userId, SseEmitter emitter, List<Notification> unread) {
        try {
            for (Notification n : unread) {
                emitter.send(
                    SseEmitter.event()
                        .name("NOTIFICATION")
                        .id(String.valueOf(n.getId()))
                        .data(n)
                );
            }
        } catch (IOException io) {
            // IOException = 클라이언트가 이미 연결을 끊음
            log.debug("Unread notifications 전송 실패 (IOException) for user {} - 클라이언트 연결 종료", userId);
            cleanup(userId, emitter, "send unread failed - io");
        } catch (Exception e) {
            // 기타 예외
            log.debug("Unread notifications 전송 실패 (Exception) for user {}: {}", userId, e.getMessage());
            cleanup(userId, emitter, "send unread failed");
        }
    }

    /**
     * SSE emitter와 관련된 리소스를 정리한다.
     * 연결 종료, 에러, 타임아웃 등 모든 종료 상황에서 호출된다.
     */
    private void cleanup(Long userId, SseEmitter emitter, String reason) {

        // emitter에 연결된 keep-alive 작업이 있다면 중단한다.
        try {
            Optional.ofNullable(keepAliveTasks.remove(emitter))
                .ifPresent(f -> f.cancel(true));
        } catch (Exception ignored) {}

        // registry에서 해당 emitter를 제거한다.
        try {
            registry.remove(userId, emitter);
        } catch (Exception ignored) {}

        log.debug("SSE cleanup done: user={}, reason={}", userId, reason);
    }
}
