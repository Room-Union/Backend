package org.codeit.roomunion.user.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.codeit.roomunion.meeting.domain.model.enums.MeetingCategory;

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
    private MeetingCategory category;

    protected UserCategoryEntity() {
    }

    UserCategoryEntity(UserEntity user, MeetingCategory category) {
        this.user = user;
        this.category = category;
    }

    static UserCategoryEntity of(UserEntity user, MeetingCategory category) {
        return new UserCategoryEntity(user, category);
    }

    public MeetingCategory toDomain() {
        return this.category;
    }
}
