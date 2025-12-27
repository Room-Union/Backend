package org.codeit.roomunion.meeting.domain.command.notification;

import lombok.Getter;

@Getter
public class SubscribeNotificationCommand {

    private final Long userId;

    public SubscribeNotificationCommand(Long userId) {
        this.userId = userId;
    }

    public static SubscribeNotificationCommand of(Long userId) {
        return new SubscribeNotificationCommand(userId);
    }
}
