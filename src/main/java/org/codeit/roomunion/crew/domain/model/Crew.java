package org.codeit.roomunion.crew.domain.model;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;


@Getter
public class Crew {
    private Long id;

    private String name;

    private String description;

    private String crewImage;

//    private Category category;

    private int maxMemberCount;

    private Long userId;

    private List<String> platformURL;

    private LocalDateTime createdAt;

    public Crew(Long id, String name, String description, String crewImage, int maxMemberCount, Long userId, List<String> platformURL, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.crewImage = crewImage;
//        this.category = category;
        this.maxMemberCount = maxMemberCount;
        this.userId = userId;
        this.platformURL = platformURL;
        this.createdAt = createdAt;
    }
}
