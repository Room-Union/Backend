package org.codeit.roomunion.user.domain.model;

import lombok.Getter;

@Getter
public class User {
    private final Long id;
    private final String email;
    private final String password;
<<<<<<< HEAD
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
=======
>>>>>>> 98b72bc (feat: 회원가입, 로그인 구현 (#6))
    private final String nickname;
    private final Gender gender;

    private User(Long id, String email, String password, String nickname, Gender gender) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.gender = gender;
    }

<<<<<<< HEAD
    public static User of(Long id, String email, String password, String name, String nickname, String description) {
        return new User(id, email, password, name, nickname, description);
>>>>>>> 0efc476 (Feature/member (#2))
    }

    public String profileImageKey(S3Properties s3Properties, UuidEntity uuidEntity) {
        return s3Properties.getPath().getProfile() + "/" + uuidEntity.getUuid();
=======
    public static User of(Long id, String email, String password, String nickname, Gender gender) {
        return new User(id, email, password, nickname, gender);
>>>>>>> 98b72bc (feat: 회원가입, 로그인 구현 (#6))
    }
}
