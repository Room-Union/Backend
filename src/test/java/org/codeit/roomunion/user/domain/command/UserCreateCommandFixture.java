package org.codeit.roomunion.user.domain.command;

import org.codeit.roomunion.meeting.domain.model.MeetingCategory;
import org.codeit.roomunion.user.domain.model.Gender;

import java.util.Set;

public class UserCreateCommandFixture {

    public static UserCreateCommand create(String email, String password, String nickname, Gender gender, Set<MeetingCategory> categories) {
        return UserCreateCommand.of(email, password, nickname, gender, categories);
    }

    public static UserCreateCommand create(String email) {
        return create(email, "password", "nickname", Gender.MALE, Set.of());
    }

}