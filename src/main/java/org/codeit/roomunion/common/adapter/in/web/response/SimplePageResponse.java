package org.codeit.roomunion.common.adapter.in.web.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
@Schema(title = "SimplePageResponse : 간단한 페이지 응답 DTO", description = "페이지 번호와 크기, 데이터 리스트만 포함된 최소 페이징 응답 DTO")
public class SimplePageResponse<T> {

    @Schema(description = "응답 데이터 리스트", example = "[...]")
    private List<T> content;

    @Schema(description = "현재 페이지 번호(0부터 시작)", example = "0")
    private int page;

    @Schema(description = "페이지당 데이터 개수", example = "10")
    private int size;

    public static <T> SimplePageResponse<T> from(Page<T> page) {
        return SimplePageResponse.<T>builder()
            .content(page.getContent())
            .page(page.getNumber())
            .size(page.getSize())
            .build();
    }
}