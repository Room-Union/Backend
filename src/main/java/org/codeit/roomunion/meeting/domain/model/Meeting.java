package org.codeit.roomunion.meeting.domain.model;

import lombok.Getter;
import org.codeit.roomunion.meeting.domain.model.enums.MeetingCategory;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Getter
public class Meeting {
    private final Long id;

    private final String name;

    private final String description;

    private final String meetingImage;

    private final MeetingCategory category;

    private final int maxMemberCount;

    private final Long userId;

    private final List<String> platformURL;

    private final LocalDateTime createdAt;

    private static final Meeting EMPTY = new Meeting(
        0L, // 존재하지 않는 ID
        "",  // 기본값
        "",
        null,
        null,
        0,
        null,
        Collections.emptyList(),
        null

    );

    public static Meeting getEmpty() {
        return EMPTY;
    }

    public boolean isEmpty() {
        return this == EMPTY;
    }

    private Meeting(Long id, String name, String description, String meetingImage, MeetingCategory category, int maxMemberCount, Long userId, List<String> platformURL, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.meetingImage = meetingImage;
        this.category = category;
        this.maxMemberCount = maxMemberCount;
        this.userId = userId;
        this.platformURL = platformURL;
        this.createdAt = createdAt;
    }

    public static Meeting of(Long id, String name, String description, String meetingImage, MeetingCategory category, int maxMemberCount, Long userId, List<String> platformURL, LocalDateTime createdAt) {
        return new Meeting(id, name, description, meetingImage, category, maxMemberCount, userId, platformURL, createdAt);
    }

}
