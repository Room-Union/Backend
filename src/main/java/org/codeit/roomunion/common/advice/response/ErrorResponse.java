package org.codeit.roomunion.common.advice.response;

import org.codeit.roomunion.common.exception.ErrorCode;

public record ErrorResponse(int code, String message) {
    public static ErrorResponse from(ErrorCode errorCode) {
        return new ErrorResponse(errorCode.getCode(), errorCode.getMessage());
    }
}
