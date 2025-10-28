package org.codeit.roomunion.meeting.domain.command;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AppointmentCreateCommand {

    private final Long meetingId;
    private final String title;
    private final int maxMemberCount;
    private final LocalDateTime scheduledAt;

    private AppointmentCreateCommand(Long meetingId, String title, int maxMemberCount, LocalDateTime scheduledAt) {
        this.meetingId = meetingId;
        this.title = title;
        this.maxMemberCount = maxMemberCount;
        this.scheduledAt = scheduledAt;
    }

    public static AppointmentCreateCommand of(Long meetingId, String title, int maxMemberCount, LocalDateTime scheduledAt) {
        return new AppointmentCreateCommand(meetingId, title, maxMemberCount, scheduledAt);
    }
}
