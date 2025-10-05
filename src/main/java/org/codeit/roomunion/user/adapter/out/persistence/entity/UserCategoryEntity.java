package org.codeit.roomunion.user.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
<<<<<<< HEAD
<<<<<<< HEAD
import org.codeit.roomunion.meeting.domain.model.enums.MeetingCategory;
=======
import org.codeit.roomunion.moim.domain.model.Category;
>>>>>>> 98b72bc (feat: 회원가입, 로그인 구현 (#6))
=======
import org.codeit.roomunion.meeting.domain.model.enums.MeetingCategory;
>>>>>>> f2440ea (:sparkles: 전역 예외 처리 및 모임 생성 기능, 특정 모임 조회 기능 구현 (#9))

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
<<<<<<< HEAD
<<<<<<< HEAD
    private MeetingCategory meetingCategory;
=======
    private Category category;
>>>>>>> 98b72bc (feat: 회원가입, 로그인 구현 (#6))
=======
    private MeetingCategory meetingCategory;
>>>>>>> f2440ea (:sparkles: 전역 예외 처리 및 모임 생성 기능, 특정 모임 조회 기능 구현 (#9))

    protected UserCategoryEntity() {
    }

<<<<<<< HEAD
<<<<<<< HEAD
    UserCategoryEntity(UserEntity user, MeetingCategory meetingCategory) {
        this.user = user;
        this.meetingCategory = meetingCategory;
    }

    static UserCategoryEntity of(UserEntity user, MeetingCategory meetingCategory) {
        return new UserCategoryEntity(user, meetingCategory);
=======
    UserCategoryEntity(UserEntity user, Category category) {
=======
    UserCategoryEntity(UserEntity user, MeetingCategory meetingCategory) {
>>>>>>> f2440ea (:sparkles: 전역 예외 처리 및 모임 생성 기능, 특정 모임 조회 기능 구현 (#9))
        this.user = user;
        this.meetingCategory = meetingCategory;
    }

<<<<<<< HEAD
    static UserCategoryEntity of(UserEntity user, Category category) {
        return new UserCategoryEntity(user, category);
>>>>>>> 98b72bc (feat: 회원가입, 로그인 구현 (#6))
=======
    static UserCategoryEntity of(UserEntity user, MeetingCategory meetingCategory) {
        return new UserCategoryEntity(user, meetingCategory);
>>>>>>> f2440ea (:sparkles: 전역 예외 처리 및 모임 생성 기능, 특정 모임 조회 기능 구현 (#9))
    }
}
