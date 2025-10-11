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
    }
}
