package org.codeit.roomunion.common.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
<<<<<<< HEAD
import org.codeit.roomunion.common.domain.model.Uuid;
=======
>>>>>>> 5f18479 (:sparkles: S3 설정 및 이미지 업로드 기능 구현 (#5))

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

    public static UuidEntity from(Uuid uuid) {
        return UuidEntity.builder()
            .uuid(uuid.getValue())
            .build();
    }

    public Uuid toDomain() {
        return Uuid.of(this.uuid);
    }


=======
>>>>>>> 5f18479 (:sparkles: S3 설정 및 이미지 업로드 기능 구현 (#5))
}
