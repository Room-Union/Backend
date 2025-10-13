package org.codeit.roomunion.user.application.port.in;

import org.codeit.roomunion.common.exception.CustomException;
import org.codeit.roomunion.user.domain.command.UserCreateCommand;
import org.codeit.roomunion.user.domain.command.UserModifyCommand;
import org.codeit.roomunion.user.domain.exception.UserErrorCode;
import org.codeit.roomunion.user.domain.model.User;
import org.codeit.roomunion.user.domain.model.UserFixture;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FakeUserService implements UserQueryUseCase, UserCommandUseCase {

    private final List<User> users;

    public FakeUserService() {
        this.users = new ArrayList<>();
    }

    @Override
    public User join(UserCreateCommand userCreateCommand) {
        User user = UserFixture.create(1L, userCreateCommand.getEmail());
        users.add(user);
        return user;
    }

    @Override
    public void modify(User user, UserModifyCommand userModifyCommand, MultipartFile profileImage) {

    }

    @Override
    public void saveEmailVerificationCode(String email, String code, LocalDateTime currentAt, LocalDateTime expirationAt) {

    }

    @Override
    public void validateEmailNotVerified(String email, LocalDateTime expirationAt) {

    }

    @Override
    public Optional<User> findByEmail(String email) {
        return users.stream()
            .filter(user -> user.getEmail().equals(email))
            .findFirst();
    }

    @Override
    public void validateEmailExists(String email) {
        if (findByEmail(email).isPresent()) {
            throw new CustomException(UserErrorCode.ALREADY_REGISTERED_EMAIL);
        }
    }

    @Override
    public void verifyCode(String email, String code, LocalDateTime currentAt) {

    }

}