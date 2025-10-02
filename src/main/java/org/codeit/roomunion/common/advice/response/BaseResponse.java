package org.codeit.roomunion.common.advice.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "BaseResponse DTO", description = "공통 API 응답 형식")
public class BaseResponse<T> {

    @Schema(description = "HTTP 상태 코드", example = "200")
    private int code;

    @Schema(description = "응답 메시지", example = "요청이 실패하였습니다.")
    private String message;

    public static <T> BaseResponse<T> error(int code, String message) {
        return new BaseResponse<>(code, message);
    }
}
