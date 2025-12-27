package org.codeit.roomunion.meeting.application.port.out.notification;

import java.time.LocalDateTime;
import java.util.List;
import org.codeit.roomunion.meeting.domain.model.Notification;
import org.codeit.roomunion.meeting.domain.model.NotificationType;

public interface NotificationRepository {

    Notification save(Long userId, NotificationType type, String message, String targetUrl, String imageUrl);

    List<Notification> findUnread(Long userId);

    //해당 유저의 읽지 않은 모든 알림을 한 번에 읽음 처리
    void markReadAll(Long userId, LocalDateTime readAt);

    //특정 알림 ID들만 읽음 처리
    void markReadByIds(Long userId, List<Long> ids, LocalDateTime readAt);

}
