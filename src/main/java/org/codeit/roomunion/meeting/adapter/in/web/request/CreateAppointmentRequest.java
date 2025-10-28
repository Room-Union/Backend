package org.codeit.roomunion.meeting.adapter.in.web.request;

import org.codeit.roomunion.meeting.domain.command.AppointmentCreateCommand;

import java.time.LocalDateTime;

public record CreateAppointmentRequest(String title, int maxMemberCount, LocalDateTime scheduledAt) {
    public AppointmentCreateCommand toCommand(Long meetingId) {
        return AppointmentCreateCommand.of(meetingId, title, maxMemberCount, scheduledAt);
    }
}
