package org.codeit.roomunion.user.application.port.in;

import org.apache.commons.lang3.NotImplementedException;
import org.codeit.roomunion.user.domain.command.UserCreateCommand;
import org.codeit.roomunion.user.domain.model.User;

import java.time.LocalDateTime;
import java.util.Optional;

public class FakeUserService implements UserQueryUseCase, UserCommandUseCase{

    @Override
    public User join(UserCreateCommand userCreateCommand) {
        throw new NotImplementedException();
    }

    @Override
    public void saveEmailVerificationCode(String email, String code, LocalDateTime currentAt, LocalDateTime expirationAt) {

    }

    @Override
    public void validateEmailNotVerified(String email, LocalDateTime expirationAt) {

    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public void validateEmailExists(String email) {

    }

    @Override
    public void verifyCode(String email, String code, LocalDateTime currentAt) {

    }

}