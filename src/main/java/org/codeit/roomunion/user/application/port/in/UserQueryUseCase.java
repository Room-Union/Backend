package org.codeit.roomunion.user.application.port.in;

import org.codeit.roomunion.user.domain.model.User;

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
=======
import java.time.LocalDateTime;
>>>>>>> 351834c (feat: 회원가입 이메일 검증 로직 개발 (이메일 코드 발송, 이메일 코드 연장, 이메일 코드 검증) (#11))
import java.util.Optional;

=======
>>>>>>> 8abfdd5 (feat: 스프링 시큐리티 개발 (#3))
=======
import java.util.Optional;

>>>>>>> 98b72bc (feat: 회원가입, 로그인 구현 (#6))
public interface UserQueryUseCase {

<<<<<<< HEAD
    User getByEmail(String email);

<<<<<<< HEAD
<<<<<<< HEAD
    Optional<User> findByEmail(String email);

=======
>>>>>>> 8abfdd5 (feat: 스프링 시큐리티 개발 (#3))
=======
    Optional<User> findByEmail(String email);

>>>>>>> 98b72bc (feat: 회원가입, 로그인 구현 (#6))
=======
    Optional<User> findByEmail(String email);

    void validateEmailExists(String email);

    void verifyCode(String email, String code, LocalDateTime currentAt);
>>>>>>> 351834c (feat: 회원가입 이메일 검증 로직 개발 (이메일 코드 발송, 이메일 코드 연장, 이메일 코드 검증) (#11))
}
