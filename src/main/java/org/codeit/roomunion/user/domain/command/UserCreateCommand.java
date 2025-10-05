package org.codeit.roomunion.user.domain.command;

import lombok.Getter;
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

@Getter
public class UserCreateCommand {
    private final String email;
    private final String password;
    private final String nickname;
    private final Gender gender;
<<<<<<< HEAD
<<<<<<< HEAD
    private final Set<MeetingCategory> categories;

    private UserCreateCommand(String email, String password, String nickname, Gender gender, Set<MeetingCategory> categories) {
=======
    private final Set<Category> categories;

    private UserCreateCommand(String email, String password, String nickname, Gender gender, Set<Category> categories) {
>>>>>>> 98b72bc (feat: 회원가입, 로그인 구현 (#6))
=======
    private final Set<MeetingCategory> categories;

    private UserCreateCommand(String email, String password, String nickname, Gender gender, Set<MeetingCategory> categories) {
>>>>>>> f2440ea (:sparkles: 전역 예외 처리 및 모임 생성 기능, 특정 모임 조회 기능 구현 (#9))
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.gender = gender;
        this.categories = categories;
    }

<<<<<<< HEAD
<<<<<<< HEAD
    public static UserCreateCommand of(String email, String password, String nickname, Gender gender, Set<MeetingCategory> categories) {
=======
    public static UserCreateCommand of(String email, String password, String nickname, Gender gender, Set<Category> categories) {
>>>>>>> 98b72bc (feat: 회원가입, 로그인 구현 (#6))
=======
    public static UserCreateCommand of(String email, String password, String nickname, Gender gender, Set<MeetingCategory> categories) {
>>>>>>> f2440ea (:sparkles: 전역 예외 처리 및 모임 생성 기능, 특정 모임 조회 기능 구현 (#9))
        return new UserCreateCommand(email, password, nickname, gender, categories);
    }

    public UserCreateCommand replaceEncodePassword(String encodedPassword) {
        return of(email, encodedPassword, nickname, gender, categories);
    }
}
