package org.codeit.roomunion.user.application.port.out;

<<<<<<< HEAD
import org.codeit.roomunion.user.domain.command.UserCreateCommand;
import org.codeit.roomunion.user.domain.model.User;

import java.util.Optional;

public interface UserRepository {

    User create(UserCreateCommand userCreateCommand);

    Optional<User> findByEmail(String email);

    Optional<User> findByNickname(String nickname);
=======
import org.codeit.roomunion.user.domain.model.User;

public interface UserRepository {

    User getByEmail(String email);

>>>>>>> 8abfdd5 (feat: 스프링 시큐리티 개발 (#3))
}
