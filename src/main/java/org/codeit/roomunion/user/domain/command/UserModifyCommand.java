package org.codeit.roomunion.user.domain.command;

import lombok.Getter;
import org.codeit.roomunion.meeting.domain.model.enums.MeetingCategory;
import org.codeit.roomunion.user.domain.model.Gender;

import java.util.Set;

@Getter
public class UserModifyCommand {

    private final String nickname;
    private final Gender gender;
    private final Set<MeetingCategory> categories;

    private UserModifyCommand(String nickname, Gender gender, Set<MeetingCategory> categories) {
        this.nickname = nickname;
        this.gender = gender;
        this.categories = categories;
    }

    public static UserModifyCommand of(String nickname, Gender gender, Set<MeetingCategory> categories) {
        return new UserModifyCommand(nickname, gender, categories);
    }

}
