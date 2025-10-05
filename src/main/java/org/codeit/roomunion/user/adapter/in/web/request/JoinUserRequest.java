package org.codeit.roomunion.user.adapter.in.web.request;

<<<<<<< HEAD
<<<<<<< HEAD
import org.codeit.roomunion.meeting.domain.model.enums.MeetingCategory;
=======
import org.codeit.roomunion.moim.domain.model.Category;
>>>>>>> 98b72bc (feat: 회원가입, 로그인 구현 (#6))
=======
import org.codeit.roomunion.meeting.domain.model.enums.MeetingCategory;
>>>>>>> f2440ea (:sparkles: 전역 예외 처리 및 모임 생성 기능, 특정 모임 조회 기능 구현 (#9))
import org.codeit.roomunion.user.domain.command.UserCreateCommand;
import org.codeit.roomunion.user.domain.model.Gender;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public record JoinUserRequest(String email, String password, String nickname, Gender gender, String categories) {

    public UserCreateCommand toCommand() {
        return UserCreateCommand.of(email, password, nickname, gender, parseCategories(categories));
    }

<<<<<<< HEAD
<<<<<<< HEAD
    private Set<MeetingCategory> parseCategories(String categories) {
        return Arrays.stream(categories.split(","))
            .map(MeetingCategory::valueOf)
            .collect(Collectors.toUnmodifiableSet());
=======
    private Set<Category> parseCategories(String categories) {
        return Arrays.stream(categories.split(","))
                .map(Category::valueOf)
                .collect(Collectors.toUnmodifiableSet());
>>>>>>> 98b72bc (feat: 회원가입, 로그인 구현 (#6))
=======
    private Set<MeetingCategory> parseCategories(String categories) {
        return Arrays.stream(categories.split(","))
            .map(MeetingCategory::valueOf)
            .collect(Collectors.toUnmodifiableSet());
>>>>>>> f2440ea (:sparkles: 전역 예외 처리 및 모임 생성 기능, 특정 모임 조회 기능 구현 (#9))
    }
}
