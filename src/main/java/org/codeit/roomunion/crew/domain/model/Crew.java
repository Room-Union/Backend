package org.codeit.roomunion.crew.domain.model;

import lombok.Getter;
<<<<<<< HEAD

import java.time.LocalDateTime;
import java.util.List;

=======
import org.codeit.roomunion.common.adapter.out.persistence.entity.UuidEntity;
<<<<<<< HEAD
import org.codeit.roomunion.common.config.S3.S3Properties;
>>>>>>> 5f18479 (:sparkles: S3 설정 및 이미지 업로드 기능 구현 (#5))
=======

import java.time.LocalDateTime;
import java.util.List;

>>>>>>> c9ee85d (:recycle: AmazonS3Manager에 S3 Path 복구 (#7))

@Getter
public class Crew {
    private Long id;

<<<<<<< HEAD
<<<<<<< HEAD
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
=======
=======
    private String name;
>>>>>>> c9ee85d (:recycle: AmazonS3Manager에 S3 Path 복구 (#7))

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

>>>>>>> 5f18479 (:sparkles: S3 설정 및 이미지 업로드 기능 구현 (#5))
}
