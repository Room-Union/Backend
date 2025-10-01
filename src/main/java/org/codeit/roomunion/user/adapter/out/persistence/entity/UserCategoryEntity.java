package org.codeit.roomunion.user.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.codeit.roomunion.crew.domain.model.enums.CrewCategory;

@Getter
@Entity
@Table(
    name = "user_categories",
    indexes = @Index(name = "idx_user_id", columnList = "user_id"),
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "category"})
)
public class UserCategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private CrewCategory crewCategory;

    protected UserCategoryEntity() {
    }

    UserCategoryEntity(UserEntity user, CrewCategory crewCategory) {
        this.user = user;
        this.crewCategory = crewCategory;
    }

    static UserCategoryEntity of(UserEntity user, CrewCategory crewCategory) {
        return new UserCategoryEntity(user, crewCategory);
    }
}
