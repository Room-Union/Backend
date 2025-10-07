package org.codeit.roomunion.auth.domain.policy;

import java.time.Duration;
import java.time.LocalDateTime;

public class EmailVerificationPolicy {

    private static final Duration EXPIRATION_DURATION = Duration.ofMinutes(3);

    public static LocalDateTime calculateExpirationAt(LocalDateTime createdAt) {
        return createdAt.plus(EXPIRATION_DURATION);
    }
}
