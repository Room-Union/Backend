package org.codeit.roomunion.meeting.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.codeit.roomunion.user.adapter.out.persistence.entity.UserEntity;

import java.time.LocalDateTime;

@Entity(name = "appointment_members")
@Getter
class AppointmentMemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = false)
    private AppointmentEntity appointment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    private LocalDateTime createdAt;

    protected AppointmentMemberEntity() {
    }

    private AppointmentMemberEntity(AppointmentEntity appointment, UserEntity user, LocalDateTime currentAt) {
        this.appointment = appointment;
        this.user = user;
        this.createdAt = currentAt;
    }

    public static AppointmentMemberEntity of(AppointmentEntity appointment, UserEntity user, LocalDateTime currentAt) {
        return new AppointmentMemberEntity(appointment, user, currentAt);
    }

}
