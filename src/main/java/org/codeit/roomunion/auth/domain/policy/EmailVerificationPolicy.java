package org.codeit.roomunion.auth.domain.policy;

import org.codeit.roomunion.common.exception.CustomException;
import org.codeit.roomunion.common.exception.GlobalErrorCode;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

public class EmailVerificationPolicy {

    private static final Duration EXPIRATION_DURATION = Duration.ofMinutes(3);
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

    public static LocalDateTime calculateExpirationAt(LocalDateTime createdAt) {
        return createdAt.plus(EXPIRATION_DURATION);
    }

    public static void validateEmailFormat(String email) {
        validateIsNotNull(email);
        if (isNotValidateEmail(email)) {
            throw new CustomException(GlobalErrorCode.INVALID_INPUT_VALUE);
        }
    }

    private static boolean isNotValidateEmail(String email) {
        return !EMAIL_PATTERN.matcher(email)
            .matches();
    }

    private static void validateIsNotNull(Object object) {
        if (object == null) {
            throw new CustomException(GlobalErrorCode.INVALID_INPUT_VALUE);
        }
    }
}
