package org.codeit.roomunion.user.adapter.out.persistence;

<<<<<<< HEAD
<<<<<<< HEAD
import org.codeit.roomunion.user.adapter.out.persistence.entity.UserEntity;
import org.codeit.roomunion.user.adapter.out.persistence.jpa.UserJpaRepository;
import org.codeit.roomunion.user.application.port.out.UserRepository;
import org.codeit.roomunion.user.domain.command.UserCreateCommand;
import org.codeit.roomunion.user.domain.model.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

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
@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    public UserRepositoryImpl(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
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
                .toDomain();
>>>>>>> 98b72bc (feat: 회원가입, 로그인 구현 (#6))
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
}
