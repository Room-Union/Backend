package org.codeit.roomunion.user.domain.command;

import lombok.Getter;
import org.codeit.roomunion.moim.domain.model.Category;
import org.codeit.roomunion.user.domain.model.Gender;

import java.util.List;

@Getter
public class UserCreateCommand {
    private final String email;
    private final String password;
    private final String nickname;
    private final Gender gender;
    private final List<Category> categories;
    private final String description;

    private UserCreateCommand(
            String email,
            String password,
            String nickname,
            Gender gender,
            List<Category> categories,
            String description
    ) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.gender = gender;
        this.categories = categories;
        this.description = description;
    }

    public static UserCreateCommand of(
            String email,
            String password,
            String nickname,
            Gender gender,
            List<Category> categories,
            String description
    ) {
        return new UserCreateCommand(email, password, nickname, gender, categories, description);
    }
}
