package org.codeit.roomunion.user.adapter.out.persistence;

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
=======
import org.codeit.roomunion.common.exception.CustomException;
import org.codeit.roomunion.user.adapter.out.persistence.entity.EmailVerificationEntity;
>>>>>>> 351834c (feat: 회원가입 이메일 검증 로직 개발 (이메일 코드 발송, 이메일 코드 연장, 이메일 코드 검증) (#11))
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

<<<<<<< HEAD
=======
import org.codeit.roomunion.common.exception.UserNotFoundException;
=======
>>>>>>> 98b72bc (feat: 회원가입, 로그인 구현 (#6))
import org.codeit.roomunion.user.adapter.out.persistence.entity.UserEntity;
import org.codeit.roomunion.user.adapter.out.persistence.jpa.UserJpaRepository;
import org.codeit.roomunion.user.application.port.out.UserRepository;
import org.codeit.roomunion.user.domain.command.UserCreateCommand;
import org.codeit.roomunion.user.domain.model.User;
import org.springframework.stereotype.Repository;

<<<<<<< HEAD
>>>>>>> 8abfdd5 (feat: 스프링 시큐리티 개발 (#3))
=======
import java.util.Optional;

>>>>>>> 98b72bc (feat: 회원가입, 로그인 구현 (#6))
=======
import static org.codeit.roomunion.user.domain.exception.UserErrorCode.*;

>>>>>>> 351834c (feat: 회원가입 이메일 검증 로직 개발 (이메일 코드 발송, 이메일 코드 연장, 이메일 코드 검증) (#11))
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
<<<<<<< HEAD
<<<<<<< HEAD
    public User create(UserCreateCommand userCreateCommand) {
        UserEntity userEntity = UserEntity.of(userCreateCommand);
        return userJpaRepository.save(userEntity)
            .toDomain();
=======
    public User create(UserCreateCommand userCreateCommand) {
        UserEntity userEntity = UserEntity.of(userCreateCommand);
        return userJpaRepository.save(userEntity)
<<<<<<< HEAD
                .toDomain();
>>>>>>> 98b72bc (feat: 회원가입, 로그인 구현 (#6))
=======
            .toDomain();
>>>>>>> f2440ea (:sparkles: 전역 예외 처리 및 모임 생성 기능, 특정 모임 조회 기능 구현 (#9))
    }

    @Override
    public Optional<User> findByEmail(String email) {
<<<<<<< HEAD
        return userJpaRepository.findByEmail(email)
            .map(UserEntity::toDomain);
    }

    @Override
    public Optional<User> findByNickname(String nickname) {
        return userJpaRepository.findByNickname(nickname)
            .map(UserEntity::toDomain);
=======
    public User getByEmail(String email) {
        return userJpaRepository.findByEmail(email)
                .map(UserEntity::toDomain)
                .orElseThrow(UserNotFoundException::new);
>>>>>>> 8abfdd5 (feat: 스프링 시큐리티 개발 (#3))
=======
        return userJpaRepository.findByEmail(email)
            .map(UserEntity::toDomain);
    }

    @Override
    public Optional<User> findByNickname(String nickname) {
        return userJpaRepository.findByNickname(nickname)
            .map(UserEntity::toDomain);
>>>>>>> 98b72bc (feat: 회원가입, 로그인 구현 (#6))
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
    public void update(User user, UserModifyCommand userModifyCommand, boolean isUpdateImage) {
        UserEntity userEntity = findByWithCategories(user)
            .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        userEntity.clearCategories();
        userJpaRepository.flush();
        userEntity.update(userModifyCommand, isUpdateImage);
    }

    @Override
    public User getByWithCategories(User user) {
        UserEntity userEntity = findByWithCategories(user)
            .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        String imageUrl = imageFactory.createProfileImagePath(userEntity);
        return userEntity.toDomainWithCategories(imageUrl);
    }

    @Override
    public void updatePassword(User user, String encodedPassword) {
        UserEntity userEntity = userJpaRepository.findById(user.getId())
            .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        userEntity.updatePassword(encodedPassword);
    }

    private Optional<UserEntity> findByWithCategories(User user) {
        return userJpaRepository.findByIdWithCategories(user.getId());
    }

}
