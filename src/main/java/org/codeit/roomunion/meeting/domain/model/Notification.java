package org.codeit.roomunion.meeting.domain.model;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class Notification {

    private final Long id;

    private final Long userId;

    private final NotificationType type;

    private final String message;

    private final String targetUrl;

    private final String imageUrl;

    private final boolean isRead;

    private final LocalDateTime createdAt;

    private final LocalDateTime readAt;

    public Notification(Long id, Long userId, NotificationType type, String message, String targetUrl,
        String imageUrl, boolean isRead, LocalDateTime createdAt, LocalDateTime readAt) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.message = message;
        this.targetUrl = targetUrl;
        this.imageUrl = imageUrl;
        this.isRead = isRead;
        this.createdAt = createdAt;
        this.readAt = readAt;
    }

    public static Notification of(Long id, Long userId, NotificationType type, String message, String targetUrl, String imageUrl, boolean isRead, LocalDateTime createdAt, LocalDateTime readAt) {
        return new Notification(id, userId, type, message, targetUrl, imageUrl, isRead, createdAt, readAt);
    }
}
