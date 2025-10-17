package org.codeit.roomunion.meeting.adapter.in.web.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.codeit.roomunion.meeting.domain.model.command.MeetingUpdateCommand;
import org.codeit.roomunion.meeting.domain.model.enums.MeetingCategory;

import java.util.List;

@Getter
@Schema(title = "UpdateMeetingRequest : Meeting 수정 요청 DTO")
public class UpdateMeetingRequest {

    @Size(max = 50)
    @Schema(example = "온라인 게임 모임")
    private String name;

    @Size(max = 1000)
    @Schema(example = "온라인으로 같이 게임하는 모임입니다.")
    private String description;

    @Schema(example = "GAME")
    private MeetingCategory category;

    @Min(value = 1, message = "최대 참여자 수는 1 이상이어야 합니다.")
    @Schema(example = "80")
    private Integer maxMemberCount;

    @Schema(example = "[\"https://zoom.us/12345\", \"https://discord.gg/abcde\"]")
    private List<String> platformURL;

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
