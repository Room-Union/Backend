package org.codeit.roomunion.user.application.port.in;

import org.codeit.roomunion.user.domain.command.UserCreateCommand;
import org.codeit.roomunion.user.domain.command.UserModifyCommand;
import org.codeit.roomunion.user.domain.model.User;
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
=======
import org.springframework.web.multipart.MultipartFile;
>>>>>>> 16456c7 (feat: 유저 수정 API, 유저 정보 조회 API, 비밀번호 변경 API 개발 (#12))

import java.time.LocalDateTime;

public interface UserCommandUseCase {

    User join(UserCreateCommand userCreateCommand);
=======
import org.springframework.web.multipart.MultipartFile;

public interface UserCommandUseCase {

    User join(UserCreateCommand userCreateCommand, MultipartFile profileImage);
>>>>>>> 98b72bc (feat: 회원가입, 로그인 구현 (#6))
=======

public interface UserCommandUseCase {

    User join(UserCreateCommand userCreateCommand);
>>>>>>> 521de11 (refactor: 회원가입시 프로필 이미지 제거, 비밀번호 유효성체크 수정, 기타 수정 (#8))

    void modify(User user, UserModifyCommand userModifyCommand, MultipartFile profileImage);

    void updatePassword(User user, String password, String newPassword);

    void saveEmailVerificationCode(String email, String code, LocalDateTime currentAt, LocalDateTime expirationAt);

    void validateEmailNotVerified(String email, LocalDateTime expirationAt);

    User getUserInfo(User user);
}
