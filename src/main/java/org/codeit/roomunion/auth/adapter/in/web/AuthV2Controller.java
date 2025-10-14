package org.codeit.roomunion.auth.adapter.in.web;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.codeit.roomunion.auth.adapter.in.web.request.LoginRequest;
import org.codeit.roomunion.auth.adapter.in.web.response.LoginResponse;
import org.codeit.roomunion.auth.application.port.in.AuthUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v2/auth")
@Tag(name = "Auth API v2", description = "인증 API v2")
public class AuthV2Controller {

    private final AuthUseCase authUsecase;

    public AuthV2Controller(AuthUseCase authUsecase) {
        this.authUsecase = authUsecase;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        String token = authUsecase.login(request.email(), request.password());
        return ResponseEntity.ok(LoginResponse.from(token));
    }
}
