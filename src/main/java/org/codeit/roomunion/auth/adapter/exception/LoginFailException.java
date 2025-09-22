package org.codeit.roomunion.auth.adapter.exception;

public class LoginFailException extends BusinessException{
    public LoginFailException(String message) {
        super(message, ErrorCode.LOGIN_FAIL);
    }
}
