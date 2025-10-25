package org.codeit.roomunion.meeting.adapter.out.persistence;

import jakarta.persistence.EntityManager;
import org.codeit.roomunion.common.exception.CustomException;
import org.codeit.roomunion.meeting.adapter.out.persistence.entity.AppointmentEntity;
import org.codeit.roomunion.meeting.adapter.out.persistence.entity.MeetingEntity;
import org.codeit.roomunion.meeting.adapter.out.persistence.jpa.AppointmentDslRepository;
import org.codeit.roomunion.meeting.adapter.out.persistence.jpa.MeetingJpaRepository;
import org.codeit.roomunion.meeting.adapter.out.persistence.jpa.MeetingMemberDslRepository;
import org.codeit.roomunion.meeting.application.port.out.AppointmentRepository;
import org.codeit.roomunion.meeting.domain.command.AppointmentCreateCommand;
import org.codeit.roomunion.meeting.domain.command.AppointmentModifyCommand;
import org.codeit.roomunion.meeting.domain.model.Appointment;
import org.codeit.roomunion.meeting.exception.MeetingErrorCode;
import org.codeit.roomunion.user.adapter.out.persistence.entity.UserEntity;
import org.codeit.roomunion.user.domain.model.User;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public class AppointmentRepositoryImpl implements AppointmentRepository {

    private final EntityManager entityManager;
    private final MeetingJpaRepository meetingJpaRepository;
    private final MeetingMemberDslRepository meetingMemberDslRepository;
    private final AppointmentDslRepository appointmentDslRepository;

    public AppointmentRepositoryImpl(
        EntityManager entityManager,
        MeetingJpaRepository meetingJpaRepository,
        MeetingMemberDslRepository meetingMemberDslRepository,
        AppointmentDslRepository appointmentDslRepository
    ) {
        this.entityManager = entityManager;
        this.meetingJpaRepository = meetingJpaRepository;
        this.meetingMemberDslRepository = meetingMemberDslRepository;
        this.appointmentDslRepository = appointmentDslRepository;
    }

    @Override
    public Appointment save(AppointmentCreateCommand command, User user, boolean hasImage, LocalDateTime currentAt) {
        MeetingEntity meetingEntity = meetingJpaRepository.findByIdWithAppointments(command.getMeetingId())
            .orElseThrow(() -> new CustomException(MeetingErrorCode.MEETING_NOT_FOUND));

        UserEntity userProxy = entityManager.getReference(UserEntity.class, user.getId());
        AppointmentEntity appointmentEntity = AppointmentEntity.from(command, hasImage, meetingEntity);
        appointmentEntity.join(userProxy, currentAt);

        meetingEntity.createAppointment(appointmentEntity);
        return appointmentEntity.toDomain();
    }

    @Override
    public Appointment modify(AppointmentModifyCommand command, User user, boolean hasImage, LocalDateTime currentAt) {
        MeetingEntity meetingEntity = meetingJpaRepository.findByIdWithAppointments(command.getMeetingId())
            .orElseThrow(() -> new CustomException(MeetingErrorCode.MEETING_NOT_FOUND));

        AppointmentEntity appointmentEntity = meetingEntity.getAppointments()
            .stream()
            .filter(entity -> entity.equalsById(command.getAppointmentId()))
            .findFirst()
            .orElseThrow(() -> new CustomException(MeetingErrorCode.APPOINTMENT_NOT_FOUND));

        appointmentEntity.modify(command, hasImage);

        return appointmentEntity.toDomain();
    }

    @Override
    public Appointment deleteAppointment(Long meetingId, Long appointmentId) {
        return meetingJpaRepository.findByIdWithAppointments(meetingId)
            .orElseThrow(() -> new CustomException(MeetingErrorCode.MEETING_NOT_FOUND))
            .deleteAppointment(appointmentId)
            .toDomain();
    }

    @Override
    public void join(Long appointmentId, User user, LocalDateTime currentAt) {
        AppointmentEntity appointment = appointmentDslRepository.findByIdWithMembers(appointmentId)
            .orElseThrow(() -> new CustomException(MeetingErrorCode.APPOINTMENT_NOT_FOUND));
        if (appointment.isMember(user.getId())) {
            throw new CustomException(MeetingErrorCode.APPOINTMENT_ALREADY_JOINED);
        }

        UserEntity userProxy = entityManager.getReference(UserEntity.class, user.getId());
        appointment.join(userProxy, currentAt);
    }

    @Override
    public void leave(Long appointmentId, User user) {
        AppointmentEntity appointment = appointmentDslRepository.findByIdWithMembers(appointmentId)
            .orElseThrow(() -> new CustomException(MeetingErrorCode.APPOINTMENT_NOT_FOUND));
        if (!appointment.isMember(user.getId())) {
            throw new CustomException(MeetingErrorCode.APPOINTMENT_NOT_JOINED);
        }

        appointment.leave(user.getId());
    }
}
