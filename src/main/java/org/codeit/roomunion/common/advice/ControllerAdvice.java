package org.codeit.roomunion.common.advice;

import lombok.extern.slf4j.Slf4j;
import org.codeit.roomunion.common.advice.response.ErrorResponse;
import org.codeit.roomunion.common.exception.BusinessException;
import org.codeit.roomunion.common.exception.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        log.error("Exception: ", e);
        return ResponseEntity.status(errorCode.getStatusCode())
                .body(ErrorResponse.from(errorCode));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e) {
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        log.error("RuntimeException: ", e);
        return ResponseEntity.status(errorCode.getStatusCode())
                .body(ErrorResponse.from(errorCode));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.error(e.getMessage());
        return ResponseEntity.status(errorCode.getStatusCode())
                .body(ErrorResponse.from(errorCode));
    }
}
