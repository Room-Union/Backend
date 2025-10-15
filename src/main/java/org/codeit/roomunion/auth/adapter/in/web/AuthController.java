package org.codeit.roomunion.auth.adapter.in.web;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.codeit.roomunion.auth.adapter.in.web.request.SendVerificationCodeRequest;
import org.codeit.roomunion.auth.adapter.in.web.request.VerifyEmailCodeRequest;
import org.codeit.roomunion.auth.application.port.in.AuthUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@Tag(name = "Auth API", description = "인증 API")
public class AuthController {

    private final AuthUseCase authUsecase;

    public AuthController(AuthUseCase authUsecase) {
        this.authUsecase = authUsecase;
    }

    @PostMapping("/email/send")
    public ResponseEntity<Void> sendVerificationEmail(@RequestBody SendVerificationCodeRequest request) {
        authUsecase.sendVerificationCode(request.email());
        return ResponseEntity.noContent()
            .build();
    }

    @PostMapping("/email/verify")
    public ResponseEntity<Void> verifyCode(@RequestBody VerifyEmailCodeRequest request) {
        authUsecase.verifyCode(request.email(), request.code());
        return ResponseEntity.noContent()
            .build();
    }

    @PostMapping("/email/extend")
    public ResponseEntity<Void> extendVerificationCode(@RequestBody SendVerificationCodeRequest request) {
        authUsecase.extendExpiration(request.email());
        return ResponseEntity.noContent()
            .build();
    }

}
