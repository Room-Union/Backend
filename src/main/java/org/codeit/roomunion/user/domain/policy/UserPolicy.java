package org.codeit.roomunion.user.domain.policy;

import org.codeit.roomunion.common.exception.CustomException;
import org.codeit.roomunion.common.exception.GlobalErrorCode;
import org.codeit.roomunion.meeting.domain.model.MeetingCategory;
import org.codeit.roomunion.user.domain.command.UserCreateCommand;
import org.codeit.roomunion.user.domain.command.UserModifyCommand;
import org.codeit.roomunion.user.domain.exception.UserErrorCode;

import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

public class UserPolicy {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^*()_+=\\-~]).{8,13}$");
    private static final Pattern NICKNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9가-힣 ]{2,16}$");
    private static final int REQUIRED_CATEGORY_COUNT = 2;

    private UserPolicy() {
    }

    public static void validate(UserCreateCommand userCreateCommand) {
        validateNonNull(userCreateCommand.getEmail(), userCreateCommand.getNickname(), userCreateCommand.getPassword(), userCreateCommand.getGender(), userCreateCommand.getCategories());

        if (isNotValidateEmail(userCreateCommand.getEmail())) {
            throw new CustomException(GlobalErrorCode.INVALID_INPUT_VALUE);
        }
        if (isNotValidateNickname(userCreateCommand.getNickname())) {
            throw new CustomException(GlobalErrorCode.INVALID_INPUT_VALUE);
        }
        if (isNotValidatePassword(userCreateCommand.getPassword())) {
            throw new CustomException(GlobalErrorCode.INVALID_INPUT_VALUE);
        }
        if (isNotRequiredCategorySize(userCreateCommand.getCategories())) {
            throw new CustomException(GlobalErrorCode.INVALID_INPUT_VALUE);
        }
    }

    public static void validate(UserModifyCommand userModifyCommand) {
        validateNonNull(userModifyCommand.getGender(), userModifyCommand.getCategories());

        if (isNotValidateNickname(userModifyCommand.getNickname())) {
            throw new CustomException(GlobalErrorCode.INVALID_INPUT_VALUE);
        }
        if (isNotRequiredCategorySize(userModifyCommand.getCategories())) {
            throw new CustomException(GlobalErrorCode.INVALID_INPUT_VALUE);
        }
    }

    public static void validateNewPassword(String password, String newPassword) {
        validateNonNull(password, newPassword);
        if (Objects.equals(password, newPassword)) {
            throw new CustomException(UserErrorCode.SAME_PASSWORD);
        }
        if (isNotValidatePassword(newPassword)) {
            throw new CustomException(GlobalErrorCode.INVALID_INPUT_VALUE);
        }
    }

    private static void validateNonNull(Object... objects) {
        for (Object o : objects) {
            validateIsNull(o);
        }
    }

    private static void validateIsNull(Object object) {
        if (object == null) {
            throw new CustomException(GlobalErrorCode.INVALID_INPUT_VALUE);
        }
    }

    private static boolean isNotValidateNickname(String nickname) {
        return !NICKNAME_PATTERN.matcher(nickname)
            .matches();
    }

    private static boolean isNotValidatePassword(String password) {
        return !PASSWORD_PATTERN.matcher(password)
            .matches();
    }

    private static boolean isNotValidateEmail(String email) {
        return !EMAIL_PATTERN.matcher(email)
            .matches();
    }

    private static boolean isNotRequiredCategorySize(Set<MeetingCategory> categories) {
        return categories.size() != REQUIRED_CATEGORY_COUNT;
    }
}
