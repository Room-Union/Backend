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
}
