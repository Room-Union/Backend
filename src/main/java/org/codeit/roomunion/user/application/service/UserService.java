package org.codeit.roomunion.user.application.service;

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> f2440ea (:sparkles: 전역 예외 처리 및 모임 생성 기능, 특정 모임 조회 기능 구현 (#9))
import org.codeit.roomunion.auth.application.port.out.CustomPasswordEncoder;
import org.codeit.roomunion.common.exception.UserNotFoundException;
=======
import org.codeit.roomunion.common.exception.CustomException;
>>>>>>> 351834c (feat: 회원가입 이메일 검증 로직 개발 (이메일 코드 발송, 이메일 코드 연장, 이메일 코드 검증) (#11))
import org.codeit.roomunion.user.application.port.in.UserCommandUseCase;
import org.codeit.roomunion.user.application.port.in.UserQueryUseCase;
import org.codeit.roomunion.user.application.port.out.UserRepository;
import org.codeit.roomunion.user.domain.command.UserCreateCommand;
import org.codeit.roomunion.user.domain.exception.UserErrorCode;
import org.codeit.roomunion.user.domain.model.User;
import org.codeit.roomunion.user.domain.policy.UserPolicy;
<<<<<<< HEAD
<<<<<<< HEAD
=======
import org.springframework.security.crypto.password.PasswordEncoder;
>>>>>>> 351834c (feat: 회원가입 이메일 검증 로직 개발 (이메일 코드 발송, 이메일 코드 연장, 이메일 코드 검증) (#11))
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService implements UserQueryUseCase, UserCommandUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
=======
=======
import org.codeit.roomunion.common.exception.UserNotFoundException;
import org.codeit.roomunion.user.application.port.in.UserCommandUseCase;
>>>>>>> 98b72bc (feat: 회원가입, 로그인 구현 (#6))
import org.codeit.roomunion.user.application.port.in.UserQueryUseCase;
import org.codeit.roomunion.user.application.port.out.UserRepository;
import org.codeit.roomunion.user.domain.command.UserCreateCommand;
import org.codeit.roomunion.user.domain.model.User;
import org.codeit.roomunion.user.domain.policy.UserPolicy;
import org.codeit.roomunion.auth.application.port.out.CustomPasswordEncoder;
=======
>>>>>>> f2440ea (:sparkles: 전역 예외 처리 및 모임 생성 기능, 특정 모임 조회 기능 구현 (#9))
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserQueryUseCase, UserCommandUseCase {

    private final UserRepository userRepository;
    private final CustomPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, CustomPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
<<<<<<< HEAD
>>>>>>> 8abfdd5 (feat: 스프링 시큐리티 개발 (#3))
=======
        this.passwordEncoder = passwordEncoder;
>>>>>>> 98b72bc (feat: 회원가입, 로그인 구현 (#6))
    }

    @Override
<<<<<<< HEAD
    public User getByEmail(String email) {
<<<<<<< HEAD
<<<<<<< HEAD
        return findByEmail(email)
            .orElseThrow(UserNotFoundException::new);
=======
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
>>>>>>> 351834c (feat: 회원가입 이메일 검증 로직 개발 (이메일 코드 발송, 이메일 코드 연장, 이메일 코드 검증) (#11))
    }

    @Override
    public void validateEmailExists(String email) {
        if (findByEmail(email).isPresent()) {
            throw new CustomException(UserErrorCode.ALREADY_REGISTERED_EMAIL);
        }
    }

    @Override
    public void verifyCode(String email, String code, LocalDateTime currentAt) {
        userRepository.verifyCode(email, code, currentAt);
    }

    @Override
    public User join(UserCreateCommand userCreateCommand) {
        UserPolicy.validate(userCreateCommand);
        validateEmailAndNicknameExists(userCreateCommand);

        // TODO 등록 전 이메일 검증 완료되었는지 확인 필요

        String encodedPassword = passwordEncoder.encode(userCreateCommand.getPassword());
        return userRepository.create(userCreateCommand.replaceEncodePassword(encodedPassword));
    }

    @Override
    public void saveEmailVerificationCode(String email, String code, LocalDateTime currentAt, LocalDateTime expirationAt) {
        userRepository.saveEmailVerificationCode(email, code, currentAt, expirationAt);
    }

    @Override
    public void validateEmailNotVerified(String email, LocalDateTime expirationAt) {
        userRepository.validateEmailNotVerified(email, expirationAt);
    }

    private void validateEmailAndNicknameExists(UserCreateCommand userCreateCommand) {
        validateEmailExists(userCreateCommand.getEmail());
        validateNicknameExists(userCreateCommand.getNickname());
    }

    private void validateNicknameExists(String nickname) {
        if (userRepository.findByNickname(nickname).isPresent()) {
            throw new CustomException(UserErrorCode.ALREADY_REGISTERED_NICKNAME);
        }
    }

=======
        return userRepository.getByEmail(email);
    }
>>>>>>> 8abfdd5 (feat: 스프링 시큐리티 개발 (#3))
=======
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

>>>>>>> 98b72bc (feat: 회원가입, 로그인 구현 (#6))
}
