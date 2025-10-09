package org.codeit.roomunion.user.adapter.in.web;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.codeit.roomunion.user.adapter.in.web.request.JoinUserRequest;
import org.codeit.roomunion.user.adapter.in.web.response.JoinUserResponse;
import org.codeit.roomunion.user.application.port.in.UserCommandUseCase;
import org.codeit.roomunion.user.domain.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/users")
@Tag(name = "User API", description = "회원 API")
public class UserController {

    private final UserCommandUseCase userCommandUseCase;

    public UserController(UserCommandUseCase userCommandUseCase) {
        this.userCommandUseCase = userCommandUseCase;
    }

    @PostMapping(value = "/sign-up")
    public ResponseEntity<JoinUserResponse> createUser(@RequestBody JoinUserRequest request) {
        User user = userCommandUseCase.join(request.toCommand());
        return ResponseEntity.ok(JoinUserResponse.from(user));
    }
}
