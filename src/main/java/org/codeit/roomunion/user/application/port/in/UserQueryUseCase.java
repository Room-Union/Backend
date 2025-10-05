package org.codeit.roomunion.user.application.port.in;

import org.codeit.roomunion.user.domain.model.User;

import java.util.Optional;

public interface UserQueryUseCase {

    Optional<User> findByEmail(String email);

    void validateEmailExists(String email);
}
