package org.codeit.roomunion.user.domain.model;

import lombok.Getter;
import org.codeit.roomunion.common.adapter.out.persistence.entity.UuidEntity;
import org.codeit.roomunion.common.config.S3.S3Properties;

@Getter
public class User {
    private final Long id;
    private final String email;
    private final String password;
    private final String nickname;
    private final Gender gender;

    private User(Long id, String email, String password, String nickname, Gender gender) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.gender = gender;
    }

    public static User of(Long id, String email, String password, String nickname, Gender gender) {
        return new User(id, email, password, nickname, gender);
    }

    public String profileImageKey(S3Properties s3Properties, UuidEntity uuidEntity) {
        return s3Properties.getPath().getProfile() + "/" + uuidEntity.getUuid();
    }
}
