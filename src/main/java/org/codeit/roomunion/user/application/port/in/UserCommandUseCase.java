package org.codeit.roomunion.user.application.port.in;

import org.codeit.roomunion.user.domain.command.UserCreateCommand;
import org.codeit.roomunion.user.domain.model.User;
<<<<<<< HEAD

public interface UserCommandUseCase {

    User join(UserCreateCommand userCreateCommand);
=======
import org.springframework.web.multipart.MultipartFile;

public interface UserCommandUseCase {

    User join(UserCreateCommand userCreateCommand, MultipartFile profileImage);
>>>>>>> 98b72bc (feat: 회원가입, 로그인 구현 (#6))

}
