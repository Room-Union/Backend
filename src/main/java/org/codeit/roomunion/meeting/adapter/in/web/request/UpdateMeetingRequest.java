package org.codeit.roomunion.meeting.adapter.in.web.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.codeit.roomunion.meeting.domain.command.MeetingUpdateCommand;

import java.util.List;
import org.codeit.roomunion.meeting.domain.model.MeetingCategory;

@Getter
@Schema(title = "UpdateMeetingRequest : Meeting 수정 요청 DTO")
public class UpdateMeetingRequest {

    @NotBlank(message = "모임명은 필수입니다.")
    @Size(max = 50)
    @Schema(example = "온라인 게임 모임")
    private String name;

    @NotBlank(message = "모임 설명은 필수입니다.")
    @Size(max = 1000)
    @Schema(example = "온라인으로 같이 게임하는 모임입니다.")
    private String description;

    @NotNull(message = "모임 카테고리 선택은 필수입니다.")
    @Schema(example = "GAME")
    private MeetingCategory category;

    @Min(value = 1, message = "최대 참여자 수는 1 이상이어야 합니다.")
    @Schema(example = "80")
    private Integer maxMemberCount;

    @NotEmpty(message = "플랫폼 URL은 최소 1개 이상 입력해야 합니다.")
    @Schema(example = "[\"https://zoom.us/12345\", \"https://discord.gg/abcde\"]")
    private List<@NotBlank String> platformURL;

    public MeetingUpdateCommand toCommand() {
        return MeetingUpdateCommand.builder()
            .name(this.name)
            .description(this.description)
            .category(this.category)
            .maxMemberCount(this.maxMemberCount)
            .platformURL(this.platformURL)
            .build();
    }
}
