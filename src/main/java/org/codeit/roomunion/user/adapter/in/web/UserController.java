package org.codeit.roomunion.user.adapter.in.web;

import org.codeit.roomunion.auth.domain.model.CustomUserDetails;
import org.codeit.roomunion.user.adapter.in.web.request.JoinUserRequest;
import org.codeit.roomunion.user.adapter.in.web.request.UserModifyRequest;
import org.codeit.roomunion.user.adapter.in.web.response.JoinUserResponse;
import org.codeit.roomunion.user.adapter.in.web.response.UserInfoResponse;
import org.codeit.roomunion.user.application.port.in.UserCommandUseCase;
import org.codeit.roomunion.user.domain.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    private final UserCommandUseCase userCommandUseCase;

    public UserController(UserCommandUseCase userCommandUseCase) {
        this.userCommandUseCase = userCommandUseCase;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<JoinUserResponse> createUser(@RequestBody JoinUserRequest request) {
        User user = userCommandUseCase.join(request.toCommand());
        return ResponseEntity.ok(JoinUserResponse.from(user));
    }

    @PutMapping
    public ResponseEntity<Void> modifyUser(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @ModelAttribute UserModifyRequest request,
        @RequestPart(required = false) MultipartFile profileImage
    ) {
        userCommandUseCase.modify(userDetails.getUser(), request.toCommand(), profileImage);
        return ResponseEntity.noContent()
            .build();
    }


    @GetMapping
    public ResponseEntity<UserInfoResponse> getUserInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userCommandUseCase.getUserInfo(userDetails.getUser());
        return ResponseEntity.ok(UserInfoResponse.from(user));
    }

}
