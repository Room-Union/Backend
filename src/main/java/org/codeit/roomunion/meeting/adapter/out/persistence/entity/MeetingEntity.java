package org.codeit.roomunion.meeting.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.codeit.roomunion.meeting.domain.model.Meeting;
import org.codeit.roomunion.meeting.domain.model.command.MeetingCreateCommand;
import org.codeit.roomunion.meeting.domain.model.enums.MeetingCategory;
import org.codeit.roomunion.meeting.domain.model.enums.MeetingRole;
import org.codeit.roomunion.user.adapter.out.persistence.entity.UserEntity;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "meeting")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeetingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(name = "meeting_image")
    private String meetingImage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MeetingCategory category;

    @Column(nullable = false)
    @ColumnDefault("1")
    private int maxMemberCount;

    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<MeetingMemberEntity> meetingMembers = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "meeting_platform_urls", joinColumns = @JoinColumn(name = "meeting_id"))
    @Column(name = "platform_url", length = 500)
    private List<String> platformUrls = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public static MeetingEntity from(MeetingCreateCommand command, UserEntity hostUser) {
        MeetingEntity meeting = MeetingEntity.builder()
            .name(command.getName())
            .description(command.getDescription())
            .category(command.getCategory())
            .meetingImage(command.getImageUrl())
            .maxMemberCount(command.getMaxMemberCount())
            .platformUrls(command.getPlatformURL())
            .createdAt(command.getCreatedAt())
            .build();
        meeting.addHost(hostUser);
        return meeting;
    }

    public void addHost(UserEntity hostUser) {
        MeetingMemberEntity hostMember = MeetingMemberEntity.builder()
            .meeting(this)
            .user(hostUser)
            .meetingRole(MeetingRole.HOST)
            .build();
        this.addMember(hostMember);
    }


    public Meeting toDomain(Long hostUserId, String hostNickname) {
        return Meeting.of(
            this.getId(),
            this.getName(),
            this.getDescription(),
            this.getMeetingImage(),
            this.getCategory(),
            this.getMaxMemberCount(),
            hostUserId,
            this.getPlatformUrls(),
            this.getCreatedAt(),
            hostNickname
        );
    }

    public void addMember(MeetingMemberEntity member) {
        this.meetingMembers.add(member);
    }


}
