package org.codeit.roomunion.meeting.application.port.out.notification;

import java.util.List;
import org.codeit.roomunion.meeting.domain.model.Notification;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

// SSE 전송 및 연결 관리 Port
public interface NotificationSsePort {

    /**
     * <pre>
     * SSE 연결 생성 및 등록
     * - 사용자 기준으로 emitter 관리
     * </pre>
     */
    SseEmitter connect(Long userId);

    /**
     * <pre>
     * 현재 사용자에게 활성 SSE 연결이 있는지 확인
     * - 실시간 전송 여부 판단용
     * </pre>
     */
    boolean hasActiveConnections(Long userId);

    /**
     * <pre>
     * 해당 사용자에게 연결된 모든 emitter로 알림 전송
     * - 멀티 탭/기기 대응
     * </pre>
     */
    void sendToAll(Long userId, Notification notification);

    /**
     * <pre>
     * SSE 최초 연결 시, 기존 미읽음 알림 전송
     * - 연결 이전 알림 누락 방지
     * </pre>
     */
    void sendUnreadOnConnect(Long userId, SseEmitter emitter, List<Notification> unread);

}
