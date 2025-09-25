package org.codeit.roomunion.user.adapter.out.persistence;

import org.codeit.roomunion.common.exception.UserNotFoundException;
import org.codeit.roomunion.user.adapter.out.persistence.entity.UserEntity;
import org.codeit.roomunion.user.adapter.out.persistence.jpa.UserJpaRepository;
import org.codeit.roomunion.user.application.port.out.UserRepository;
import org.codeit.roomunion.user.domain.command.UserCreateCommand;
import org.codeit.roomunion.user.domain.model.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    public UserRepositoryImpl(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public User getByEmail(String email) {
        return userJpaRepository.findByEmail(email)
                .map(UserEntity::toDomain)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public User create(UserCreateCommand userCreateCommand) {
        UserEntity userEntity = UserEntity.from(userCreateCommand);
        return userJpaRepository.save(userEntity)
                .toDomain();
    }
}
