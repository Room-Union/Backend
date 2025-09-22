package org.codeit.roomunion.auth.adapter.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // 400
    INVALID_INPUT_VALUE(40001, "입력값이 잘못되었습니다."),

    // 403
    LOGIN_FAIL(403001, "로그인에 실패했습니다."),

    // 500
    INTERNAL_SERVER_ERROR(50001, "Internal Server Error"),
    ;

    private final int code;
    private final String message;

    public int getStatusCode() {
        return code / 100;
    }
}
