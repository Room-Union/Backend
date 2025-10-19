package org.codeit.roomunion.user.adapter.in.web.request;

import org.codeit.roomunion.meeting.domain.model.MeetingCategory;
import org.codeit.roomunion.user.domain.command.UserModifyCommand;
import org.codeit.roomunion.user.domain.model.Gender;

import java.util.Set;

public record UserModifyRequest(String nickname, Gender gender, Set<MeetingCategory> categories) {

    public UserModifyCommand toCommand() {
        return UserModifyCommand.of(nickname, gender, categories);
    }

}
