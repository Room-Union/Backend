package org.codeit.roomunion.user.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.codeit.roomunion.common.exception.BaseErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserErrorCode implements BaseErrorCode {

    ALREADY_REGISTERED_EMAIL("이미 가입된 이메일", HttpStatus.BAD_REQUEST),
    ALREADY_REGISTERED_NICKNAME("이미 사용중인 닉네임", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND("사용자를 찾을 수 없음", HttpStatus.NOT_FOUND),

    EMAIL_VALIDATION_NOT_FOUND("이메일 인증 내역 없음", HttpStatus.BAD_REQUEST),
    ALREADY_VERIFIED_EMAIL("이미 인증된 이메일", HttpStatus.BAD_REQUEST),
    EXPIRED_CODE("만료된 코드", HttpStatus.BAD_REQUEST),
    INVALID_CODE("유효하지 않은 코드", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD("일치하지 않는 비밀번호", HttpStatus.FORBIDDEN),
    SAME_PASSWORD("같은 비밀번호 입력", HttpStatus.BAD_REQUEST),
    ;

    private final String message;

    private final HttpStatus status;

}
