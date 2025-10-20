package org.codeit.roomunion.common.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.codeit.roomunion.common.domain.model.Uuid;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "uuid")
public class UuidEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String uuid;

    public static UuidEntity from(Uuid uuid) {
        return UuidEntity.builder()
            .uuid(uuid.getValue())
            .build();
    }

    public Uuid toDomain() {
        return Uuid.from(this.uuid);
    }


}
