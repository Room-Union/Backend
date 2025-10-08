package org.codeit.roomunion.user.application.port.out;

import org.codeit.roomunion.user.domain.command.UserCreateCommand;
import org.codeit.roomunion.user.domain.command.UserModifyCommand;
import org.codeit.roomunion.user.domain.model.User;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserRepository {

    User create(UserCreateCommand userCreateCommand);

    Optional<User> findByEmail(String email);

    Optional<User> findByNickname(String nickname);

    void saveEmailVerificationCode(String email, String code, LocalDateTime currentAt, LocalDateTime expirationAt);

    void verifyCode(String email, String code, LocalDateTime currentAt);

    void validateEmailNotVerified(String email, LocalDateTime expirationAt);

    User modify(User user, UserModifyCommand userModifyCommand, boolean isUpdateImage);
}
