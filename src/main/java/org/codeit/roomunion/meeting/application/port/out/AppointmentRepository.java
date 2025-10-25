package org.codeit.roomunion.meeting.application.port.out;

import org.codeit.roomunion.meeting.domain.command.AppointmentCreateCommand;
import org.codeit.roomunion.meeting.domain.command.AppointmentModifyCommand;
import org.codeit.roomunion.meeting.domain.model.Appointment;
import org.codeit.roomunion.user.domain.model.User;

import java.time.LocalDateTime;

public interface AppointmentRepository {

    Appointment save(AppointmentCreateCommand command, User user, boolean hasImage, LocalDateTime currentAt);

    Appointment modify(AppointmentModifyCommand command, User user, boolean hasImage, LocalDateTime currentAt);

    Appointment deleteAppointment(Long meetingId, Long appointmentId);

    void join(Long appointmentId, User user, LocalDateTime currentAt);
}
