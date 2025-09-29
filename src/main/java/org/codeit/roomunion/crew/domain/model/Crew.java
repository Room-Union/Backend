package org.codeit.roomunion.crew.domain.model;

import lombok.Getter;
<<<<<<< HEAD

import java.time.LocalDateTime;
import java.util.List;

=======
import org.codeit.roomunion.common.adapter.out.persistence.entity.UuidEntity;
import org.codeit.roomunion.common.config.S3.S3Properties;
>>>>>>> 5f18479 (:sparkles: S3 설정 및 이미지 업로드 기능 구현 (#5))

@Getter
public class Crew {
    private Long id;

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

    public String crewImageKey(S3Properties s3Properties, UuidEntity uuidEntity) {
        return s3Properties.getPath().getCrew() + '/' + uuidEntity.getUuid();
    }

>>>>>>> 5f18479 (:sparkles: S3 설정 및 이미지 업로드 기능 구현 (#5))
}
