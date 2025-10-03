package org.codeit.roomunion.user.application.port.in;

import org.codeit.roomunion.user.domain.model.User;

import java.util.Optional;

public interface UserQueryUseCase {

    User getByEmail(String email);

    Optional<User> findByEmail(String email);

    void validateEmailExists(String email);
}
