package org.codeit.roomunion.auth.application.port.in;

import org.codeit.roomunion.auth.application.service.AuthService;
import org.codeit.roomunion.common.application.port.out.*;
import org.codeit.roomunion.common.exception.CustomException;
import org.codeit.roomunion.user.application.port.in.FakeUserService;
import org.codeit.roomunion.user.application.port.in.UserCommandUseCase;
import org.codeit.roomunion.user.application.port.in.UserQueryUseCase;
import org.codeit.roomunion.user.domain.command.UserCreateCommandFixture;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class AuthUseCaseTest {

    private UserQueryUseCase userQueryUseCase;
    private UserCommandUseCase userCommandUseCase;
    private RandomNumberGenerator randomNumberGenerator;
    private TimeHolder timeHolder;
    private EventPublisher eventPublisher;

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        FakeUserService fakeUserService = new FakeUserService();
        userQueryUseCase = fakeUserService;
        userCommandUseCase = fakeUserService;
        randomNumberGenerator = new FakeRandomNumberGenerator(10000);
        timeHolder = new FakeTimeHolder(LocalDateTime.now());
        eventPublisher = new FakeEventPublisher();
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }

    @DisplayName("Send Verification Code")
    @Nested
    class SendVerificationCode {

        @DisplayName("sendVerificationCode 를 호출하면 EventPublisher 의 publish 가 호출된다.")
        @Test
        void sendVerificationCodeSuccess() {
            // Given
            AuthService authService = new AuthService(userQueryUseCase, userCommandUseCase, randomNumberGenerator, timeHolder, eventPublisher);

            // When & Then
            assertThatCode(() -> authService.sendVerificationCode("bht9011@gmail.com"))
                .doesNotThrowAnyException();
            String text = outputStreamCaptor.toString();

            String expect = "Published EmailVerificationCodeEvent / email [bht9011@gmail.com], code [010000]";
            assertThat(text).isEqualTo(expect);
        }

        @DisplayName("sendVerificationCode 를 호출할 때 이미 회원으로 가입한 이메일이면 예외가 발생한다.")
        @ParameterizedTest
        @ValueSource(strings = {"bht9011@gmail.com", "test@test.com"})
        void sendVerificationCodeFailAlreadyExistsEmail(String email) {
            // Given
            AuthService authService = new AuthService(userQueryUseCase, userCommandUseCase, randomNumberGenerator, timeHolder, eventPublisher);
            userCommandUseCase.join(UserCreateCommandFixture.create(email));

            // When & Then
            assertThatThrownBy(() -> authService.sendVerificationCode(email))
                .isInstanceOf(CustomException.class)
                .hasMessage("이미 가입된 이메일");
        }

    }

    @DisplayName("Extend Expiration")
    @Nested
    class ExtendExpiration {

        @DisplayName("extendExpiration 를 호출하면 유효기간이 연장된다.")
        @Test
        void extendExpirationSuccess() {
            // Given
            AuthService authService = new AuthService(userQueryUseCase, userCommandUseCase, randomNumberGenerator, timeHolder, eventPublisher);

            // When & Then
            assertThatCode(() -> authService.extendExpiration("bht9011@gmail.com"))
                .doesNotThrowAnyException();
        }

    }

}