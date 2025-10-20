package org.codeit.roomunion.user.domain.command;

import lombok.Getter;
import org.codeit.roomunion.meeting.domain.model.MeetingCategory;
import org.codeit.roomunion.user.domain.model.Gender;

import java.util.Set;

@Getter
public class UserCreateCommand {
    private final String email;
    private final String password;
    private final String nickname;
    private final Gender gender;
    private final Set<MeetingCategory> categories;

    private UserCreateCommand(String email, String password, String nickname, Gender gender, Set<MeetingCategory> categories) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.gender = gender;
        this.categories = categories;
    }

    public static UserCreateCommand of(String email, String password, String nickname, Gender gender, Set<MeetingCategory> categories) {
        return new UserCreateCommand(email, password, nickname, gender, categories);
    }

    public UserCreateCommand replaceEncodePassword(String encodedPassword) {
        return of(email, encodedPassword, nickname, gender, categories);
    }
}
