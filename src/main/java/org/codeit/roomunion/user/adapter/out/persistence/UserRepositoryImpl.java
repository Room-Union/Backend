package org.codeit.roomunion.user.adapter.out.persistence;

import org.codeit.roomunion.user.adapter.out.persistence.entity.EmailVerificationEntity;
import org.codeit.roomunion.user.adapter.out.persistence.entity.UserEntity;
import org.codeit.roomunion.user.adapter.out.persistence.jpa.EmailVarificationJpaRepository;
import org.codeit.roomunion.user.adapter.out.persistence.jpa.UserJpaRepository;
import org.codeit.roomunion.user.application.port.out.UserRepository;
import org.codeit.roomunion.user.domain.command.UserCreateCommand;
import org.codeit.roomunion.user.domain.model.User;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final EmailVarificationJpaRepository emailVarificationJpaRepository;

    public UserRepositoryImpl(UserJpaRepository userJpaRepository, EmailVarificationJpaRepository emailVarificationJpaRepository) {
        this.userJpaRepository = userJpaRepository;
        this.emailVarificationJpaRepository = emailVarificationJpaRepository;
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
        emailVarificationJpaRepository.save(emailVerificationEntity);
    }

    @Override
    public void verifyCode(String email, String code, LocalDateTime currentAt) {
        EmailVerificationEntity emailVerificationEntity = emailVarificationJpaRepository.findValidVerificationBy(email, currentAt)
            .orElseThrow(() -> new IllegalArgumentException("코드 시간 만료")); //TODO 예외 수정
        if (emailVerificationEntity.isCodeNotValid(code)) {
            throw new IllegalArgumentException("코드 불일치"); //TODO 예외 수정
        }
        emailVerificationEntity.verify();
    }

    @Override
    public void validateEmailNotVerified(String email, LocalDateTime expirationAt) {
        EmailVerificationEntity emailVerificationEntity = emailVarificationJpaRepository.findLatestVerificationByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("이메일 인증 내역 없음"));// TODO 예외 수정
        emailVerificationEntity.renewExpirationAt(expirationAt);
    }

}
