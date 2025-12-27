package org.codeit.roomunion.meeting.adapter.in.web.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import org.codeit.roomunion.meeting.domain.model.Notification;
import org.codeit.roomunion.meeting.domain.model.NotificationType;

@Getter
@Builder
@Schema(title = "NotificationResponse : 테스트용 알림 응답 DTO")
public class NotificationResponse {
    private Long id;
    private Long userId;
    private NotificationType type;
    private String message;
    private String targetUrl;
    private String imageUrl;
    private boolean isRead;
    private LocalDateTime createdAt;
    private LocalDateTime readAt;

    public static NotificationResponse from(Notification notification) {
        return NotificationResponse.builder()
            .id(notification.getId())
            .userId(notification.getUserId())
            .type(notification.getType())
            .message(notification.getMessage())
            .targetUrl(notification.getTargetUrl())
            .imageUrl(notification.getImageUrl())
            .isRead(notification.isRead())
            .createdAt(notification.getCreatedAt())
            .readAt(notification.getReadAt())
            .build();
    }

}
