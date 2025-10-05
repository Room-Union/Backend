package org.codeit.roomunion.auth.application.service;

import org.codeit.roomunion.auth.application.port.in.AuthUseCase;
import org.codeit.roomunion.auth.domain.event.EmailVerificationCodeEvent;
import org.codeit.roomunion.auth.domain.policy.EmailVerificationPolicy;
import org.codeit.roomunion.common.application.port.out.EventPublisher;
import org.codeit.roomunion.common.application.port.out.RandomNumberGenerator;
import org.codeit.roomunion.common.application.port.out.TimeHolder;
import org.codeit.roomunion.user.application.port.in.UserCommandUseCase;
import org.codeit.roomunion.user.application.port.in.UserQueryUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
public class AuthService implements AuthUseCase {

    private static final int VERIFICATION_CODE_BOUND = 1000000;
    private static final String ROOM_UNION_EMAIL_VARIFICATION_SUBJECT = "RoomUnion 회원가입 인증 코드";
    private static final String ROOM_UNION_EMAIL_VARIFICATION_BODY = "인증 코드: %s";

    private final UserQueryUseCase userQueryUseCase;
    private final UserCommandUseCase userCommandUseCase;
    private final RandomNumberGenerator randomNumberGenerator;
    private final TimeHolder timeHolder;
    private final EventPublisher eventPublisher;

    public AuthService(UserQueryUseCase userQueryUseCase, UserCommandUseCase userCommandUseCase, RandomNumberGenerator randomNumberGenerator, TimeHolder timeHolder, EventPublisher eventPublisher) {
        this.userQueryUseCase = userQueryUseCase;
        this.userCommandUseCase = userCommandUseCase;
        this.randomNumberGenerator = randomNumberGenerator;
        this.timeHolder = timeHolder;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public void sendVerificationCode(String email) {
        userQueryUseCase.validateEmailExists(email);

        LocalDateTime currentAt = timeHolder.localDateTime();
        LocalDateTime expirationAt = EmailVerificationPolicy.calculateExpirationAt(currentAt);
        String code = generateVerifyCode();

        userCommandUseCase.saveEmailVerificationCode(email, code, currentAt, expirationAt);

        EmailVerificationCodeEvent emailVerificationCodeEvent = EmailVerificationCodeEvent.of(email, code, ROOM_UNION_EMAIL_VARIFICATION_SUBJECT, ROOM_UNION_EMAIL_VARIFICATION_BODY.formatted(code));
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
