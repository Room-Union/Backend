package org.codeit.roomunion.meeting.domain.command.notification;

import lombok.Getter;
import org.codeit.roomunion.meeting.domain.model.NotificationType;

@Getter
public class CreateAndSendNotificationCommand {

    private final Long userId;

    private final NotificationType type;

    private final String message;

    private final String targetUrl;

    private final String imageUrl;


    public CreateAndSendNotificationCommand(Long userId, NotificationType type, String message,
        String targetUrl, String imageUrl) {
        this.userId = userId;
        this.type = type;
        this.message = message;
        this.targetUrl = targetUrl;
        this.imageUrl = imageUrl;
    }

    public static CreateAndSendNotificationCommand of(Long userId, NotificationType type, String message, String targetUrl, String imageUrl) {
        return new CreateAndSendNotificationCommand(userId, type, message, targetUrl, imageUrl);
    }
}
