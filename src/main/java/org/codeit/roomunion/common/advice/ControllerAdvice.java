package org.codeit.roomunion.common.advice;

import lombok.extern.slf4j.Slf4j;
import org.codeit.roomunion.common.advice.response.ErrorResponse;
import org.codeit.roomunion.common.exception.BaseErrorCode;
import org.codeit.roomunion.common.exception.CustomException;
import org.codeit.roomunion.common.exception.GlobalErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        BaseErrorCode errorCode = e.getErrorCode();
        log.error("CustomException: {}", e.getMessage());
        return ResponseEntity.status(errorCode.getStatus())
            .body(ErrorResponse.error(errorCode.getCode(), e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        BaseErrorCode errorCode = GlobalErrorCode.INVALID_INPUT_VALUE;
        String errorMessages = getValidationErrorMessage(e);
        log.error("Validation 오류 발생: {}", errorMessages);
        return ResponseEntity.status(errorCode.getStatus())
            .body(ErrorResponse.error(errorCode.getCode(), errorMessages));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        BaseErrorCode errorCode = GlobalErrorCode.INVALID_INPUT_VALUE;
        String detail = "필수 파라미터 누락: " + e.getParameterName();
        log.error("필수 요청 파라미터 누락: {}", detail);
        return ResponseEntity.status(errorCode.getStatus())
            .body(ErrorResponse.error(errorCode.getCode(), detail));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        BaseErrorCode errorCode = GlobalErrorCode.INVALID_INPUT_VALUE;
        log.error("HttpMessageNotReadableException: ", e);
        return ResponseEntity.status(errorCode.getStatus())
            .body(ErrorResponse.error(errorCode.getCode(), errorCode.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        BaseErrorCode errorCode = GlobalErrorCode.INTERNAL_SERVER_ERROR;
        log.error("Server 오류 발생: ", e);
        return ResponseEntity.status(errorCode.getStatus())
            .body(ErrorResponse.error(errorCode.getCode(), errorCode.getMessage()));
    }

    private static String getValidationErrorMessage(MethodArgumentNotValidException e) {
        return e.getBindingResult().getFieldErrors().stream()
            .map(ex -> String.format("[%s] %s", ex.getField(), ex.getDefaultMessage()))
            .collect(Collectors.joining(" / "));
    }
}