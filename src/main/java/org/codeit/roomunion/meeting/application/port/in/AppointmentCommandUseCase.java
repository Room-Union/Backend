package org.codeit.roomunion.meeting.application.port.in;

import org.codeit.roomunion.meeting.domain.command.AppointmentCreateCommand;
import org.springframework.web.multipart.MultipartFile;

public interface AppointmentCommandUseCase {

    void create(AppointmentCreateCommand appointmentCreateCommand, MultipartFile image);

}
