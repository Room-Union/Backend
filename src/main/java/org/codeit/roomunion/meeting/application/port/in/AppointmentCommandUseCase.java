package org.codeit.roomunion.meeting.application.port.in;

import org.codeit.roomunion.auth.domain.model.CustomUserDetails;
import org.codeit.roomunion.meeting.domain.command.AppointmentCreateCommand;
import org.codeit.roomunion.meeting.domain.command.AppointmentModifyCommand;
import org.springframework.web.multipart.MultipartFile;

public interface AppointmentCommandUseCase {

    void create(CustomUserDetails userDetails, AppointmentCreateCommand appointmentCreateCommand, MultipartFile image);

    void modify(CustomUserDetails userDetails, AppointmentModifyCommand command, MultipartFile image);

    void delete(CustomUserDetails customUserDetails, Long meetingId, Long appointmentId);

    void join(CustomUserDetails customUserDetails, Long meetingId, Long appointmentId);

    void leave(CustomUserDetails customUserDetails, Long appointmentId);
}
