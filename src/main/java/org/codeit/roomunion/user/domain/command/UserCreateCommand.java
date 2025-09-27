package org.codeit.roomunion.user.domain.command;

import lombok.Getter;
import org.codeit.roomunion.auth.application.port.out.CustomPasswordEncoder;
import org.codeit.roomunion.moim.domain.model.Category;
import org.codeit.roomunion.user.domain.model.Gender;

import java.util.Set;

@Getter
public class UserCreateCommand {
    private final String email;
    private final String password;
    private final String nickname;
    private final Gender gender;
    private final Set<Category> categories;

    private UserCreateCommand(String email, String password, String nickname, Gender gender, Set<Category> categories) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.gender = gender;
        this.categories = categories;
    }

    public static UserCreateCommand of(String email, String password, String nickname, Gender gender, Set<Category> categories) {
        return new UserCreateCommand(email, password, nickname, gender, categories);
    }

    public UserCreateCommand encodePassword(CustomPasswordEncoder passwordEncoder) {
        return of(email, passwordEncoder.encode(password), nickname, gender, categories);
    }
}
