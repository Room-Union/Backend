package org.codeit.roomunion.crew.domain.model;

import lombok.Getter;
import org.codeit.roomunion.common.adapter.out.persistence.entity.UuidEntity;
import org.codeit.roomunion.common.config.S3.S3Properties;

@Getter
public class Crew {
    private Long id;


    public String crewImageKey(S3Properties s3Properties, UuidEntity uuidEntity) {
        return s3Properties.getPath().getCrew() + '/' + uuidEntity.getUuid();
    }

}
