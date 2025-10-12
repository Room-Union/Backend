package org.codeit.roomunion.user.adapter.in.web;

import org.codeit.roomunion.auth.domain.model.CustomUserDetails;
import org.codeit.roomunion.user.adapter.in.web.request.JoinUserRequest;
import org.codeit.roomunion.user.adapter.in.web.request.UserModifyRequest;
import org.codeit.roomunion.user.adapter.in.web.request.UserUpdatePasswordRequest;
import org.codeit.roomunion.user.adapter.in.web.response.JoinUserResponse;
import org.codeit.roomunion.user.adapter.in.web.response.UserInfoResponse;
import org.codeit.roomunion.user.application.port.in.UserCommandUseCase;
import org.codeit.roomunion.user.domain.model.User;
import org.springframework.http.ResponseEntity;
<<<<<<< HEAD
<<<<<<< HEAD
import org.springframework.web.bind.annotation.*;
<<<<<<< HEAD
<<<<<<< HEAD
=======
import org.springframework.web.multipart.MultipartFile;
>>>>>>> 98b72bc (feat: 회원가입, 로그인 구현 (#6))
=======
>>>>>>> 521de11 (refactor: 회원가입시 프로필 이미지 제거, 비밀번호 유효성체크 수정, 기타 수정 (#8))
=======
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
>>>>>>> 351834c (feat: 회원가입 이메일 검증 로직 개발 (이메일 코드 발송, 이메일 코드 연장, 이메일 코드 검증) (#11))
=======
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
>>>>>>> 16456c7 (feat: 유저 수정 API, 유저 정보 조회 API, 비밀번호 변경 API 개발 (#12))

@RestController
@RequestMapping("/v1/users")
public class UserController {

    private final UserCommandUseCase userCommandUseCase;

    public UserController(UserCommandUseCase userCommandUseCase) {
        this.userCommandUseCase = userCommandUseCase;
    }

<<<<<<< HEAD
    @PostMapping(value = "/sign-up")
<<<<<<< HEAD
<<<<<<< HEAD
=======
    @PostMapping("/sign-up")
>>>>>>> 16456c7 (feat: 유저 수정 API, 유저 정보 조회 API, 비밀번호 변경 API 개발 (#12))
    public ResponseEntity<JoinUserResponse> createUser(@RequestBody JoinUserRequest request) {
        User user = userCommandUseCase.join(request.toCommand());
=======
    public ResponseEntity<JoinUserResponse> createUser(@ModelAttribute JoinUserRequest request, @RequestPart(required = false) MultipartFile profileImage) {
        User user = userCommandUseCase.join(request.toCommand(), profileImage);
>>>>>>> 98b72bc (feat: 회원가입, 로그인 구현 (#6))
=======
    public ResponseEntity<JoinUserResponse> createUser(@RequestBody JoinUserRequest request) {
        User user = userCommandUseCase.join(request.toCommand());
>>>>>>> 521de11 (refactor: 회원가입시 프로필 이미지 제거, 비밀번호 유효성체크 수정, 기타 수정 (#8))
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

    @PutMapping("/password")
    public ResponseEntity<Void> updatePassword(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestBody UserUpdatePasswordRequest request
    ) {
        userCommandUseCase.updatePassword(userDetails.getUser(), request.password(), request.newPassword());
        return ResponseEntity.noContent()
            .build();
    }

}
