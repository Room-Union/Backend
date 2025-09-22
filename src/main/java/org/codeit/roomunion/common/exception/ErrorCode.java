package org.codeit.roomunion.common.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    // 400
    INVALID_INPUT_VALUE(40001, "입력값이 잘못되었습니다."),

    // 404
    USER_NOT_FOUND(40401, "유저를 찾을 수 없습니다."),

    // 403,
    LOGIN_FAIL(403001, "로그인에 실패했습니다."),
    INVALID_JWT_TOKEN(403002, "jwt 토큰이 유효하지 않습니다."),

    // 500,
    INTERNAL_SERVER_ERROR(50001, "Internal Server Error");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getStatusCode() {
        return code / 100;
    }
}
