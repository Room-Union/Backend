package org.codeit.roomunion.meeting.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.codeit.roomunion.meeting.domain.command.AppointmentCreateCommand;
import org.codeit.roomunion.meeting.domain.command.AppointmentModifyCommand;
import org.codeit.roomunion.meeting.domain.model.Appointment;
import org.codeit.roomunion.user.adapter.out.persistence.entity.UserEntity;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "appointments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AppointmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id", nullable = false)
    private MeetingEntity meeting;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDateTime scheduledAt;

    @Column(nullable = false)
    private Integer maxMemberCount;

    @Column(nullable = false)
    @ColumnDefault(value = "false")
    private Boolean hasImage;

    @OneToMany(mappedBy = "appointment", cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<AppointmentMemberEntity> members = new ArrayList<>();

    private AppointmentEntity(String title, LocalDateTime scheduledAt, Integer maxMemberCount, Boolean hasImage, MeetingEntity meeting) {
        this.title = title;
        this.scheduledAt = scheduledAt;
        this.maxMemberCount = maxMemberCount;
        this.hasImage = hasImage;
        this.meeting = meeting;
    }

    public static AppointmentEntity from(AppointmentCreateCommand command, boolean hasImage, MeetingEntity meetingEntity) {
        return new AppointmentEntity(command.getTitle(), command.getScheduledAt(), command.getMaxMemberCount(), hasImage, meetingEntity);
    }

    public void join(UserEntity user, LocalDateTime currentAt) {
        AppointmentMemberEntity appointmentMemberEntity = AppointmentMemberEntity.of(this, user, currentAt);
        members.add(appointmentMemberEntity);
    }

    public boolean isMember(Long userId) {
        return members.stream()
            .anyMatch(member -> member.getUser().getId().equals(userId));
    }

    public void leave(Long userId) {
        members.removeIf(member -> member.getUser().getId().equals(userId));
    }

    public Appointment toDomain() {
        return Appointment.of(id, title, maxMemberCount, scheduledAt, hasImage);
    }

    public boolean equalsById(Long appointmentId) {
        return id.equals(appointmentId);
    }

    public void modify(AppointmentModifyCommand command, boolean hasImage) {
        this.title = command.getTitle();
        this.scheduledAt = command.getScheduledAt();
        this.maxMemberCount = command.getMaxMemberCount();
        if (hasImage) {
            this.hasImage = true;
        }
    }
}
