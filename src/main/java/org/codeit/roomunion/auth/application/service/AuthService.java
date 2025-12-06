package org.codeit.roomunion.auth.application.service;

import lombok.extern.slf4j.Slf4j;
import org.codeit.roomunion.auth.application.port.in.AuthUseCase;
import org.codeit.roomunion.auth.application.port.out.RefreshTokenRepository;
import org.codeit.roomunion.auth.domain.event.EmailVerificationCodeEvent;
import org.codeit.roomunion.auth.domain.exception.AuthErrorCode;
import org.codeit.roomunion.auth.domain.model.LoginResult;
import org.codeit.roomunion.auth.domain.model.LoginUserDetails;
import org.codeit.roomunion.auth.domain.model.RefreshResult;
import org.codeit.roomunion.auth.domain.model.RefreshToken;
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

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
@Slf4j
public class AuthService implements AuthUseCase {

    private static final int VERIFICATION_CODE_BOUND = 1000000;
    private static final String ROOM_UNION_EMAIL_VERIFICATION_SUBJECT = "RoomUnion 회원가입 인증 코드";
    private static final String ROOM_UNION_EMAIL_VERIFICATION_BODY = "인증 코드: %s";

    private final UserQueryUseCase userQueryUseCase;
    private final UserCommandUseCase userCommandUseCase;
    private final RandomNumberGenerator randomNumberGenerator;
    private final TimeHolder timeHolder;
    private final EventPublisher eventPublisher;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthService(UserQueryUseCase userQueryUseCase, UserCommandUseCase userCommandUseCase, RandomNumberGenerator randomNumberGenerator, TimeHolder timeHolder, EventPublisher eventPublisher, AuthenticationManager authenticationManager, JwtUtil jwtUtil, JwtService jwtService, RefreshTokenRepository refreshTokenRepository) {
        this.userQueryUseCase = userQueryUseCase;
        this.userCommandUseCase = userCommandUseCase;
        this.randomNumberGenerator = randomNumberGenerator;
        this.timeHolder = timeHolder;
        this.eventPublisher = eventPublisher;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.jwtService = jwtService;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    @Transactional
    public LoginResult login(String email, String password) {
        try {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password);
            Authentication authentication = authenticationManager.authenticate(authToken);

            LoginUserDetails userDetails = (LoginUserDetails) authentication.getPrincipal();
            Long userId = userDetails.getId();
            String userEmail = userDetails.getUsername();

            String accessToken = jwtUtil.createAccessToken(userId, userEmail);
            String refreshToken = jwtUtil.createRefreshToken(userId, userEmail);

            LocalDateTime currentAt = timeHolder.localDateTime();
            LocalDateTime expiresAt = currentAt.plusSeconds(JwtUtil.REFRESH_TOKEN_EXPIRATION / 1000);
            refreshTokenRepository.save(userId, refreshToken, expiresAt);

            return LoginResult.of(accessToken, refreshToken);
        } catch (AuthenticationException e) {
            log.error("login error", e);
            throw new CustomException(AuthErrorCode.INVALID_INPUT_VALUE);
        }
    }

    @Override
    @Transactional
    public RefreshResult refresh(String refreshToken) {
        if (!jwtService.validateRefreshToken(refreshToken)) {
            throw new CustomException(AuthErrorCode.INVALID_REFRESH_TOKEN);
        }

        RefreshToken storedToken = refreshTokenRepository.getRefreshToken(refreshToken);
        LocalDateTime currentAt = timeHolder.localDateTime();

        if (storedToken.isExpired(currentAt)) {
            refreshTokenRepository.deleteByUserId(storedToken.getUserId());
            throw new CustomException(AuthErrorCode.EXPIRED_REFRESH_TOKEN);
        }

        Long userId = jwtService.extractUserId(refreshToken);
        String email = jwtService.extractUsername(refreshToken);

        String newAccessToken = jwtUtil.createAccessToken(userId, email);

        return RefreshResult.of(newAccessToken);
    }

    @Override
    @Transactional
    public void sendVerificationCode(String email) {
        EmailVerificationPolicy.validateEmailFormat(email);
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
