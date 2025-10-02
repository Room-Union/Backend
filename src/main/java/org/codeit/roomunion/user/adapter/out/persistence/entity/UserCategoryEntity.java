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
    private MeetingCategory meetingCategory;

    protected UserCategoryEntity() {
    }

    UserCategoryEntity(UserEntity user, MeetingCategory meetingCategory) {
        this.user = user;
        this.meetingCategory = meetingCategory;
    }

    static UserCategoryEntity of(UserEntity user, MeetingCategory meetingCategory) {
        return new UserCategoryEntity(user, meetingCategory);
    }
}
