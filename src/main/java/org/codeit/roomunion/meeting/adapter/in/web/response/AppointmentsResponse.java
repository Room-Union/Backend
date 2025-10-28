package org.codeit.roomunion.meeting.adapter.in.web.response;

import org.codeit.roomunion.meeting.domain.model.Appointment;

import java.util.List;

public record AppointmentsResponse(List<AppointmentResponse> appointments) {
    public static AppointmentsResponse from(List<Appointment> appointments) {
        List<AppointmentResponse> appointmentResponses = appointments.stream()
            .map(AppointmentResponse::from)
            .toList();
        return new AppointmentsResponse(appointmentResponses);
    }
}
