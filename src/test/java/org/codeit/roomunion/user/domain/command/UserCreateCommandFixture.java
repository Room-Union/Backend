package org.codeit.roomunion.user.domain.command;

import org.codeit.roomunion.moim.domain.model.Category;
import org.codeit.roomunion.user.domain.model.Gender;

import java.util.Set;

public class UserCreateCommandFixture {

    public static UserCreateCommand create(String email, String password, Gender gender, Set<Category> categories, String description) {
        return UserCreateCommand.of(email, password, gender, categories, description);
    }

}