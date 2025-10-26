package org.codeit.roomunion.meeting.adapter.in.web.response;

import org.codeit.roomunion.meeting.domain.model.Appointment;

import java.time.LocalDateTime;

public record AppointmentResponse(
    Long id,
    String title,
    int maxMemberCount,
    LocalDateTime scheduledAt,
    String profileImageUrl,
    int currentMemberCount,
    boolean isJoined
) {
    public static AppointmentResponse from(Appointment appointment) {
        return new AppointmentResponse(
            appointment.getId(),
            appointment.getTitle(),
            appointment.getMaxMemberCount(),
            appointment.getScheduledAt(),
            appointment.getProfileImageUrl(),
            appointment.getCurrentMemberCount(),
            appointment.isJoined()
        );
    }
}