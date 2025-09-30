package org.codeit.roomunion.user.adapter.out.persistence.entity;

<<<<<<< HEAD
<<<<<<< HEAD
import jakarta.persistence.*;
import lombok.Getter;
<<<<<<< HEAD
import org.codeit.roomunion.meeting.adapter.out.persistence.entity.MeetingMemberEntity;
import org.codeit.roomunion.user.domain.command.UserCreateCommand;
import org.codeit.roomunion.user.domain.model.Gender;
import org.codeit.roomunion.user.domain.model.User;
import org.hibernate.annotations.NaturalId;

import java.util.ArrayList;
import java.util.List;

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
    private String nickname;

    private Gender gender;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserCategoryEntity> userCategories = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MeetingMemberEntity> meetingMembers = new ArrayList<>();

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
}
