package org.codeit.roomunion.user.domain.model;

public class UserFixture {

    public static User create(Long id, String email) {
        return User.of(id, email, "password", "nickname", Gender.FEMALE);
    }
}