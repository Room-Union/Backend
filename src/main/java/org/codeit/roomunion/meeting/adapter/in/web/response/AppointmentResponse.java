package org.codeit.roomunion.meeting.adapter.in.web.response;

import org.codeit.roomunion.meeting.domain.model.Appointment;

import java.time.LocalDateTime;

public record AppointmentResponse(
    Long id,
    String title,
    int maxMemberCount,
    LocalDateTime scheduledAt,
    boolean hasImage,
    int currentMemberCount,
    boolean isJoined
) {
    public static AppointmentResponse from(Appointment appointment) {
        return new AppointmentResponse(
            appointment.getId(),
            appointment.getTitle(),
            appointment.getMaxMemberCount(),
            appointment.getScheduledAt(),
            appointment.isHasImage(),
            appointment.getCurrentMemberCount(),
            appointment.isJoined()
        );
    }
}