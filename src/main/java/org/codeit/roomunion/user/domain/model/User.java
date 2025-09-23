package org.codeit.roomunion.user.domain.model;

import lombok.Getter;

@Getter
public class User {
    private final Long id;
    private final String email;
    private final String password;
<<<<<<< HEAD
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
=======
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
>>>>>>> 0efc476 (Feature/member (#2))
    }
}
