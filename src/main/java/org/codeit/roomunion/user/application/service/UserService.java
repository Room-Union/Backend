package org.codeit.roomunion.user.application.service;

import org.codeit.roomunion.common.exception.UserNotFoundException;
import org.codeit.roomunion.user.application.port.in.UserCommandUseCase;
import org.codeit.roomunion.user.application.port.in.UserQueryUseCase;
import org.codeit.roomunion.user.application.port.out.NicknameGenerator;
import org.codeit.roomunion.user.application.port.out.UserRepository;
import org.codeit.roomunion.user.domain.command.UserCreateCommand;
import org.codeit.roomunion.user.domain.model.User;
import org.codeit.roomunion.user.domain.policy.UserPolicy;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserQueryUseCase, UserCommandUseCase {

    private final UserRepository userRepository;
    private final NicknameGenerator nicknameGenerator;

    public UserService(UserRepository userRepository, NicknameGenerator nicknameGenerator) {
        this.userRepository = userRepository;
        this.nicknameGenerator = nicknameGenerator;
    }

    @Override
    public User getByEmail(String email) {
        return findByEmail(email)
            .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User join(UserCreateCommand userCreateCommand) {
        UserPolicy policy = new UserPolicy();
        policy.validate(userCreateCommand);
        if (validateEmailExists(userCreateCommand.getEmail())) { // TODO 예외 처리 수정
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
        String nickname = generateNickname();

        return userRepository.create(userCreateCommand, nickname);
    }

    private String generateNickname() {
        String nickname = nicknameGenerator.generate();
        while (validateNicknameExists(nickname)) {
            nickname = nicknameGenerator.generate();
        }
        return nickname;
    }

    private boolean validateNicknameExists(String nickname) {
        return userRepository.findByNickname(nickname).isPresent();
    }

    private boolean validateEmailExists(String email) {
        return findByEmail(email).isPresent();
    }

}
