package org.codeit.roomunion.user.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.codeit.roomunion.crew.adapter.out.persistence.entity.CrewEntity;
import org.codeit.roomunion.crew.adapter.out.persistence.entity.CrewMemberEntity;
import org.codeit.roomunion.user.domain.model.User;
import org.hibernate.annotations.NaturalId;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NaturalId
    private String email;

    private String password;

    private String name;

    private String nickname;

    private String description;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CrewMemberEntity> crewMembers = new ArrayList<>();

    protected UserEntity() {
    }

    public static User toDomain(UserEntity userEntity) {
        return null;
    }
}
