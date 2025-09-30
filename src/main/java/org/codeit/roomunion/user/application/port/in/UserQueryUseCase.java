package org.codeit.roomunion.user.application.port.in;

import org.codeit.roomunion.user.domain.model.User;

<<<<<<< HEAD
<<<<<<< HEAD
import java.util.Optional;

=======
>>>>>>> 8abfdd5 (feat: 스프링 시큐리티 개발 (#3))
=======
import java.util.Optional;

>>>>>>> 98b72bc (feat: 회원가입, 로그인 구현 (#6))
public interface UserQueryUseCase {

    User getByEmail(String email);

<<<<<<< HEAD
<<<<<<< HEAD
    Optional<User> findByEmail(String email);

=======
>>>>>>> 8abfdd5 (feat: 스프링 시큐리티 개발 (#3))
=======
    Optional<User> findByEmail(String email);

>>>>>>> 98b72bc (feat: 회원가입, 로그인 구현 (#6))
}
