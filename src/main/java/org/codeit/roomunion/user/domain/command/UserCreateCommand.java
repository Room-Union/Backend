package org.codeit.roomunion.user.domain.command;

import lombok.Getter;
import org.codeit.roomunion.moim.domain.model.Category;
import org.codeit.roomunion.user.domain.model.Gender;

import java.util.Set;

@Getter
public class UserCreateCommand {
    private final String email;
    private final String password;
    private final Gender gender;
    private final Set<Category> categories;
    private final String description;

    private UserCreateCommand(String email, String password, Gender gender, Set<Category> categories, String description) {
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.categories = categories;
        this.description = description;
    }

    public static UserCreateCommand of(String email, String password, Gender gender, Set<Category> categories, String description) {
        return new UserCreateCommand(email, password, gender, categories, description);
    }
}
