package org.codeit.roomunion.user.domain.policy;

import org.codeit.roomunion.moim.domain.model.Category;
import org.codeit.roomunion.user.domain.command.UserCreateCommand;

import java.util.List;
import java.util.regex.Pattern;

public class UserPolicy {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,}$");
    private static final int MINIMUM_CATEGORY_SIZE = 2;

    public void validate(UserCreateCommand userCreateCommand) { // TODO 예외 변경
        if (!EMAIL_PATTERN.matcher(userCreateCommand.getEmail()).matches()) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (!PASSWORD_PATTERN.matcher(userCreateCommand.getPassword()).matches()) {
            throw new IllegalArgumentException("Password must be at least 8 characters long, contain at least one uppercase letter, one lowercase letter, and one special character");
        }
        if (hasNotMinimumCategories(userCreateCommand.getCategories())) {
            throw new IllegalArgumentException("At least two categories must be selected");
        }
    }

    private boolean hasNotMinimumCategories(List<Category> categories) {
        return categories.size() < MINIMUM_CATEGORY_SIZE;
    }
}
