package org.codeit.roomunion.meeting.domain.command;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AppointmentModifyCommand {

    private final Long meetingId;
    private final Long appointmentId;
    private final String title;
    private final int maxMemberCount;
    private final LocalDateTime scheduledAt;

    private AppointmentModifyCommand(Long meetingId, Long appointmentId, String title, int maxMemberCount, LocalDateTime scheduledAt) {
        this.meetingId = meetingId;
        this.appointmentId = appointmentId;
        this.title = title;
        this.maxMemberCount = maxMemberCount;
        this.scheduledAt = scheduledAt;
    }

    public static AppointmentModifyCommand of(Long meetingId, Long appointmentId, String title, int maxMemberCount, LocalDateTime scheduledAt) {
        return new AppointmentModifyCommand(meetingId, appointmentId, title, maxMemberCount, scheduledAt);
    }
}
