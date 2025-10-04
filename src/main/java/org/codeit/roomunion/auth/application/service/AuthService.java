package org.codeit.roomunion.auth.application.service;

import org.codeit.roomunion.auth.application.port.in.AuthUseCase;
import org.codeit.roomunion.auth.domain.policy.EmailVerificationPolicy;
import org.codeit.roomunion.common.application.port.out.EmailSender;
import org.codeit.roomunion.common.application.port.out.RandomNumberGenerator;
import org.codeit.roomunion.common.application.port.out.TimeHolder;
import org.codeit.roomunion.user.application.port.in.UserQueryUseCase;
import org.codeit.roomunion.user.application.port.out.UserRepository;
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
    private final UserRepository userRepository;
    private final EmailSender emailSender;
    private final RandomNumberGenerator randomNumberGenerator;
    private final TimeHolder timeHolder;

    public AuthService(UserQueryUseCase userQueryUseCase, UserRepository userRepository, EmailSender emailSender, RandomNumberGenerator randomNumberGenerator, TimeHolder timeHolder) {
        this.userQueryUseCase = userQueryUseCase;
        this.userRepository = userRepository;
        this.emailSender = emailSender;
        this.randomNumberGenerator = randomNumberGenerator;
        this.timeHolder = timeHolder;
    }

    @Override
    @Transactional
    public void sendVerificationCode(String email) {
        userQueryUseCase.validateEmailExists(email);

        LocalDateTime currentAt = timeHolder.localDateTime();
        LocalDateTime expirationAt = EmailVerificationPolicy.calculateExpirationAt(currentAt);
        String code = generateVerifyCode();

        userRepository.saveEmailVerificationCode(email, code, currentAt, expirationAt);
        emailSender.send(email, ROOM_UNION_EMAIL_VARIFICATION_SUBJECT, ROOM_UNION_EMAIL_VARIFICATION_BODY.formatted(code));
    }

    private String generateVerifyCode() {
        int randomNumber = randomNumberGenerator.generate(VERIFICATION_CODE_BOUND);
        return String.format("%06d", randomNumber);
    }

}
