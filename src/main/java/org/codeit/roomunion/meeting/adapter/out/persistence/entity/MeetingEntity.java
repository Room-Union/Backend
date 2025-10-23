package org.codeit.roomunion.meeting.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.codeit.roomunion.common.exception.CustomException;
import org.codeit.roomunion.meeting.domain.model.Meeting;
import org.codeit.roomunion.meeting.domain.command.MeetingCreateCommand;
import org.codeit.roomunion.meeting.domain.model.MeetingCategory;
import org.codeit.roomunion.meeting.domain.model.MeetingRole;
import org.codeit.roomunion.meeting.exception.MeetingErrorCode;
import org.codeit.roomunion.user.adapter.out.persistence.entity.UserEntity;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
    name = "meeting",
    indexes = {
        @Index(name = "idx_meeting_created_at", columnList = "createdAt"),
        @Index(name = "idx_meeting_category_created_at", columnList = "category, createdAt")
    }
)
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

    @Column(nullable = false)
    @ColumnDefault("0")
    private int currentMemberCount;

    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<MeetingMemberEntity> meetingMembers = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "meeting_platform_urls", joinColumns = @JoinColumn(name = "meeting_id"))
    @Column(name = "platform_url", length = 500)
    @Builder.Default
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
            .currentMemberCount(0)
            .platformUrls(command.getPlatformURL())
            .createdAt(command.getCreatedAt())
            .build();
        meeting.addHost(hostUser);
        return meeting;
    }

    private void addHost(UserEntity hostUser) {
        MeetingMemberEntity hostMember = MeetingMemberEntity.builder()
            .meeting(this)
            .user(hostUser)
            .meetingRole(MeetingRole.HOST)
            .build();
        this.addMember(hostMember);
    }


    public Meeting toDomain() {

        MeetingMemberEntity hostMember = this.meetingMembers.stream()
            .filter(MeetingMemberEntity::isHost)
            .findFirst()
            .orElseThrow(() -> new CustomException(MeetingErrorCode.MEETING_HOST_NOT_FOUND));

        UserEntity host = hostMember.getUser();

        return Meeting.of(
            this.getId(),
            this.getName(),
            this.getDescription(),
            this.getMeetingImage(),
            this.getCategory(),
            this.getMaxMemberCount(),
            this.getCurrentMemberCount(),
            this.getPlatformUrls(),
            this.getCreatedAt(),
            false,
            host.toDomain()
        );
    }

    public void addMember(MeetingMemberEntity member) {
        if (this.meetingMembers.size() >= this.maxMemberCount) {
            throw new CustomException(MeetingErrorCode.MEETING_MEMBER_LIMIT_REACHED);
        }
        this.meetingMembers.add(member);
        this.currentMemberCount = this.meetingMembers.size();
    }

    public void applyFromDomain(Meeting meeting) {
        this.name = meeting.getName();
        this.description = meeting.getDescription();
        this.meetingImage = meeting.getMeetingImage();
        this.category = meeting.getCategory();
        this.maxMemberCount = meeting.getMaxMemberCount();
        this.currentMemberCount = this.meetingMembers.size();

        this.platformUrls.clear();
        this.platformUrls.addAll(meeting.getPlatformURL());
    }

}
