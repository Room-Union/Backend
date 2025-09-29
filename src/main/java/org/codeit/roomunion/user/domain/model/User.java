package org.codeit.roomunion.user.domain.model;

import lombok.Getter;
import org.codeit.roomunion.common.adapter.out.persistence.entity.UuidEntity;
import org.codeit.roomunion.common.config.S3.S3Properties;

@Getter
public class User {
    private final Long id;
    private final String email;
    private final String password;
    private final String name;
    private final String nickname;
    private final String description;

    private User(Long id, String email, String password, String name, String nickname, String description) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.description = description;
    }

    public static User of(Long id, String email, String password, String name, String nickname, String description) {
        return new User(id, email, password, name, nickname, description);
    }
}
