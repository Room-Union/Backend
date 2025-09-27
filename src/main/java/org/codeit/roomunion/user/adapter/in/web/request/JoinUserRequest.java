package org.codeit.roomunion.user.adapter.in.web.request;

import org.codeit.roomunion.moim.domain.model.Category;
import org.codeit.roomunion.user.domain.command.UserCreateCommand;
import org.codeit.roomunion.user.domain.model.Gender;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public record JoinUserRequest(String email, String password, Gender gender, String categories, String description) {

    public UserCreateCommand toCommand() {
        return UserCreateCommand.of(email, password, gender, parseCategories(categories), description);
    }

    private Set<Category> parseCategories(String categories) {
        return Arrays.stream(categories.split(","))
                .map(Category::valueOf)
                .collect(Collectors.toUnmodifiableSet());
    }
}
