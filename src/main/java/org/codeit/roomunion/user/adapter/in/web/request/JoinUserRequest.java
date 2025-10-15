package org.codeit.roomunion.user.adapter.in.web.request;

import org.codeit.roomunion.meeting.domain.model.enums.MeetingCategory;
import org.codeit.roomunion.user.domain.command.UserCreateCommand;
import org.codeit.roomunion.user.domain.model.Gender;

import java.util.Set;

public record JoinUserRequest(String email, String password, String nickname, Gender gender, Set<MeetingCategory> categories) {

    public UserCreateCommand toCommand() {
        return UserCreateCommand.of(email, password, nickname, gender, categories);
    }

}
