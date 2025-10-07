package org.codeit.roomunion.user.adapter.out.persistence.jpa;

import org.codeit.roomunion.user.adapter.out.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByNickname(String nickname);

    @Query("SELECT u FROM UserEntity u LEFT JOIN FETCH u.userCategories WHERE u.id = :id")
    Optional<UserEntity> findByIdWithCategories(@Param Long id);

}
