package org.codeit.roomunion.user.domain.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import org.codeit.roomunion.meeting.domain.model.enums.MeetingCategory;

import java.util.Set;

@Getter
public class User {

    public static final String PROFILE_IMAGE_PATH = "user/%s/profile";

    private final Long id;
    private final String email;
    private final String password;
<<<<<<< HEAD
<<<<<<< HEAD
    private final String nickname;
    private final Gender gender;
    private final Set<MeetingCategory> categories;
    private final String profileImageUrl;

    @Builder(access = AccessLevel.PRIVATE)
    private User(Long id, String email, String password, String nickname, Gender gender, Set<MeetingCategory> categories, String profileImageUrl) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.gender = gender;
        this.categories = categories;
        this.profileImageUrl = profileImageUrl;
    }

    public static User of(Long id, String email, String password, String nickname, Gender gender, Set<MeetingCategory> categories, String profileImageUrl) {
        return new User(id, email, password, nickname, gender, categories, profileImageUrl);
    }

    public static User of(Long id, String email, String password, String nickname, Gender gender) {
<<<<<<< HEAD
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
=======
        return User.builder()
            .id(id)
            .email(email)
            .password(password)
            .nickname(nickname)
            .gender(gender)
            .build();
    }

    public String getProfileImagePath() {
        return PROFILE_IMAGE_PATH.formatted(id);
>>>>>>> 16456c7 (feat: 유저 수정 API, 유저 정보 조회 API, 비밀번호 변경 API 개발 (#12))
    }
}
