package org.codeit.roomunion.meeting.application.port.out;

import org.codeit.roomunion.auth.domain.model.CustomUserDetails;
import org.codeit.roomunion.meeting.domain.command.AppointmentCreateCommand;
import org.codeit.roomunion.meeting.domain.command.AppointmentModifyCommand;
import org.codeit.roomunion.meeting.domain.model.Appointment;
import org.codeit.roomunion.user.domain.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository {

    Appointment save(AppointmentCreateCommand command, User user, boolean hasImage, LocalDateTime currentAt);

    Appointment modify(AppointmentModifyCommand command, User user, boolean hasImage, LocalDateTime currentAt);

    Appointment deleteAppointment(Long meetingId, Long appointmentId);

    void join(Long appointmentId, User user, LocalDateTime currentAt);

    void leave(Long appointmentId, User user);

    List<Appointment> findAllBy(Long meetingId, CustomUserDetails userDetails);
}
