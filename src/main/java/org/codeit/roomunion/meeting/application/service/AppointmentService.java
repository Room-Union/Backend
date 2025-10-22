package org.codeit.roomunion.meeting.application.service;

import org.codeit.roomunion.meeting.application.port.in.AppointmentCommandUseCase;
import org.codeit.roomunion.meeting.domain.command.AppointmentCreateCommand;
import org.springframework.web.multipart.MultipartFile;

public class AppointmentService implements AppointmentCommandUseCase {

    @Override
    public void create(AppointmentCreateCommand appointmentCreateCommand, MultipartFile image) {
    }
}
