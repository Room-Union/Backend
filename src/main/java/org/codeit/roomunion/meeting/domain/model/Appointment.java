package org.codeit.roomunion.meeting.domain.model;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class Appointment {

    public static final String PROFILE_IMAGE_PATH = "appointment/%s/image";

    private final Long id;
    private final String title;
    private final int maxMemberCount;
    private final LocalDateTime scheduledAt;
    private final String profileImageUrl;
    private final List<Long> memberIds;
    private final boolean isJoined;

    private Appointment(Long id, String title, int maxMemberCount, LocalDateTime scheduledAt, String profileImageUrl, List<Long> memberIds, boolean isJoined) {
        this.id = id;
        this.title = title;
        this.maxMemberCount = maxMemberCount;
        this.scheduledAt = scheduledAt;
        this.profileImageUrl = profileImageUrl;
        this.memberIds = memberIds;
        this.isJoined = isJoined;
    }

    public static Appointment of(Long id, String title, int maxMemberCount, LocalDateTime scheduledAt, String profileImageUrl) {
        return of(id, title, maxMemberCount, scheduledAt, profileImageUrl, List.of(), false);
    }

    public static Appointment of(Long id, String title, int maxMemberCount, LocalDateTime scheduledAt, String profileImageUrl, List<Long> memberIds) {
        return new Appointment(id, title, maxMemberCount, scheduledAt, profileImageUrl, memberIds, false);
    }

    public static Appointment of(Long id, String title, int maxMemberCount, LocalDateTime scheduledAt, String profileImageUrl, List<Long> memberIds, boolean isJoined) {
        return new Appointment(id, title, maxMemberCount, scheduledAt, profileImageUrl, memberIds, isJoined);
    }

    public String getProfileImagePath() {
        return PROFILE_IMAGE_PATH.formatted(id);
    }

    public int getCurrentMemberCount() {
        return memberIds.size();
    }
}
