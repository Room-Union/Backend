package org.codeit.roomunion.user.domain.model;

import lombok.Getter;

@Getter
public class User {
    private final Long id;
    private final String email;
    private final String password;
    private final String nickname;
    private final Gender gender;
    private final String description;

    private User(Long id, String email, String password, String nickname, Gender gender, String description) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.gender = gender;
        this.description = description;
    }

    public static User of(Long id, String email, String password, String nickname, Gender gender, String description) {
        return new User(id, email, password, nickname, gender, description);
    }
}
