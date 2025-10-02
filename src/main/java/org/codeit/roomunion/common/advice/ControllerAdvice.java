package org.codeit.roomunion.common.advice;

import lombok.extern.slf4j.Slf4j;
import org.codeit.roomunion.common.advice.response.BaseResponse;
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
    public ResponseEntity<BaseResponse<Object>> handleCustomException(CustomException e) {
        BaseErrorCode errorCode = e.getErrorCode();
        log.error("CustomException: {}", e.getMessage());
        return ResponseEntity.status(errorCode.getStatus())
            .body(BaseResponse.error(errorCode.getStatus().value(), e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<Object>> handleValidationException(MethodArgumentNotValidException e) {
        BaseErrorCode errorCode = GlobalErrorCode.INVALID_INPUT_VALUE;
        String errorMessages = e.getBindingResult().getFieldErrors().stream()
            .map(ex -> String.format("[%s] %s", ex.getField(), ex.getDefaultMessage()))
            .collect(Collectors.joining(" / "));
        log.error("Validation 오류 발생: {}", errorMessages);
        return ResponseEntity.status(errorCode.getStatus()).body(BaseResponse.error(errorCode.getStatus().value(), errorMessages));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<BaseResponse<Object>> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        BaseErrorCode errorCode = GlobalErrorCode.INVALID_INPUT_VALUE;
        String detail = "필수 파라미터 누락: " + e.getParameterName();
        log.error("필수 요청 파라미터 누락: {}", detail);
        return ResponseEntity.status(errorCode.getStatus()).body(BaseResponse.error(errorCode.getStatus().value(), detail));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<BaseResponse<Object>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        BaseErrorCode errorCode = GlobalErrorCode.INVALID_INPUT_VALUE;
        log.error("HttpMessageNotReadableException: ", e);
        return ResponseEntity.status(errorCode.getStatus())
            .body(BaseResponse.error(400, "요청 형식이 잘못되었습니다. JSON을 확인해주세요."));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<Object>> handleException(Exception e) {
        BaseErrorCode errorCode = GlobalErrorCode.INTERNAL_SERVER_ERROR;
        log.error("Server 오류 발생: ", e);
        return ResponseEntity.status(errorCode.getStatus())
            .body(BaseResponse.error(500, "서버 오류가 발생했습니다."));
    }
}
