package org.codeit.roomunion.user.adapter.in.web.request;

import org.codeit.roomunion.meeting.domain.model.enums.MeetingCategory;
import org.codeit.roomunion.user.domain.command.UserModifyCommand;
import org.codeit.roomunion.user.domain.model.Gender;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public record UserModifyRequest(String nickname, Gender gender, String categories) {

    public UserModifyCommand toCommand() {
        return UserModifyCommand.of(nickname, gender, parseCategories(categories));
    }

    private Set<MeetingCategory> parseCategories(String categories) {
        return Arrays.stream(categories.split(","))
            .map(MeetingCategory::valueOf)
            .collect(Collectors.toUnmodifiableSet());
    }
}
