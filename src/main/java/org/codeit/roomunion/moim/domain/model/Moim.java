package org.codeit.roomunion.moim.domain.model;

import lombok.Getter;
import org.codeit.roomunion.moim.domain.model.enums.MoimCategory;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class Moim {
    private final Long id;

    private final String name;

    private final String description;

    private final String moimImage;

    private final MoimCategory category;

    private final int maxMemberCount;

    private final Long userId;

    private final List<String> platformURL;

    private final LocalDateTime createdAt;

    public Moim(Long id, String name, String description, String moimImage, MoimCategory category, int maxMemberCount, Long userId, List<String> platformURL, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.moimImage = moimImage;
        this.category = category;
        this.maxMemberCount = maxMemberCount;
        this.userId = userId;
        this.platformURL = platformURL;
        this.createdAt = createdAt;
    }

    public static Moim of(Long id, String name, String description, String moimImage, MoimCategory category, int maxMemberCount, Long userId, List<String> platformURL, LocalDateTime createdAt) {
        return new Moim(id, name, description, moimImage, category, maxMemberCount, userId, platformURL, createdAt);
    }

}
