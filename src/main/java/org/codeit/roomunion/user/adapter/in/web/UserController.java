package org.codeit.roomunion.user.adapter.in.web;

import org.codeit.roomunion.user.adapter.in.web.request.JoinUserRequest;
import org.codeit.roomunion.user.adapter.in.web.response.JoinUserResponse;
import org.codeit.roomunion.user.application.port.in.UserCommandUseCase;
import org.codeit.roomunion.user.domain.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
<<<<<<< HEAD
=======
import org.springframework.web.multipart.MultipartFile;
>>>>>>> 98b72bc (feat: 회원가입, 로그인 구현 (#6))

@RestController
@RequestMapping("/v1/users")
public class UserController {

    private final UserCommandUseCase userCommandUseCase;

    public UserController(UserCommandUseCase userCommandUseCase) {
        this.userCommandUseCase = userCommandUseCase;
    }

    @PostMapping(value = "/sign-up")
<<<<<<< HEAD
    public ResponseEntity<JoinUserResponse> createUser(@RequestBody JoinUserRequest request) {
        User user = userCommandUseCase.join(request.toCommand());
=======
    public ResponseEntity<JoinUserResponse> createUser(@ModelAttribute JoinUserRequest request, @RequestPart(required = false) MultipartFile profileImage) {
        User user = userCommandUseCase.join(request.toCommand(), profileImage);
>>>>>>> 98b72bc (feat: 회원가입, 로그인 구현 (#6))
        return ResponseEntity.ok(JoinUserResponse.from(user));
    }
}
