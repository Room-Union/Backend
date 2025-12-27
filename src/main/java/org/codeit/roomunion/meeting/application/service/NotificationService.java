package org.codeit.roomunion.meeting.application.service;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.codeit.roomunion.meeting.application.port.in.notification.NotificationUseCase;
import org.codeit.roomunion.meeting.application.port.out.notification.NotificationRepository;
import org.codeit.roomunion.meeting.application.port.out.notification.NotificationSsePort;
import org.codeit.roomunion.meeting.domain.command.notification.CreateAndSendNotificationCommand;
import org.codeit.roomunion.meeting.domain.command.notification.ReadNotificationCommand;
import org.codeit.roomunion.meeting.domain.command.notification.SubscribeNotificationCommand;
import org.codeit.roomunion.meeting.domain.model.Notification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class NotificationService implements NotificationUseCase {

    private final NotificationRepository notificationRepository;
    private final NotificationSsePort notificationSsePort;

    @Override
    public SseEmitter subscribe(SubscribeNotificationCommand command) {
        Long userId = command.getUserId();
        SseEmitter emitter = notificationSsePort.connect(userId);

        // 연결 직후: 미읽음 알림 밀어넣기(비동기)
        pushUnreadAsync(userId, emitter);

        return emitter;
    }

    @Async
    protected void pushUnreadAsync(Long userId, SseEmitter emitter) {
        List<Notification> unreadNotification = notificationRepository.findUnread(userId);
        if (!unreadNotification.isEmpty()) {
            notificationSsePort.sendUnreadOnConnect(userId, emitter, unreadNotification);
        }
    }


    @Override
    public void createAndSend(CreateAndSendNotificationCommand command) {
        Long userId = command.getUserId();

        // DB 저장
        Notification saved = notificationRepository.save(
            userId,
            command.getType(),
            command.getMessage(),
            command.getTargetUrl(),
            command.getImageUrl()
        );

        // SSE 연결이 있으면, "커밋 이후" 전송 (일관성)
        if (notificationSsePort.hasActiveConnections(userId)) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    notificationSsePort.sendToAll(userId, saved);
                }
            });
        }
    }

    @Override
    public void read(ReadNotificationCommand command) {
        LocalDateTime readAt = (command.getReadAt() == null) ? LocalDateTime.now() : command.getReadAt();

        if (command.getIds() == null || command.getIds().isEmpty()) {
            notificationRepository.markReadAll(command.getUserId(), readAt);
            return;
        }

        // ids 주지 않으면 전부 읽음 처리 (중복 x)
        List<Long> distinct = command.getIds().stream().distinct().toList();
        if (distinct.isEmpty()) {
            notificationRepository.markReadAll(command.getUserId(), readAt);
        } else {
            notificationRepository.markReadByIds(command.getUserId(), distinct, readAt);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notification> getUnread(Long userId) {
        return notificationRepository.findUnread(userId);
    }
}
