package org.codeit.roomunion.user.adapter.out.persistence.jpa;

import org.codeit.roomunion.user.adapter.out.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

<<<<<<< HEAD
<<<<<<< HEAD
    Optional<UserEntity> findByNickname(String nickname);

<<<<<<< HEAD
=======
>>>>>>> 5f18479 (:sparkles: S3 설정 및 이미지 업로드 기능 구현 (#5))
=======
    Optional<UserEntity> findByNickname(String nickname);

>>>>>>> 98b72bc (feat: 회원가입, 로그인 구현 (#6))
=======
    @Query("SELECT u FROM UserEntity u LEFT JOIN FETCH u.userCategories WHERE u.id = :id")
    Optional<UserEntity> findByIdWithCategories(@Param("id") Long id);

>>>>>>> 16456c7 (feat: 유저 수정 API, 유저 정보 조회 API, 비밀번호 변경 API 개발 (#12))
}
