package org.codeit.roomunion.user.domain.command;

<<<<<<< HEAD
<<<<<<< HEAD
import org.codeit.roomunion.meeting.domain.model.enums.MeetingCategory;
=======
import org.codeit.roomunion.moim.domain.model.Category;
>>>>>>> 98b72bc (feat: 회원가입, 로그인 구현 (#6))
=======
import org.codeit.roomunion.meeting.domain.model.enums.MeetingCategory;
>>>>>>> f2440ea (:sparkles: 전역 예외 처리 및 모임 생성 기능, 특정 모임 조회 기능 구현 (#9))
import org.codeit.roomunion.user.domain.model.Gender;

import java.util.Set;

public class UserCreateCommandFixture {

<<<<<<< HEAD
<<<<<<< HEAD
    public static UserCreateCommand create(String email, String password, String nickname, Gender gender, Set<MeetingCategory> categories) {
=======
    public static UserCreateCommand create(String email, String password, String nickname, Gender gender, Set<Category> categories) {
>>>>>>> 98b72bc (feat: 회원가입, 로그인 구현 (#6))
=======
    public static UserCreateCommand create(String email, String password, String nickname, Gender gender, Set<MeetingCategory> categories) {
>>>>>>> f2440ea (:sparkles: 전역 예외 처리 및 모임 생성 기능, 특정 모임 조회 기능 구현 (#9))
        return UserCreateCommand.of(email, password, nickname, gender, categories);
    }

    public static UserCreateCommand create(String email) {
        return create(email, "password", "nickname", Gender.MALE, Set.of());
    }

}