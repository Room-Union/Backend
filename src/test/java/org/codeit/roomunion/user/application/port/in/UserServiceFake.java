package org.codeit.roomunion.user.application.port.in;

import org.apache.commons.lang3.NotImplementedException;
import org.codeit.roomunion.user.domain.command.UserCreateCommand;
import org.codeit.roomunion.user.domain.model.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceFake implements UserQueryUseCase, UserCommandUseCase{

    @Override
    public User join(UserCreateCommand userCreateCommand) {
        throw new NotImplementedException();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public void validateEmailExists(String email) {

    }

}