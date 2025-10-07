package org.codeit.roomunion.user.application.port.in;

import org.codeit.roomunion.user.domain.command.UserCreateCommand;
import org.codeit.roomunion.user.domain.model.User;

import java.time.LocalDateTime;

public interface UserCommandUseCase {

    User join(UserCreateCommand userCreateCommand);

    void saveEmailVerificationCode(String email, String code, LocalDateTime currentAt, LocalDateTime expirationAt);

    void validateEmailNotVerified(String email, LocalDateTime expirationAt);
}
