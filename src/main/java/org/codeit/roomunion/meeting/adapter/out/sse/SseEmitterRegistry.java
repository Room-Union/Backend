package org.codeit.roomunion.meeting.adapter.out.sse;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
public class SseEmitterRegistry {

    // userId 기준으로 여러 개의 SSE Emitter를 관리 (멀티 탭/기기 대응)
    private final Map<Long, List<SseEmitter>> userEmitters = new ConcurrentHashMap<>();

    /**
     * 사용자에 대한 SSE Emitter 등록
     */
    public void add(Long userId, SseEmitter sseEmitter) {
        userEmitters.computeIfAbsent(userId, k -> new CopyOnWriteArrayList<>())
            .add(sseEmitter);
    }

    /**
     * 해당 사용자에게 연결된 모든 emitter 조회
     */
    public List<SseEmitter> getAll(Long userId) {
        return userEmitters.getOrDefault(userId, List.of());
    }

    /**
     * <pre>
     * emitter 제거
     * - 연결 종료 / 에러 / 타임아웃 시 호출
     * </pre>
     */
    public void remove(Long userId, SseEmitter sseEmitter) {
        List<SseEmitter> list = userEmitters.get(userId);
        if (list == null) return;

        // 현재 emitter 제거
        list.remove(sseEmitter);

        // 더 이상 연결된 SSE가 하나도 없으면
        if (list.isEmpty()) {
            // map에 저장된 값이
            // "내가 지금 보고 있는 이 list"일 때만 삭제
            // (중간에 다른 스레드가 emitter를 추가했으면 삭제되지 않음)
            userEmitters.remove(userId, list);
        }
    }

    /**
     * 해당 사용자에게 활성 SSE 연결이 있는지 확인
     */
    public boolean hasAny(Long userId) {
        List<SseEmitter> list = userEmitters.get(userId);
        return list != null && !list.isEmpty();
    }

}
