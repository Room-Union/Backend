package org.codeit.roomunion.meeting.domain.model;

import lombok.Getter;
import org.codeit.roomunion.meeting.domain.model.enums.MeetingBadge;
import org.codeit.roomunion.meeting.domain.model.enums.MeetingCategory;
import org.codeit.roomunion.user.domain.model.User;

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

    private final int currentMemberCount;

    private final List<String> platformURL;

    private final LocalDateTime createdAt;

//    private final String hostProfileImage;

    private final boolean isJoined;

    private final List<MeetingBadge> badges;

    private final User host;

    public static String getImagePath(String uuid) {
        return String.format(IMAGE_PATH_TEMPLATE, uuid);
    }

    public Meeting withHost(User host) {
        return new Meeting(
            this.id, this.name, this.description, this.meetingImage, this.category, this.maxMemberCount, this.currentMemberCount, this.platformURL, this.createdAt, this.isJoined, this.badges, host
        );
    }

    public Meeting withBadges(List<MeetingBadge> badges) {
        return new Meeting(
            this.id, this.name, this.description, this.meetingImage, this.category, this.maxMemberCount, this.currentMemberCount, this.platformURL, this.createdAt, this.isJoined, badges, this.host
        );
    }

    private Meeting(Long id, String name, String description, String meetingImage, MeetingCategory category, int maxMemberCount, int currentMemberCount, List<String> platformURL, LocalDateTime createdAt,
                    boolean isJoined, List<MeetingBadge> badges, User host) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.meetingImage = meetingImage;
        this.category = category;
        this.maxMemberCount = maxMemberCount;
        this.currentMemberCount = currentMemberCount;
        this.platformURL = platformURL;
        this.createdAt = createdAt;
        this.isJoined = isJoined;
        this.badges = badges;
        this.host = host;
    }

    public static Meeting of(Long id, String name, String description, String meetingImage, MeetingCategory category, int maxMemberCount, int currentMemberCount, List<String> platformURL,
                             LocalDateTime createdAt, boolean isJoined, User host) {
        return new Meeting(id, name, description, meetingImage, category, maxMemberCount, currentMemberCount, platformURL, createdAt, isJoined, List.of(), host);
    }

    public Meeting withJoined(boolean joined) {
        return new Meeting(
            this.id, this.name, this.description, this.meetingImage, this.category, this.maxMemberCount, this.currentMemberCount, this.platformURL, this.createdAt, joined, this.badges, this.host
        );
    }



}
