package org.codeit.roomunion.auth.application.port.in;

public interface AuthUseCase {

    void sendVerificationCode(String email);

    void verifyCode(String email, String code);

    void extendExpiration(String email);
}
