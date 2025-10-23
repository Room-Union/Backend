package org.codeit.roomunion.meeting.domain.model;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Appointment {

    public static final String PROFILE_IMAGE_PATH = "appointment/%s/image";

    private final Long id;
    private final String title;
    private final int maxMemberCount;
    private final LocalDateTime scheduledAt;
    private final boolean hasImage;

    private Appointment(Long id, String title, int maxMemberCount, LocalDateTime scheduledAt, boolean hasImage) {
        this.id = id;
        this.title = title;
        this.maxMemberCount = maxMemberCount;
        this.scheduledAt = scheduledAt;
        this.hasImage = hasImage;
    }

    public static Appointment of(Long id, String title, int maxMemberCount, LocalDateTime scheduledAt, boolean hasImage) {
        return new Appointment(id, title, maxMemberCount, scheduledAt, hasImage);
    }

    public String getProfileImagePath() {
        return PROFILE_IMAGE_PATH.formatted(id);
    }
}
