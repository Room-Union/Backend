package org.codeit.roomunion.user.adapter.in.web.response;

import org.codeit.roomunion.meeting.domain.model.enums.MeetingCategory;
import org.codeit.roomunion.user.domain.model.Gender;
import org.codeit.roomunion.user.domain.model.User;

import java.util.Set;

public record UserInfoResponse(String email, String nickname, Gender gender, Set<MeetingCategory> categories, String profileImageUrl) {
    public static UserInfoResponse from(User user) {
        return new UserInfoResponse(
                user.getEmail(),
                user.getNickname(),
                user.getGender(),
                user.getCategories(),
                user.getProfileImageUrl()
        );
    }
}
