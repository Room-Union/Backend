package org.codeit.roomunion.common.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
<<<<<<< HEAD
<<<<<<< HEAD
import org.codeit.roomunion.common.domain.model.Uuid;
=======
>>>>>>> 5f18479 (:sparkles: S3 설정 및 이미지 업로드 기능 구현 (#5))
=======
import org.codeit.roomunion.common.domain.model.Uuid;
>>>>>>> f2440ea (:sparkles: 전역 예외 처리 및 모임 생성 기능, 특정 모임 조회 기능 구현 (#9))

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UuidEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String uuid;
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> f2440ea (:sparkles: 전역 예외 처리 및 모임 생성 기능, 특정 모임 조회 기능 구현 (#9))

    public static UuidEntity from(Uuid uuid) {
        return UuidEntity.builder()
            .uuid(uuid.getValue())
            .build();
    }

    public Uuid toDomain() {
        return Uuid.of(this.uuid);
    }


<<<<<<< HEAD
=======
>>>>>>> 5f18479 (:sparkles: S3 설정 및 이미지 업로드 기능 구현 (#5))
=======
>>>>>>> f2440ea (:sparkles: 전역 예외 처리 및 모임 생성 기능, 특정 모임 조회 기능 구현 (#9))
}
