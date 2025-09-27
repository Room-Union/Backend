package org.codeit.roomunion.user.domain.model;

import lombok.Getter;

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
}
