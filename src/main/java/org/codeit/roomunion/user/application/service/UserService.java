package org.codeit.roomunion.user.application.service;

import org.codeit.roomunion.common.application.port.out.EventPublisher;
import org.codeit.roomunion.common.exception.CustomException;
import org.codeit.roomunion.user.application.port.in.UserCommandUseCase;
import org.codeit.roomunion.user.application.port.in.UserQueryUseCase;
import org.codeit.roomunion.user.application.port.out.UserRepository;
import org.codeit.roomunion.user.domain.command.UserCreateCommand;
import org.codeit.roomunion.user.domain.command.UserModifyCommand;
import org.codeit.roomunion.user.domain.event.ProfileImageUploadEvent;
import org.codeit.roomunion.user.domain.exception.UserErrorCode;
import org.codeit.roomunion.user.domain.model.User;
import org.codeit.roomunion.user.domain.policy.UserPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserService implements UserQueryUseCase, UserCommandUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EventPublisher eventPublisher;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, EventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
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
    @Transactional
    public User join(UserCreateCommand userCreateCommand) {
        UserPolicy.validate(userCreateCommand);
        validateEmailAndNicknameExists(userCreateCommand);

        // TODO 등록 전 이메일 검증 완료되었는지 확인 필요

        String encodedPassword = passwordEncoder.encode(userCreateCommand.getPassword());
        return userRepository.create(userCreateCommand.replaceEncodePassword(encodedPassword));
    }

    @Override
    @Transactional
    public void modify(User user, UserModifyCommand userModifyCommand, MultipartFile profileImage) {
        UserPolicy.validate(userModifyCommand);
        validateNicknameExists(userModifyCommand.getNickname());
        userRepository.update(user, userModifyCommand, hasImage(profileImage));
        updateProfileImage(user, profileImage);
    }

    @Override
    @Transactional
    public void updatePassword(User user, String password, String newPassword) {
        validatePassword(user, password);
        UserPolicy.validateNewPassword(password, newPassword);
        String encodedPassword = passwordEncoder.encode(newPassword);
        userRepository.updatePassword(user, encodedPassword);
    }

    private void validatePassword(User user, String password) {
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomException(UserErrorCode.INVALID_PASSWORD);
        }
    }

    private boolean hasImage(MultipartFile profileImage) {
        return profileImage != null && !profileImage.isEmpty() && !Objects.equals(profileImage.getOriginalFilename(), "null");
    }

    private void updateProfileImage(User user, MultipartFile profileImage) {
        if (hasImage(profileImage)) {
            amazonS3Manager.uploadFile(user.getProfileImagePath(), profileImage);
        }
        ProfileImageUploadEvent profileImageUploadEvent = ProfileImageUploadEvent.of(user.getProfileImagePath(), profileImage);
        eventPublisher.publish(profileImageUploadEvent);
    }

    @Override
    public void saveEmailVerificationCode(String email, String code, LocalDateTime currentAt, LocalDateTime expirationAt) {
        userRepository.saveEmailVerificationCode(email, code, currentAt, expirationAt);
    }

    @Override
    public void validateEmailNotVerified(String email, LocalDateTime expirationAt) {
        userRepository.validateEmailNotVerified(email, expirationAt);
    }

    @Override
    public User getUserInfo(User user) {
        return userRepository.getByWithCategories(user);
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

}
