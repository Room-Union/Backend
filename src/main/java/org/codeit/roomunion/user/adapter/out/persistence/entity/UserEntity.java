package org.codeit.roomunion.user.adapter.out.persistence.entity;

<<<<<<< HEAD
import jakarta.persistence.*;
import lombok.Getter;
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
import lombok.Getter;
import org.hibernate.annotations.NaturalId;

@Getter
@Table(name = "users")
public class UserEntity {
>>>>>>> 0efc476 (Feature/member (#2))
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NaturalId
    private String email;

    private String password;

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

    private String nickname;

    private String description;

    protected UserEntity() {
    }
>>>>>>> 0efc476 (Feature/member (#2))
}
