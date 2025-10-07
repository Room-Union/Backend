package org.codeit.roomunion.user.domain.policy;

import org.codeit.roomunion.common.exception.CustomException;
import org.codeit.roomunion.common.exception.GlobalErrorCode;
import org.codeit.roomunion.meeting.domain.model.enums.MeetingCategory;
import org.codeit.roomunion.user.domain.command.UserCreateCommand;

import java.util.Set;
import java.util.regex.Pattern;

public class UserPolicy {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^*()_+=\\-~]).{8,13}$");
    private static final int REQUIRED_CATEGORY_COUNT = 2;

    private UserPolicy() {
    }

    public static void validate(UserCreateCommand userCreateCommand) {
        if (isNotValidateEmail(userCreateCommand)) {
            throw new CustomException(GlobalErrorCode.INVALID_INPUT_VALUE);
        }
        if (isNotValidatePassword(userCreateCommand)) {
            throw new CustomException(GlobalErrorCode.INVALID_INPUT_VALUE);
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

    private static boolean isNotRequiredCategorySize(Set<MeetingCategory> categories) {
        if (categories == null || categories.isEmpty()) {
            return true;
        }
        return categories.size() != REQUIRED_CATEGORY_COUNT;
    }
}
