package org.codeit.roomunion.user.adapter.out.persistence.entity;

<<<<<<< HEAD
<<<<<<< HEAD
import jakarta.persistence.*;
import lombok.Getter;
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> f2440ea (:sparkles: 전역 예외 처리 및 모임 생성 기능, 특정 모임 조회 기능 구현 (#9))
import org.codeit.roomunion.meeting.adapter.out.persistence.entity.MeetingMemberEntity;
import org.codeit.roomunion.meeting.domain.model.enums.MeetingCategory;
import org.codeit.roomunion.user.domain.command.UserCreateCommand;
import org.codeit.roomunion.user.domain.command.UserModifyCommand;
import org.codeit.roomunion.user.domain.model.Gender;
import org.codeit.roomunion.user.domain.model.User;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.NaturalId;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Entity
@Table(name = "users")
public class UserEntity {

=======
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
=======
import jakarta.persistence.*;
>>>>>>> 8abfdd5 (feat: 스프링 시큐리티 개발 (#3))
import lombok.Getter;
=======
import org.codeit.roomunion.user.domain.command.UserCreateCommand;
import org.codeit.roomunion.user.domain.model.Gender;
>>>>>>> 98b72bc (feat: 회원가입, 로그인 구현 (#6))
import org.codeit.roomunion.user.domain.model.User;
import org.hibernate.annotations.NaturalId;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "users")
public class UserEntity {
<<<<<<< HEAD
>>>>>>> 0efc476 (Feature/member (#2))
=======

>>>>>>> 98b72bc (feat: 회원가입, 로그인 구현 (#6))
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NaturalId
    private String email;

    private String password;

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
=======
    @Column(unique = true)
>>>>>>> 16456c7 (feat: 유저 수정 API, 유저 정보 조회 API, 비밀번호 변경 API 개발 (#12))
    private String nickname;

    private Gender gender;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserCategoryEntity> userCategories = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MeetingMemberEntity> meetingMembers = new ArrayList<>();

    @ColumnDefault("false")
    private boolean hasImage;

    protected UserEntity() {
    }

    private UserEntity(String email, String password, String nickname, Gender gender) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.gender = gender;
    }

    public static UserEntity of(UserCreateCommand userCreateCommand) {
        UserEntity userEntity = new UserEntity(userCreateCommand.getEmail(), userCreateCommand.getPassword(), userCreateCommand.getNickname(), userCreateCommand.getGender());
        userEntity.userCategories = createUserCategoryEntities(userCreateCommand.getCategories(), userEntity);
        return userEntity;
    }

    private static List<UserCategoryEntity> createUserCategoryEntities(Set<MeetingCategory> userCreateCommand, UserEntity userEntity) {
        return userCreateCommand.stream()
            .map(category -> UserCategoryEntity.of(userEntity, category))
            .toList();
    }

    public User toDomain() {
        return User.of(id, email, password, nickname, gender);
    }
<<<<<<< HEAD
=======
    private String name;

=======
>>>>>>> 98b72bc (feat: 회원가입, 로그인 구현 (#6))
    private String nickname;

    private Gender gender;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserCategoryEntity> userCategories = new ArrayList<>();

    protected UserEntity() {
    }
<<<<<<< HEAD
>>>>>>> 0efc476 (Feature/member (#2))
=======

    private UserEntity(String email, String password, String nickname, Gender gender) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.gender = gender;
    }

    public static UserEntity of(UserCreateCommand userCreateCommand) {
        UserEntity userEntity = new UserEntity(userCreateCommand.getEmail(), userCreateCommand.getPassword(), userCreateCommand.getNickname(), userCreateCommand.getGender());
        userEntity.userCategories = createUserCategoryEntities(userCreateCommand, userEntity);
        return userEntity;
    }

    private static List<UserCategoryEntity> createUserCategoryEntities(UserCreateCommand userCreateCommand, UserEntity userEntity) {
        return userCreateCommand.getCategories()
            .stream()
            .map(category -> UserCategoryEntity.of(userEntity, category))
            .toList();
    }

    public User toDomain() {
        return User.of(id, email, password, nickname, gender);
    }
>>>>>>> 8abfdd5 (feat: 스프링 시큐리티 개발 (#3))
=======

    public User toDomainWithCategories(String imageUrl) {
        Set<MeetingCategory> categories = userCategories.stream()
            .map(UserCategoryEntity::toDomain)
            .collect(Collectors.toUnmodifiableSet());
        return User.of(id, email, password, nickname, gender, categories, imageUrl);
    }

    public void update(UserModifyCommand userModifyCommand, boolean isUpdateImage) {
        modifyNickname(userModifyCommand.getNickname());
        gender = userModifyCommand.getGender();
        userCategories.addAll(createUserCategoryEntities(userModifyCommand.getCategories(), this));
        if (isUpdateImage) {
            hasImage = true;
        }
    }

    private void modifyNickname(String nickname) {
        if (nickname != null) {
            this.nickname = nickname;
        }
    }

    public void clearCategories() {
        userCategories.clear();
    }

    public void updatePassword(String encodedPassword) {
        this.password = encodedPassword;
    }
>>>>>>> 16456c7 (feat: 유저 수정 API, 유저 정보 조회 API, 비밀번호 변경 API 개발 (#12))
}
