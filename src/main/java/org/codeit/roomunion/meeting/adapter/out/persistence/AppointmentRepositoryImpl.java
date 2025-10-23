package org.codeit.roomunion.meeting.adapter.out.persistence;

import jakarta.persistence.EntityManager;
import org.codeit.roomunion.common.exception.CustomException;
import org.codeit.roomunion.meeting.adapter.out.persistence.entity.AppointmentEntity;
import org.codeit.roomunion.meeting.adapter.out.persistence.entity.MeetingEntity;
import org.codeit.roomunion.meeting.adapter.out.persistence.jpa.MeetingJpaRepository;
import org.codeit.roomunion.meeting.application.port.out.AppointmentRepository;
import org.codeit.roomunion.meeting.domain.command.AppointmentCreateCommand;
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

    public AppointmentRepositoryImpl(EntityManager entityManager, MeetingJpaRepository meetingJpaRepository) {
        this.entityManager = entityManager;
        this.meetingJpaRepository = meetingJpaRepository;
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
}
