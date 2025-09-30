package org.codeit.roomunion.user.application.port.out;

<<<<<<< HEAD
<<<<<<< HEAD
import org.codeit.roomunion.user.domain.command.UserCreateCommand;
import org.codeit.roomunion.user.domain.model.User;

import java.util.Optional;

public interface UserRepository {

    User create(UserCreateCommand userCreateCommand);

    Optional<User> findByEmail(String email);

    Optional<User> findByNickname(String nickname);
=======
=======
import org.codeit.roomunion.user.domain.command.UserCreateCommand;
>>>>>>> 98b72bc (feat: 회원가입, 로그인 구현 (#6))
import org.codeit.roomunion.user.domain.model.User;

import java.util.Optional;

public interface UserRepository {

    User create(UserCreateCommand userCreateCommand);

<<<<<<< HEAD
>>>>>>> 8abfdd5 (feat: 스프링 시큐리티 개발 (#3))
=======
    Optional<User> findByEmail(String email);

    Optional<User> findByNickname(String nickname);
>>>>>>> 98b72bc (feat: 회원가입, 로그인 구현 (#6))
}
