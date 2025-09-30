package org.codeit.roomunion.user.domain.command;

<<<<<<< HEAD
import org.codeit.roomunion.meeting.domain.model.enums.MeetingCategory;
=======
import org.codeit.roomunion.moim.domain.model.Category;
>>>>>>> 98b72bc (feat: 회원가입, 로그인 구현 (#6))
import org.codeit.roomunion.user.domain.model.Gender;

import java.util.Set;

public class UserCreateCommandFixture {

<<<<<<< HEAD
    public static UserCreateCommand create(String email, String password, String nickname, Gender gender, Set<MeetingCategory> categories) {
=======
    public static UserCreateCommand create(String email, String password, String nickname, Gender gender, Set<Category> categories) {
>>>>>>> 98b72bc (feat: 회원가입, 로그인 구현 (#6))
        return UserCreateCommand.of(email, password, nickname, gender, categories);
    }

}