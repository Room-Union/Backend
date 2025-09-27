package org.codeit.roomunion.user.domain.command;

import org.codeit.roomunion.moim.domain.model.Category;
import org.codeit.roomunion.user.domain.model.Gender;

import java.util.Set;

public class UserCreateCommandFixture {

    public static UserCreateCommand create(String email, String password, String nickname, Gender gender, Set<Category> categories) {
        return UserCreateCommand.of(email, password, nickname, gender, categories);
    }

}