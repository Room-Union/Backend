package org.codeit.roomunion.user.adapter.out.persistence;

import org.codeit.roomunion.common.exception.CustomException;
import org.codeit.roomunion.user.adapter.out.persistence.entity.EmailVerificationEntity;
import org.codeit.roomunion.user.adapter.out.persistence.entity.UserEntity;
import org.codeit.roomunion.user.adapter.out.persistence.factory.ImageFactory;
import org.codeit.roomunion.user.adapter.out.persistence.jpa.EmailVerificationJpaRepository;
import org.codeit.roomunion.user.adapter.out.persistence.jpa.UserJpaRepository;
import org.codeit.roomunion.user.application.port.out.UserRepository;
import org.codeit.roomunion.user.domain.command.UserCreateCommand;
import org.codeit.roomunion.user.domain.command.UserModifyCommand;
import org.codeit.roomunion.user.domain.model.User;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.codeit.roomunion.user.domain.exception.UserErrorCode.*;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final EmailVerificationJpaRepository emailVerificationJpaRepository;
    private final ImageFactory imageFactory;

    public UserRepositoryImpl(UserJpaRepository userJpaRepository, EmailVerificationJpaRepository emailVerificationJpaRepository, ImageFactory imageFactory) {
        this.userJpaRepository = userJpaRepository;
        this.emailVerificationJpaRepository = emailVerificationJpaRepository;
        this.imageFactory = imageFactory;
    }

    @Override
    public User create(UserCreateCommand userCreateCommand) {
        UserEntity userEntity = UserEntity.of(userCreateCommand);
        return userJpaRepository.save(userEntity)
            .toDomain();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email)
            .map(UserEntity::toDomain);
    }

    @Override
    public Optional<User> findByNickname(String nickname) {
        return userJpaRepository.findByNickname(nickname)
            .map(UserEntity::toDomain);
    }

    @Override
    public void saveEmailVerificationCode(String email, String code, LocalDateTime currentAt, LocalDateTime expirationAt) {
        EmailVerificationEntity emailVerificationEntity = EmailVerificationEntity.of(email, code, currentAt, expirationAt);
        emailVerificationJpaRepository.save(emailVerificationEntity);
    }

    @Override
    public void verifyCode(String email, String code, LocalDateTime currentAt) {
        EmailVerificationEntity emailVerificationEntity = emailVerificationJpaRepository.findValidVerificationBy(email, currentAt)
            .orElseThrow(() -> new CustomException(EXPIRED_CODE));
        if (emailVerificationEntity.isCodeNotValid(code)) {
            throw new CustomException(INVALID_CODE);
        }
        emailVerificationEntity.verify();
    }

    @Override
    public void validateEmailNotVerified(String email, LocalDateTime expirationAt) {
        EmailVerificationEntity emailVerificationEntity = emailVerificationJpaRepository.findLatestVerificationByEmail(email)
            .orElseThrow(() -> new CustomException(EMAIL_VALIDATION_NOT_FOUND));
        if (emailVerificationEntity.isVerified()) {
            throw new CustomException(ALREADY_VERIFIED_EMAIL);
        }
        emailVerificationEntity.renewExpirationAt(expirationAt);
    }

    @Override
    public void modify(User user, UserModifyCommand userModifyCommand, boolean isUpdateImage) {
        UserEntity userEntity = findByWithCategories(user)
            .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        userEntity.clearCategories();
        userJpaRepository.flush();
        userEntity.modify(userModifyCommand, isUpdateImage);
    }

    @Override
    public User getByWithCategories(User user) {
        UserEntity userEntity = findByWithCategories(user)
            .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        String imageUrl = imageFactory.createProfileImagePath(userEntity);
        return userEntity.toDomainWithCategories(imageUrl);
    }

    private Optional<UserEntity> findByWithCategories(User user) {
        return userJpaRepository.findByIdWithCategories(user.getId());
    }

}
