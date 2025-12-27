package org.codeit.roomunion.meeting.domain.command.notification;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;

@Getter
public class ReadNotificationCommand {

    private final Long userId;

    private final List<Long> ids; // null 이나 empty면 전체 읽음 처리

    private final LocalDateTime readAt; // null이면 현재로 처리

    public ReadNotificationCommand(Long userId, List<Long> ids, LocalDateTime readAt) {
        this.userId = userId;
        this.ids = ids;
        this.readAt = readAt;
    }

    public static ReadNotificationCommand of(Long userId, List<Long> ids, LocalDateTime readAt) {
        return new ReadNotificationCommand(userId, ids, readAt);
    }
}
