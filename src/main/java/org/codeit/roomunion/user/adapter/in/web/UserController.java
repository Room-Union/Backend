package org.codeit.roomunion.user.adapter.in.web;

import org.codeit.roomunion.user.adapter.in.web.request.JoinUserRequest;
import org.codeit.roomunion.user.adapter.in.web.response.JoinUserResponse;
import org.codeit.roomunion.user.application.port.in.UserCommandUseCase;
import org.codeit.roomunion.user.domain.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    private final UserCommandUseCase userCommandUseCase;

    public UserController(UserCommandUseCase userCommandUseCase) {
        this.userCommandUseCase = userCommandUseCase;
    }

    @PostMapping(value = "/sign-up")
    public ResponseEntity<JoinUserResponse> createUser(@ModelAttribute JoinUserRequest request) {
        User user = userCommandUseCase.join(request.toCommand());
        return ResponseEntity.ok(JoinUserResponse.from(user));
    }
}
