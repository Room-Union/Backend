package org.codeit.roomunion.meeting.adapter.in.web.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.codeit.roomunion.meeting.domain.model.Meeting;
import org.codeit.roomunion.meeting.domain.model.MeetingBadge;
import org.codeit.roomunion.meeting.domain.model.MeetingCategory;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@Schema(title = "MeetingResponse : 모임 응답 DTO")
public class MeetingResponse {

    @Schema(description = "모임 ID", example = "1")
    private Long meetingId;

    @Schema(description = "모임명", example = "온라인 코딩 모임")
    private String name;

    @Schema(description = "모임 설명", example = "같이 개발하는 온라인 코딩 모임입니다.")
    private String description;

    @Schema(description = "모임 카테고리", example = "게임")
    private MeetingCategory category;

    @Schema(description = "최대 참여자 수", example = "10")
    private int maxMemberCount;

    @Schema(description = "현재 참여자 수", example = "5")
    private int currentMemberCount;

    @Schema(description = "현재 사용자의 해당 모임 참여 여부", example = "true")
    private boolean isJoined;

    @Schema(description = "모임 관련 플랫폼 URL", example = "[\"https://zoom.us/12345\", \"https://discord.gg/abcde\"]")
    private List<String> platformURL;

    @Schema(description = "모임 대표 이미지", example = "https://...")
    private String meetingImage;

    @Schema(description = "만든 사람 userId", example = "1")
    private Long userId;

    @Schema(description = "만든 사람 닉네임", example = "비쿠")
    private String nickname;

    @Schema(description = "모임 생성 시각")
    private LocalDateTime createdAt;

    @Schema(description = "뱃지 목록", example = "[\"RECRUITING\",\"NEW\"]")
    private List<MeetingBadge> badges;

    public static MeetingResponse from(Meeting meeting) {
        return MeetingResponse.builder()
            .meetingId(meeting.getId())
            .name(meeting.getName())
            .description(meeting.getDescription())
            .category(meeting.getCategory())
            .maxMemberCount(meeting.getMaxMemberCount())
            .currentMemberCount(meeting.getCurrentMemberCount())
            .isJoined(meeting.isJoined())
            .platformURL(meeting.getPlatformURL())
            .meetingImage(meeting.getMeetingImage())
            .userId(meeting.getHost().getId())
            .nickname(meeting.getHost().getNickname())
            .createdAt(meeting.getCreatedAt())
            .badges(meeting.getBadges())
            .build();
    }


}
