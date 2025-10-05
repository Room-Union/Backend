package org.codeit.roomunion.user.application.service;

import org.codeit.roomunion.auth.application.port.out.CustomPasswordEncoder;
import org.codeit.roomunion.common.exception.UserNotFoundException;
import org.codeit.roomunion.user.application.port.in.UserCommandUseCase;
import org.codeit.roomunion.user.application.port.in.UserQueryUseCase;
import org.codeit.roomunion.user.application.port.out.UserRepository;
import org.codeit.roomunion.user.domain.command.UserCreateCommand;
import org.codeit.roomunion.user.domain.model.User;
import org.codeit.roomunion.user.domain.policy.UserPolicy;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserQueryUseCase, UserCommandUseCase {

    private final UserRepository userRepository;
    private final CustomPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, CustomPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
        UserPolicy.validate(userCreateCommand);
        validateEmailAndNicknameExists(userCreateCommand);

        // TODO 등록 전 이메일 검증 완료되었는지 확인 필요

        String encodedPassword = passwordEncoder.encode(userCreateCommand.getPassword());
        return userRepository.create(userCreateCommand.replaceEncodePassword(encodedPassword));
    }

    private void validateEmailAndNicknameExists(UserCreateCommand userCreateCommand) {
        if (validateEmailExists(userCreateCommand.getEmail())) { // TODO 예외 처리 수정
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
        if (validateNicknameExists(userCreateCommand.getNickname())) {
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        }
    }

    private boolean validateNicknameExists(String nickname) {
        return userRepository.findByNickname(nickname).isPresent();
    }

    private boolean validateEmailExists(String email) {
        return findByEmail(email).isPresent();
    }

}
