package org.codeit.roomunion.auth.domain.event;

public record EmailVerificationCodeEvent(String email, String code, String subject, String body) {

    public static EmailVerificationCodeEvent of(String email, String code, String subject, String body) {
        return new EmailVerificationCodeEvent(email, code, subject, body);
    }
}