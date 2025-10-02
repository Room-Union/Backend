package org.codeit.roomunion.crew.adapter.in.web.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.codeit.roomunion.crew.domain.model.command.CrewCreateCommand;
import org.codeit.roomunion.crew.domain.model.enums.CrewCategory;

import java.util.List;

@Getter
@Setter
@Builder
@Schema(title = "CreateCrewRequest : Crew 생성 요청 DTO")
@NoArgsConstructor
@AllArgsConstructor
public class CreateCrewRequest {

    @NotBlank(message = "모임명은 필수입니다.")
    @Size(max = 50)
    @Schema(example = "온라인 코딩 모임")
    private String name;

    @NotBlank(message = "모임 설명은 필수입니다.")
    @Size(max = 1000)
    @Schema(example = "온라인으로 코드 리뷰·스터디·프로젝트 협업을 진행합니다.")
    private String description;

    @NotBlank(message = "모임 카테고리 선택은 필수입니다.")
    @Schema(example = "GAME")
    private CrewCategory category;

    @Min(value = 1, message = "최대 참여자 수는 1 이상이어야 합니다.")
    @Schema(example = "10")
    private int maxMemberCount;

    @NotNull(message = "플랫폼 URL은 최소 1개 이상 입력해야 합니다.")
    @Schema(example = "[\"https://zoom.us/12345\", \"https://discord.gg/abcde\"]")
    private List<String> platformURL;

    public CrewCreateCommand toCommand(Long userId) {
        return CrewCreateCommand.builder()
            .name(this.name)
            .description(this.description)
            .category(this.category)
            .maxMemberCount(this.maxMemberCount)
            .platformURL(this.platformURL)
            .userId(userId)
            .build();
    }


}
