package org.codeit.roomunion.common.advice;

import lombok.extern.slf4j.Slf4j;
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> f2440ea (:sparkles: 전역 예외 처리 및 모임 생성 기능, 특정 모임 조회 기능 구현 (#9))
import org.codeit.roomunion.common.advice.response.BaseResponse;
=======
import org.codeit.roomunion.common.advice.response.ErrorResponse;
>>>>>>> 351834c (feat: 회원가입 이메일 검증 로직 개발 (이메일 코드 발송, 이메일 코드 연장, 이메일 코드 검증) (#11))
import org.codeit.roomunion.common.exception.BaseErrorCode;
import org.codeit.roomunion.common.exception.CustomException;
import org.codeit.roomunion.common.exception.GlobalErrorCode;
<<<<<<< HEAD
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

=======
import org.codeit.roomunion.common.advice.response.ErrorResponse;
import org.codeit.roomunion.common.exception.BusinessException;
import org.codeit.roomunion.common.exception.ErrorCode;
=======
>>>>>>> f2440ea (:sparkles: 전역 예외 처리 및 모임 생성 기능, 특정 모임 조회 기능 구현 (#9))
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

<<<<<<< HEAD
>>>>>>> 8abfdd5 (feat: 스프링 시큐리티 개발 (#3))
=======
import java.util.stream.Collectors;

>>>>>>> f2440ea (:sparkles: 전역 예외 처리 및 모임 생성 기능, 특정 모임 조회 기능 구현 (#9))
@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> f2440ea (:sparkles: 전역 예외 처리 및 모임 생성 기능, 특정 모임 조회 기능 구현 (#9))
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        BaseErrorCode errorCode = e.getErrorCode();
        log.error("CustomException: {}", e.getMessage());
        return ResponseEntity.status(errorCode.getStatus())
<<<<<<< HEAD
            .body(BaseResponse.error(errorCode.getStatusValue(), e.getMessage()));
<<<<<<< HEAD
=======
            .body(ErrorResponse.error(errorCode.getCode(), e.getMessage()));
>>>>>>> 351834c (feat: 회원가입 이메일 검증 로직 개발 (이메일 코드 발송, 이메일 코드 연장, 이메일 코드 검증) (#11))
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
=======
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        log.error("Exception: ", e);
        return ResponseEntity.status(errorCode.getStatusCode())
                .body(ErrorResponse.from(errorCode));
=======
>>>>>>> f2440ea (:sparkles: 전역 예외 처리 및 모임 생성 기능, 특정 모임 조회 기능 구현 (#9))
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<Object>> handleValidationException(MethodArgumentNotValidException e) {
        BaseErrorCode errorCode = GlobalErrorCode.INVALID_INPUT_VALUE;
        String errorMessages = getValidationErrorMessage(e);
        log.error("Validation 오류 발생: {}", errorMessages);
        return ResponseEntity.status(errorCode.getStatus())
                .body(BaseResponse.error(errorCode.getStatusValue(), errorMessages));
    }

<<<<<<< HEAD
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.error(e.getMessage());
        return ResponseEntity.status(errorCode.getStatusCode())
                .body(ErrorResponse.from(errorCode));
>>>>>>> 8abfdd5 (feat: 스프링 시큐리티 개발 (#3))
=======
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<BaseResponse<Object>> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        BaseErrorCode errorCode = GlobalErrorCode.INVALID_INPUT_VALUE;
        String detail = "필수 파라미터 누락: " + e.getParameterName();
        log.error("필수 요청 파라미터 누락: {}", detail);
        return ResponseEntity.status(errorCode.getStatus())
                .body(BaseResponse.error(errorCode.getStatusValue(), detail));
>>>>>>> f2440ea (:sparkles: 전역 예외 처리 및 모임 생성 기능, 특정 모임 조회 기능 구현 (#9))
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

    private static String getValidationErrorMessage(MethodArgumentNotValidException e) {
        return e.getBindingResult().getFieldErrors().stream()
            .map(ex -> String.format("[%s] %s", ex.getField(), ex.getDefaultMessage()))
            .collect(Collectors.joining(" / "));
    }
}
