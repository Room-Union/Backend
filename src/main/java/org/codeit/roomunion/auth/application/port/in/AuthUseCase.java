package org.codeit.roomunion.auth.application.port.in;

import org.codeit.roomunion.auth.domain.model.LoginResult;
import org.codeit.roomunion.auth.domain.model.RefreshResult;

public interface AuthUseCase {

    LoginResult login(String email, String password);

    RefreshResult refresh(String refreshToken);

    void sendVerificationCode(String email);

    void verifyCode(String email, String code);

    void extendExpiration(String email);
}
