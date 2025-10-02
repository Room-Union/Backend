package org.codeit.roomunion.moim.adapter.in.web.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.codeit.roomunion.moim.domain.model.Crew;
import org.codeit.roomunion.moim.domain.model.enums.CrewCategory;
import org.codeit.roomunion.user.domain.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@Schema(title = "CrewResponse : 모임 응답 DTO")
public class CrewResponse {

    @Schema(description = "모임 ID", example = "1")
    private Long crewId;

    @Schema(description = "모임명", example = "온라인 코딩 모임")
    private String name;

    @Schema(description = "모임 설명", example = "같이 개발하는 온라인 코딩 모임입니다.")
    private String description;

    @Schema(description = "모임 카테고리", example = "게임")
    private CrewCategory category;

    @Schema(description = "최대 참여자 수", example = "10")
    private int maxMemberCount;

    @Schema(description = "현재 사용자의 해당 모임 참여 여부", example = "true")
    private boolean isJoined;

    @Schema(description = "모임 관련 플랫폼 URL", example = "[\"https://zoom.us/12345\", \"https://discord.gg/abcde\"]")
    private List<String> platformURL;

    @Schema(description = "모임 대표 이미지", example = "https://...")
    private String crewImage;

    @Schema(description = "만든 사람 userId", example = "1")
    private Long userId;

//    @Schema(description = "만든 사람 프로필 이미지", example = "https://...")
//    private String profileImage;

    @Schema(description = "만든 사람 닉네임", example = "비쿠")
    private String nickname;

    @Schema(description = "모임 생성 시각")
    private LocalDateTime createdAt;

    public static CrewResponse from(Crew crew, User host, boolean isJoined) {
        return CrewResponse.builder()
            .crewId(crew.getId())
            .name(crew.getName())
            .description(crew.getDescription())
            .category(crew.getCategory())
            .maxMemberCount(crew.getMaxMemberCount())
            .isJoined(isJoined)
            .platformURL(crew.getPlatformURL())
            .crewImage(crew.getCrewImage())
            .userId(host.getId())
//            .profileImage(host.getProfileImage())
            .nickname(host.getNickname())
            .createdAt(crew.getCreatedAt())
            .build();
    }


}
