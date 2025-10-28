package org.codeit.roomunion.meeting.application.port.in;

import org.codeit.roomunion.auth.domain.model.CustomUserDetails;
import org.codeit.roomunion.meeting.domain.model.Appointment;

import java.util.List;

public interface AppointmentQueryUseCase {

    List<Appointment> getAppointments(Long meetingId, CustomUserDetails userDetails);
}