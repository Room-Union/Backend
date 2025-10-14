package org.codeit.roomunion.auth.application.service;

import org.codeit.roomunion.auth.application.port.in.AuthUseCase;
import org.codeit.roomunion.auth.domain.event.EmailVerificationCodeEvent;
import org.codeit.roomunion.auth.domain.exception.AuthErrorCode;
import org.codeit.roomunion.auth.domain.model.CustomUserDetails;
import org.codeit.roomunion.auth.domain.policy.EmailVerificationPolicy;
import org.codeit.roomunion.common.application.port.out.EventPublisher;
import org.codeit.roomunion.common.application.port.out.RandomNumberGenerator;
import org.codeit.roomunion.common.application.port.out.TimeHolder;
import org.codeit.roomunion.common.exception.CustomException;
import org.codeit.roomunion.common.jwt.JwtUtil;
import org.codeit.roomunion.user.application.port.in.UserCommandUseCase;
import org.codeit.roomunion.user.application.port.in.UserQueryUseCase;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
public class AuthService implements AuthUseCase {

    private static final int VERIFICATION_CODE_BOUND = 1000000;
    private static final String ROOM_UNION_EMAIL_VERIFICATION_SUBJECT = "RoomUnion 회원가입 인증 코드";
    private static final String ROOM_UNION_EMAIL_VERIFICATION_BODY = "인증 코드: %s";
    private static final String BEARER_TYPE = "Bearer %s";
    private static final long JWT_EXPIRATION = Duration.ofHours(24).toMillis() * 7; // 7 days

    private final UserQueryUseCase userQueryUseCase;
    private final UserCommandUseCase userCommandUseCase;
    private final RandomNumberGenerator randomNumberGenerator;
    private final TimeHolder timeHolder;
    private final EventPublisher eventPublisher;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthService(UserQueryUseCase userQueryUseCase, UserCommandUseCase userCommandUseCase, RandomNumberGenerator randomNumberGenerator, TimeHolder timeHolder, EventPublisher eventPublisher, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userQueryUseCase = userQueryUseCase;
        this.userCommandUseCase = userCommandUseCase;
        this.randomNumberGenerator = randomNumberGenerator;
        this.timeHolder = timeHolder;
        this.eventPublisher = eventPublisher;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public String login(String email, String password) {
        try {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password);
            Authentication authentication = authenticationManager.authenticate(authToken);

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Long userId = userDetails.getId();
            String userEmail = userDetails.getUsername();

            String jwt = jwtUtil.createJwt(userId, userEmail, JWT_EXPIRATION);
            return BEARER_TYPE.formatted(jwt);
        } catch (AuthenticationException e) {
            throw new CustomException(AuthErrorCode.INVALID_INPUT_VALUE);
        }
    }

    @Override
    @Transactional
    public void sendVerificationCode(String email) {
        userQueryUseCase.validateEmailExists(email);

        LocalDateTime currentAt = timeHolder.localDateTime();
        LocalDateTime expirationAt = EmailVerificationPolicy.calculateExpirationAt(currentAt);
        String code = generateVerifyCode();

        userCommandUseCase.saveEmailVerificationCode(email, code, currentAt, expirationAt);

        EmailVerificationCodeEvent emailVerificationCodeEvent = EmailVerificationCodeEvent.of(email, code, ROOM_UNION_EMAIL_VERIFICATION_SUBJECT, ROOM_UNION_EMAIL_VERIFICATION_BODY.formatted(code));
        eventPublisher.publish(emailVerificationCodeEvent);
    }

    @Override
    @Transactional
    public void verifyCode(String email, String code) {
        LocalDateTime currentAt = timeHolder.localDateTime();
        userQueryUseCase.verifyCode(email, code, currentAt);
    }

    @Override
    @Transactional
    public void extendExpiration(String email) {
        LocalDateTime currentAt = timeHolder.localDateTime();
        LocalDateTime expirationAt = EmailVerificationPolicy.calculateExpirationAt(currentAt);
        userCommandUseCase.validateEmailNotVerified(email, expirationAt);
    }

    private String generateVerifyCode() {
        int randomNumber = randomNumberGenerator.generate(VERIFICATION_CODE_BOUND);
        return String.format("%06d", randomNumber);
    }

}
