package org.codeit.roomunion.user.adapter.in.web.request;

import org.codeit.roomunion.moim.domain.model.enums.MoimCategory;
import org.codeit.roomunion.user.domain.command.UserCreateCommand;
import org.codeit.roomunion.user.domain.model.Gender;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public record JoinUserRequest(String email, String password, String nickname, Gender gender, String categories) {

    public UserCreateCommand toCommand() {
        return UserCreateCommand.of(email, password, nickname, gender, parseCategories(categories));
    }

    private Set<MoimCategory> parseCategories(String categories) {
        return Arrays.stream(categories.split(","))
            .map(MoimCategory::valueOf)
            .collect(Collectors.toUnmodifiableSet());
    }
}
