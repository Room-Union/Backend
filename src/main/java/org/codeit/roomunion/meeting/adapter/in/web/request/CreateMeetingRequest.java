package org.codeit.roomunion.meeting.adapter.in.web.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import org.codeit.roomunion.meeting.domain.command.MeetingCreateCommand;
import org.codeit.roomunion.meeting.domain.model.MeetingCategory;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Schema(title = "CreateMeetingRequest : Meeting 생성 요청 DTO")
public class CreateMeetingRequest {

    @NotBlank(message = "모임명은 필수입니다.")
    @Size(max = 50)
    @Schema(example = "온라인 코딩 모임")
    private String name;

    @NotBlank(message = "모임 설명은 필수입니다.")
    @Size(max = 1000)
    @Schema(example = "온라인으로 코드 리뷰·스터디·프로젝트 협업을 진행합니다.")
    private String description;

    @NotNull(message = "모임 카테고리 선택은 필수입니다.")
    @Schema(example = "GAME")
    private MeetingCategory category;

    @Min(value = 1, message = "최대 참여자 수는 1 이상이어야 합니다.")
    @Schema(example = "10")
    private int maxMemberCount;

    @NotEmpty(message = "플랫폼 URL은 최소 1개 이상 입력해야 합니다.")
    @Schema(example = "[\"https://zoom.us/12345\", \"https://discord.gg/abcde\"]")
    private List<String> platformURL;

    public MeetingCreateCommand toCommand(Long userId, String hostEmail) {
        return MeetingCreateCommand.builder()
            .name(this.name)
            .description(this.description)
            .category(this.category)
            .maxMemberCount(this.maxMemberCount)
            .platformURL(this.platformURL)
            .hostEmail(hostEmail)
            .userId(userId)
            .createdAt(LocalDateTime.now())
            .build();
    }


}
