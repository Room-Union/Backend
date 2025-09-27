package org.codeit.roomunion.user.application.port.out;

import org.codeit.roomunion.user.domain.command.UserCreateCommand;
import org.codeit.roomunion.user.domain.model.User;

import java.util.Optional;

public interface UserRepository {

    User create(UserCreateCommand userCreateCommand, String nickname);

    Optional<User> findByEmail(String email);

    Optional<User> findByNickname(String nickname);
}
