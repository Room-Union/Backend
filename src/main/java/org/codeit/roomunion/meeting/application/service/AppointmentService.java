package org.codeit.roomunion.meeting.application.service;

import org.codeit.roomunion.auth.domain.model.CustomUserDetails;
import org.codeit.roomunion.common.adapter.out.s3.AmazonS3Manager;
import org.codeit.roomunion.common.application.port.out.TimeHolder;
import org.codeit.roomunion.common.exception.CustomException;
import org.codeit.roomunion.meeting.application.port.in.AppointmentCommandUseCase;
import org.codeit.roomunion.meeting.application.port.in.MeetingQueryUseCase;
import org.codeit.roomunion.meeting.application.port.out.AppointmentRepository;
import org.codeit.roomunion.meeting.domain.command.AppointmentCreateCommand;
import org.codeit.roomunion.meeting.domain.command.AppointmentModifyCommand;
import org.codeit.roomunion.meeting.domain.model.Appointment;
import org.codeit.roomunion.meeting.domain.model.Meeting;
import org.codeit.roomunion.meeting.domain.policy.AppointmentPolicy;
import org.codeit.roomunion.meeting.exception.MeetingErrorCode;
import org.codeit.roomunion.user.domain.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
public class AppointmentService implements AppointmentCommandUseCase {

    private final MeetingQueryUseCase meetingQueryUseCase;
    private final AppointmentRepository appointmentRepository;
    private final AmazonS3Manager amazonS3Manager;

    private final TimeHolder timeHolder;

    public AppointmentService(MeetingQueryUseCase meetingQueryUseCase, AppointmentRepository appointmentRepository, AmazonS3Manager amazonS3Manager, TimeHolder timeHolder) {
        this.meetingQueryUseCase = meetingQueryUseCase;
        this.appointmentRepository = appointmentRepository;
        this.amazonS3Manager = amazonS3Manager;
        this.timeHolder = timeHolder;
    }

    @Override
    @Transactional
    public void create(CustomUserDetails userDetails, AppointmentCreateCommand command, MultipartFile image) {
        LocalDateTime currentAt = timeHolder.localDateTime();
        AppointmentPolicy.validate(command, currentAt);

        Meeting meeting = meetingQueryUseCase.getByMeetingId(command.getMeetingId(), userDetails);

        User user = userDetails.getUser();
        AppointmentPolicy.validateIsHost(meeting, user);

        Appointment appointment = appointmentRepository.save(command, user, hasImage(image), currentAt);

        updateProfileImage(appointment, image);
    }

    @Override
    @Transactional
    public void modify(CustomUserDetails userDetails, AppointmentModifyCommand command, MultipartFile image) {
        LocalDateTime currentAt = timeHolder.localDateTime();
        AppointmentPolicy.validate(command, currentAt);

        Meeting meeting = meetingQueryUseCase.getByMeetingId(command.getMeetingId(), userDetails);

        User user = userDetails.getUser();
        AppointmentPolicy.validateIsHost(meeting, user);

        Appointment appointment = appointmentRepository.modify(command, user, hasImage(image), currentAt);

        updateProfileImage(appointment, image);
    }

    @Override
    @Transactional
    public void delete(CustomUserDetails customUserDetails, Long meetingId, Long appointmentId) {
        Meeting meeting = meetingQueryUseCase.getByMeetingId(meetingId, customUserDetails);

        User user = customUserDetails.getUser();
        AppointmentPolicy.validateIsHost(meeting, user);

        Appointment appointment = appointmentRepository.deleteAppointment(meetingId, appointmentId);

        amazonS3Manager.deleteFile(appointment.getProfileImagePath());
    }

    @Override
    @Transactional
    public void join(CustomUserDetails userDetails, Long meetingId, Long appointmentId) {
        User user = userDetails.getUser();
        boolean isMember = meetingQueryUseCase.existsMemberBy(meetingId, user);
        if (!isMember) {
            throw new CustomException(MeetingErrorCode.NOT_JOINED);
        }

        LocalDateTime currentAt = timeHolder.localDateTime();
        appointmentRepository.join(appointmentId, user, currentAt);
    }

    @Override
    @Transactional
    public void leave(CustomUserDetails userDetails, Long appointmentId) {
        User user = userDetails.getUser();
        appointmentRepository.leave(appointmentId, user);
    }

    private boolean hasImage(MultipartFile profileImage) {
        return profileImage != null && !profileImage.isEmpty() && !Objects.equals(profileImage.getOriginalFilename(), "null");
    }

    private void updateProfileImage(Appointment appointment, MultipartFile profileImage) {
        if (hasImage(profileImage)) {
            amazonS3Manager.uploadFile(appointment.getProfileImagePath(), profileImage);
        }
    }
}
