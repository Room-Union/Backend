package org.codeit.roomunion.auth.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.codeit.roomunion.common.exception.BaseErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements BaseErrorCode {

    // 400
    INVALID_INPUT_VALUE("입력값이 잘못되었습니다.", HttpStatus.BAD_REQUEST),

    //401
    UNAUTHORIZED_TOKEN("토큰이 없거나 만료되었습니다.", HttpStatus.UNAUTHORIZED),


    // 403,
    LOGIN_FAIL("로그인에 실패했습니다.", HttpStatus.FORBIDDEN),

    INVALID_JWT_TOKEN("jwt 토큰이 유효하지 않습니다.", HttpStatus.FORBIDDEN),

    INVALID_JWT("잘못된 JWT 토큰입니다.", HttpStatus.FORBIDDEN),

    EXPIRED_JWT("만료된 JWT 토큰입니다.", HttpStatus.FORBIDDEN),

    MALFORMED_JWT("형식이 잘못된 JWT 토큰입니다.", HttpStatus.FORBIDDEN),

    INVALID_JWT_SIGNATURE("서명이 유효하지 않은 JWT 토큰입니다.", HttpStatus.FORBIDDEN),

    // Refresh Token
    INVALID_REFRESH_TOKEN("Refresh Token이 유효하지 않습니다.", HttpStatus.FORBIDDEN),
    EXPIRED_REFRESH_TOKEN("Refresh Token이 만료되었습니다.", HttpStatus.FORBIDDEN),
    REFRESH_TOKEN_NOT_FOUND("Refresh Token을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    // 404
    USER_NOT_FOUND("유저를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    // 500,
    INTERNAL_SERVER_ERROR("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR),
    ;

    private final String message;

    private final HttpStatus status;
}
