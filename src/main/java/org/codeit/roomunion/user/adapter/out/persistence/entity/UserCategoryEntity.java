package org.codeit.roomunion.user.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.codeit.roomunion.moim.domain.model.enums.MoimCategory;

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
    private MoimCategory moimCategory;

    protected UserCategoryEntity() {
    }

    UserCategoryEntity(UserEntity user, MoimCategory moimCategory) {
        this.user = user;
        this.moimCategory = moimCategory;
    }

    static UserCategoryEntity of(UserEntity user, MoimCategory moimCategory) {
        return new UserCategoryEntity(user, moimCategory);
    }
}
