package org.codeit.roomunion.user.application.port.out;

<<<<<<< HEAD
<<<<<<< HEAD
import org.codeit.roomunion.user.domain.command.UserCreateCommand;
import org.codeit.roomunion.user.domain.command.UserModifyCommand;
import org.codeit.roomunion.user.domain.model.User;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserRepository {

    User create(UserCreateCommand userCreateCommand);

    Optional<User> findByEmail(String email);

    Optional<User> findByNickname(String nickname);
<<<<<<< HEAD
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
=======

    void saveEmailVerificationCode(String email, String code, LocalDateTime currentAt, LocalDateTime expirationAt);

    void verifyCode(String email, String code, LocalDateTime currentAt);

    void validateEmailNotVerified(String email, LocalDateTime expirationAt);
<<<<<<< HEAD
>>>>>>> 351834c (feat: 회원가입 이메일 검증 로직 개발 (이메일 코드 발송, 이메일 코드 연장, 이메일 코드 검증) (#11))
=======

    void update(User user, UserModifyCommand userModifyCommand, boolean isUpdateImage);

    User getByWithCategories(User user);

    void updatePassword(User user, String encodedPassword);
>>>>>>> 16456c7 (feat: 유저 수정 API, 유저 정보 조회 API, 비밀번호 변경 API 개발 (#12))
}
