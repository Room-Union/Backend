package org.codeit.roomunion.common.advice;

import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e, HttpServletRequest request) {
        // SSE 요청인 경우 예외를 다시 던져서 SSE 연결이 종료되도록 함
        if (isSseRequest(request)) {
            log.error("SSE 요청 중 CustomException 발생: {}", e.getMessage());
            throw e;
        }
        
        BaseErrorCode errorCode = e.getErrorCode();
        log.error("CustomException: {}", e.getMessage());
        return ResponseEntity.status(errorCode.getStatus())
            .body(ErrorResponse.error(errorCode.getCode(), e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e, HttpServletRequest request) {
        // SSE 요청인 경우 예외를 다시 던져서 SSE 연결이 종료되도록 함
        if (isSseRequest(request)) {
            log.error("SSE 요청 중 Validation 오류 발생");
            throw new RuntimeException("Validation error in SSE request", e);
        }
        
        BaseErrorCode errorCode = GlobalErrorCode.INVALID_INPUT_VALUE;
        String errorMessages = getValidationErrorMessage(e);
        log.error("Validation 오류 발생: {}", errorMessages);
        return ResponseEntity.status(errorCode.getStatus())
            .body(ErrorResponse.error(errorCode.getCode(), errorMessages));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(MissingServletRequestParameterException e, HttpServletRequest request) {
        // SSE 요청인 경우 예외를 다시 던져서 SSE 연결이 종료되도록 함
        if (isSseRequest(request)) {
            log.error("SSE 요청 중 필수 파라미터 누락");
            throw new RuntimeException("Missing parameter in SSE request", e);
        }
        
        BaseErrorCode errorCode = GlobalErrorCode.INVALID_INPUT_VALUE;
        String detail = "필수 파라미터 누락: " + e.getParameterName();
        log.error("필수 요청 파라미터 누락: {}", detail);
        return ResponseEntity.status(errorCode.getStatus())
            .body(ErrorResponse.error(errorCode.getCode(), detail));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e, HttpServletRequest request) {
        // SSE 요청인 경우 예외를 다시 던져서 SSE 연결이 종료되도록 함
        if (isSseRequest(request)) {
            log.error("SSE 요청 중 HttpMessageNotReadableException 발생");
            throw new RuntimeException("Message not readable in SSE request", e);
        }
        
        BaseErrorCode errorCode = GlobalErrorCode.INVALID_INPUT_VALUE;
        log.error("HttpMessageNotReadableException: ", e);
        return ResponseEntity.status(errorCode.getStatus())
            .body(ErrorResponse.error(errorCode.getCode(), errorCode.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e, HttpServletRequest request) {
        // SSE 요청인 경우 예외를 다시 던져서 SSE 연결이 종료되도록 함
        if (isSseRequest(request)) {
            log.error("SSE 요청 중 예외 발생: ", e);
            throw new RuntimeException("Error in SSE request", e);
        }
        
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

    /**
     * SSE 요청인지 확인하는 헬퍼 메서드
     */
    private boolean isSseRequest(HttpServletRequest request) {
        String accept = request.getHeader("Accept");
        String requestUri = request.getRequestURI();
        
        // Accept 헤더에 text/event-stream이 포함되어 있거나
        // URL에 /sse/가 포함되어 있으면 SSE 요청으로 판단
        return (accept != null && accept.contains("text/event-stream")) 
            || (requestUri != null && requestUri.contains("/sse/"));
    }
}