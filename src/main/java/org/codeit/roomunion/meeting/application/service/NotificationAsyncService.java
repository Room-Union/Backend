package org.codeit.roomunion.meeting.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.codeit.roomunion.meeting.application.port.out.notification.NotificationRepository;
import org.codeit.roomunion.meeting.application.port.out.notification.NotificationSsePort;
import org.codeit.roomunion.meeting.domain.model.Notification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class NotificationAsyncService {

    private final NotificationRepository notificationRepository;
    private final NotificationSsePort notificationSsePort;

    /**
     * SSE 연결 시점에 이미 저장돼 있던 미읽음 알림을 DB에서 조회해서 한 번에 보내주는 초기 동기화 로직이고
     * DB 부하 때문에 비동기로 분리
     * @param userId
     * @param emitter
     */
    @Async
    @Transactional(readOnly = true)
    public void pushUnreadOnConnect(Long userId, SseEmitter emitter) {
        List<Notification> unread = notificationRepository.findUnread(userId);
        if (!unread.isEmpty()) {
            notificationSsePort.sendUnreadOnConnect(userId, emitter, unread);
        }
    }
}