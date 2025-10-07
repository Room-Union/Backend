package org.codeit.roomunion.user.domain.policy;

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
=======
import org.codeit.roomunion.common.exception.CustomException;
import org.codeit.roomunion.common.exception.GlobalErrorCode;
>>>>>>> 351834c (feat: 회원가입 이메일 검증 로직 개발 (이메일 코드 발송, 이메일 코드 연장, 이메일 코드 검증) (#11))
import org.codeit.roomunion.meeting.domain.model.enums.MeetingCategory;
=======
import org.codeit.roomunion.moim.domain.model.Category;
>>>>>>> 98b72bc (feat: 회원가입, 로그인 구현 (#6))
=======
import org.codeit.roomunion.meeting.domain.model.enums.MeetingCategory;
>>>>>>> f2440ea (:sparkles: 전역 예외 처리 및 모임 생성 기능, 특정 모임 조회 기능 구현 (#9))
import org.codeit.roomunion.user.domain.command.UserCreateCommand;

import java.util.Set;
import java.util.regex.Pattern;

public class UserPolicy {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
<<<<<<< HEAD
<<<<<<< HEAD
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^*()_+=\\-~]).{8,13}$");
=======
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,}$");
>>>>>>> 98b72bc (feat: 회원가입, 로그인 구현 (#6))
=======
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^*()_+=\\-~]).{8,13}$");
>>>>>>> 521de11 (refactor: 회원가입시 프로필 이미지 제거, 비밀번호 유효성체크 수정, 기타 수정 (#8))
    private static final int REQUIRED_CATEGORY_COUNT = 2;

    private UserPolicy() {
    }

    public static void validate(UserCreateCommand userCreateCommand) {
        if (isNotValidateEmail(userCreateCommand)) {
            throw new CustomException(GlobalErrorCode.INVALID_INPUT_VALUE);
        }
        if (isNotValidatePassword(userCreateCommand)) {
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
            throw new IllegalArgumentException("Password must be 8-13 characters long and contain letters, numbers, and special characters (!, @, #, $, %, ^, *, (, ), _, +, =, -, ~)");
=======
            throw new IllegalArgumentException("Password must be at least 8 characters long, contain at least one uppercase letter, one lowercase letter, and one special character");
>>>>>>> 98b72bc (feat: 회원가입, 로그인 구현 (#6))
=======
            throw new IllegalArgumentException("Password must be 8-13 characters long and contain letters, numbers, and special characters (!, @, #, $, %, ^, *, (, ), _, +, =, -, ~)");
>>>>>>> 521de11 (refactor: 회원가입시 프로필 이미지 제거, 비밀번호 유효성체크 수정, 기타 수정 (#8))
=======
            throw new CustomException(GlobalErrorCode.INVALID_INPUT_VALUE);
>>>>>>> 351834c (feat: 회원가입 이메일 검증 로직 개발 (이메일 코드 발송, 이메일 코드 연장, 이메일 코드 검증) (#11))
        }
        if (isNotRequiredCategorySize(userCreateCommand.getCategories())) {
            throw new CustomException(GlobalErrorCode.INVALID_INPUT_VALUE);
        }
    }

    private static boolean isNotValidatePassword(UserCreateCommand userCreateCommand) {
        return !PASSWORD_PATTERN.matcher(userCreateCommand.getPassword())
            .matches();
    }

    private static boolean isNotValidateEmail(UserCreateCommand userCreateCommand) {
        return !EMAIL_PATTERN.matcher(userCreateCommand.getEmail())
            .matches();
    }

<<<<<<< HEAD
<<<<<<< HEAD
    private static boolean isNotRequiredCategorySize(Set<MeetingCategory> categories) {
=======
    private static boolean isNotRequiredCategorySize(Set<Category> categories) {
>>>>>>> 98b72bc (feat: 회원가입, 로그인 구현 (#6))
=======
    private static boolean isNotRequiredCategorySize(Set<MeetingCategory> categories) {
>>>>>>> f2440ea (:sparkles: 전역 예외 처리 및 모임 생성 기능, 특정 모임 조회 기능 구현 (#9))
        if (categories == null || categories.isEmpty()) {
            return true;
        }
        return categories.size() != REQUIRED_CATEGORY_COUNT;
    }
}
