package org.codeit.roomunion.auth.adapter.in.web;

import org.codeit.roomunion.auth.adapter.in.web.request.SendVarificationCodeRequest;
import org.codeit.roomunion.auth.application.port.in.AuthUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    private final AuthUseCase authUsecase;

    public AuthController(AuthUseCase authUsecase) {
        this.authUsecase = authUsecase;
    }

    @PostMapping("/email/send")
    public ResponseEntity<Void> sendVerificationEmail(@RequestBody SendVarificationCodeRequest request) {
        authUsecase.sendVerificationCode(request.email());
        return ResponseEntity.noContent()
            .build();
    }

}
