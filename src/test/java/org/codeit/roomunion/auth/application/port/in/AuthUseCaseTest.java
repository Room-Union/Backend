package org.codeit.roomunion.auth.application.port.in;

import org.codeit.roomunion.auth.application.service.AuthService;
import org.codeit.roomunion.common.application.port.out.*;
import org.codeit.roomunion.user.application.port.in.UserCommandUseCase;
import org.codeit.roomunion.user.application.port.in.UserQueryUseCase;
import org.codeit.roomunion.user.application.port.in.FakeUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class AuthUseCaseTest {

    private UserQueryUseCase userQueryUseCase;
    private UserCommandUseCase userCommandUseCase;
    private RandomNumberGenerator randomNumberGenerator;
    private TimeHolder timeHolder;
    private EventPublisher eventPublisher;

    @BeforeEach
    void setUp() {
        userQueryUseCase = new FakeUserService();
        userCommandUseCase = new FakeUserService();
        randomNumberGenerator = new FakeRandomNumberGenerator(10000);
        timeHolder = new FakeTimeHolder(LocalDateTime.now());
        eventPublisher = new FakeEventPublisher();
    }

    @DisplayName("Send Verification Code")
    @Nested
    class SendVerificationCode {

        @DisplayName("sendVerificationCode 를 호출하면 EventPublisher 의 publish 가 호출된다.")
        @Test
        void sendVerificationCodeSuccess() {
            // Given
            AuthService authService = new AuthService(userQueryUseCase, userCommandUseCase, randomNumberGenerator, timeHolder, eventPublisher);

            // When
            authService.sendVerificationCode("bht9011@gmail.com");


        }

    }

}