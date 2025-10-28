package org.codeit.roomunion.meeting.adapter.in.web.request;

import org.codeit.roomunion.meeting.domain.command.AppointmentModifyCommand;

import java.time.LocalDateTime;

public record ModifyAppointmentRequest(String title, int maxMemberCount, LocalDateTime scheduledAt) {
    public AppointmentModifyCommand toCommand(Long meetingId, Long appointmentId) {
        return AppointmentModifyCommand.of(meetingId, appointmentId, title, maxMemberCount, scheduledAt);
    }
}
