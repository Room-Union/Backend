package org.codeit.roomunion.user.adapter.out.persistence.jpa;

import org.codeit.roomunion.user.adapter.out.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

<<<<<<< HEAD
<<<<<<< HEAD
    Optional<UserEntity> findByNickname(String nickname);

=======
>>>>>>> 5f18479 (:sparkles: S3 설정 및 이미지 업로드 기능 구현 (#5))
=======
    Optional<UserEntity> findByNickname(String nickname);

>>>>>>> 98b72bc (feat: 회원가입, 로그인 구현 (#6))
}
