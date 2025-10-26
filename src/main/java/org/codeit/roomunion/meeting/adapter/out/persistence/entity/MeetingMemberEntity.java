package org.codeit.roomunion.meeting.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.codeit.roomunion.meeting.domain.model.MeetingRole;
import org.codeit.roomunion.user.adapter.out.persistence.entity.UserEntity;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
    name = "meeting_member",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_meeting_user", columnNames = {"meeting_id", "user_id"})
    },
    indexes = {
        @Index(name = "idx_meeting_member_meeting_id", columnList = "meeting_id"),
        @Index(name = "idx_meeting_member_user_id", columnList = "user_id")
    }

)
public class MeetingMemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id", nullable = false)
    private MeetingEntity meeting;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MeetingRole meetingRole;

    public boolean isHost() {
        return this.meetingRole == MeetingRole.HOST;
    }

    public static MeetingMemberEntity of(MeetingEntity meeting, UserEntity user) {
        return MeetingMemberEntity.builder()
            .meeting(meeting)
            .user(user)
            .meetingRole(MeetingRole.MEMBER)
            .build();
    }


}
