package org.codeit.roomunion.common.advice.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(title = "ErrorResponse DTO", description = "공통 API 응답 형식")
public class ErrorResponse {

    @Schema(description = "에러 코드", example = "INVALID_INPUT_VALUE")
    private String code;

    @Schema(description = "응답 메시지", example = "요청이 실패하였습니다.")
    private String message;

    public static ErrorResponse error(String code, String message) {
        return new ErrorResponse(code, message);
    }
}