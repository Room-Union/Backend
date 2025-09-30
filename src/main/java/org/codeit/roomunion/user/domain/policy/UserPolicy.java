package org.codeit.roomunion.user.domain.policy;

<<<<<<< HEAD
import org.codeit.roomunion.meeting.domain.model.enums.MeetingCategory;
=======
import org.codeit.roomunion.moim.domain.model.Category;
>>>>>>> 98b72bc (feat: 회원가입, 로그인 구현 (#6))
import org.codeit.roomunion.user.domain.command.UserCreateCommand;

import java.util.Set;
import java.util.regex.Pattern;

public class UserPolicy {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
<<<<<<< HEAD
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^*()_+=\\-~]).{8,13}$");
=======
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,}$");
>>>>>>> 98b72bc (feat: 회원가입, 로그인 구현 (#6))
    private static final int REQUIRED_CATEGORY_COUNT = 2;

    private UserPolicy() {
    }

    public static void validate(UserCreateCommand userCreateCommand) { // TODO 현준님 예외 변경하면 여기도 바꾸기
        if (isNotValidateEmail(userCreateCommand)) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (isNotValidatePassword(userCreateCommand)) {
<<<<<<< HEAD
            throw new IllegalArgumentException("Password must be 8-13 characters long and contain letters, numbers, and special characters (!, @, #, $, %, ^, *, (, ), _, +, =, -, ~)");
=======
            throw new IllegalArgumentException("Password must be at least 8 characters long, contain at least one uppercase letter, one lowercase letter, and one special character");
>>>>>>> 98b72bc (feat: 회원가입, 로그인 구현 (#6))
        }
        if (isNotRequiredCategorySize(userCreateCommand.getCategories())) {
            throw new IllegalArgumentException("At least two categories must be selected");
        }
    }

    private static boolean isNotValidatePassword(UserCreateCommand userCreateCommand) {
        return !PASSWORD_PATTERN.matcher(userCreateCommand.getPassword()).matches();
    }

    private static boolean isNotValidateEmail(UserCreateCommand userCreateCommand) {
        return !EMAIL_PATTERN.matcher(userCreateCommand.getEmail()).matches();
    }

<<<<<<< HEAD
    private static boolean isNotRequiredCategorySize(Set<MeetingCategory> categories) {
=======
    private static boolean isNotRequiredCategorySize(Set<Category> categories) {
>>>>>>> 98b72bc (feat: 회원가입, 로그인 구현 (#6))
        if (categories == null || categories.isEmpty()) {
            return true;
        }
        return categories.size() != REQUIRED_CATEGORY_COUNT;
    }
}
