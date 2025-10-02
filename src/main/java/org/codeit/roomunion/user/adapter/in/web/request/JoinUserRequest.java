package org.codeit.roomunion.user.adapter.in.web.request;

import org.codeit.roomunion.crew.domain.model.enums.CrewCategory;
import org.codeit.roomunion.user.domain.command.UserCreateCommand;
import org.codeit.roomunion.user.domain.model.Gender;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public record JoinUserRequest(String email, String password, String nickname, Gender gender, String categories) {

    public UserCreateCommand toCommand() {
        return UserCreateCommand.of(email, password, nickname, gender, parseCategories(categories));
    }

    private Set<CrewCategory> parseCategories(String categories) {
        return Arrays.stream(categories.split(","))
            .map(CrewCategory::valueOf)
            .collect(Collectors.toUnmodifiableSet());
    }
}
