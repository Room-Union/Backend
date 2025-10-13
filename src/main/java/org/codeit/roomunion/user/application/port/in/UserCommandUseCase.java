package org.codeit.roomunion.user.application.port.in;

import org.codeit.roomunion.user.domain.command.UserCreateCommand;
import org.codeit.roomunion.user.domain.command.UserModifyCommand;
import org.codeit.roomunion.user.domain.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

public interface UserCommandUseCase {

    User join(UserCreateCommand userCreateCommand);

    void modify(User user, UserModifyCommand userModifyCommand, MultipartFile profileImage);

    void updatePassword(User user, String password, String newPassword);

    void saveEmailVerificationCode(String email, String code, LocalDateTime currentAt, LocalDateTime expirationAt);

    void validateEmailNotVerified(String email, LocalDateTime expirationAt);

    User getUserInfo(User user);
}
