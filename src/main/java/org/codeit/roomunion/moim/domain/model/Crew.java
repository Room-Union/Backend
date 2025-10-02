package org.codeit.roomunion.moim.domain.model;

import lombok.Getter;
import org.codeit.roomunion.moim.domain.model.enums.CrewCategory;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class Crew {
    private final Long id;

    private final String name;

    private final String description;

    private final String crewImage;

    private final CrewCategory category;

    private final int maxMemberCount;

    private final Long userId;

    private final List<String> platformURL;

    private final LocalDateTime createdAt;

    public Crew(Long id, String name, String description, String crewImage, CrewCategory category, int maxMemberCount, Long userId, List<String> platformURL, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.crewImage = crewImage;
        this.category = category;
        this.maxMemberCount = maxMemberCount;
        this.userId = userId;
        this.platformURL = platformURL;
        this.createdAt = createdAt;
    }

    public static Crew of(Long id, String name, String description, String crewImage, CrewCategory category, int maxMemberCount, Long userId, List<String> platformURL, LocalDateTime createdAt) {
        return new Crew(id, name, description, crewImage, category, maxMemberCount, userId, platformURL, createdAt);
    }

}
