package org.codeit.roomunion.meeting.domain.model;

import lombok.Getter;
import org.codeit.roomunion.meeting.domain.model.enums.MeetingBadge;
import org.codeit.roomunion.meeting.domain.model.enums.MeetingCategory;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class Meeting {

    private static final String IMAGE_PATH_TEMPLATE = "meeting/%s";

    private final Long id;

    private final String name;

    private final String description;

    private final String meetingImage;

    private final MeetingCategory category;

    private final int maxMemberCount;

    private final List<String> platformURL;

    private final LocalDateTime createdAt;

    private final Long userId;

    private final String hostNickname;

//    private final String hostProfileImage;

    private final boolean isJoined;

    private final List<MeetingBadge> badges;

    public static String getImagePath(String uuid) {
        return String.format(IMAGE_PATH_TEMPLATE, uuid);
    }

    public Meeting withHostInfo(String hostNickname) {
        return new Meeting(
            this.id, this.name, this.description, this.meetingImage, this.category, this.maxMemberCount, this.userId, this.platformURL, this.createdAt, hostNickname, this.isJoined, this.badges
        );
    }

    public Meeting withBadges(List<MeetingBadge> badges) {
        return new Meeting(
            this.id, this.name, this.description, this.meetingImage, this.category, this.maxMemberCount, this.userId, this.platformURL, this.createdAt, this.hostNickname, this.isJoined, badges
        );
    }

    private Meeting(Long id, String name, String description, String meetingImage, MeetingCategory category, int maxMemberCount, Long userId, List<String> platformURL, LocalDateTime createdAt,
        String hostNickname, boolean isJoined, List<MeetingBadge> badges) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.meetingImage = meetingImage;
        this.category = category;
        this.maxMemberCount = maxMemberCount;
        this.userId = userId;
        this.platformURL = platformURL;
        this.createdAt = createdAt;
        this.hostNickname = hostNickname;
        this.isJoined = isJoined;
        this.badges = badges;
    }

    public static Meeting of(Long id, String name, String description, String meetingImage, MeetingCategory category, int maxMemberCount, Long userId, List<String> platformURL,
        LocalDateTime createdAt, String hostNickname, boolean isJoined) {
        return new Meeting(id, name, description, meetingImage, category, maxMemberCount, userId, platformURL, createdAt, hostNickname, isJoined, List.of());
    }

    public Meeting withJoined(boolean joined) {
        return new Meeting(
            this.id, this.name, this.description, this.meetingImage, this.category, this.maxMemberCount, this.userId, this.platformURL, this.createdAt, this.hostNickname, joined,this.badges
        );
    }


}
