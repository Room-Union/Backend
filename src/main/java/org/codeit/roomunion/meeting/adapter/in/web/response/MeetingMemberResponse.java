package org.codeit.roomunion.meeting.adapter.in.web.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.codeit.roomunion.meeting.adapter.out.persistence.entity.MeetingMemberEntity;
import org.codeit.roomunion.meeting.domain.model.MeetingCategory;
import org.codeit.roomunion.user.domain.model.Gender;
import org.codeit.roomunion.user.domain.model.User;

@Getter
@Builder
@Schema(title = "MeetingResponse : 모임 멤버 응답 DTO")
public class MeetingMemberResponse {

    @Schema(description = "프로필 이미지", example = "https://profileImage")
    private String profileImage;

    @Schema(description = "닉네임", example = "비쿠")
    private String nickname;

    @Schema(description = "성별", example = "MALE")
    private Gender gender;

    @Schema(description = "모임 카테고리", example = "GAME")
    private MeetingCategory category;

    @Schema(description = "모임장 여부", example = "true")
    private boolean isOwner;

    public static MeetingMemberResponse from(MeetingMemberEntity meetingMember, String profileImageUrl) {

        User user = meetingMember.getUser().toDomain();

        return MeetingMemberResponse.builder()
            .profileImage(profileImageUrl)
            .nickname(user.getNickname())
            .gender(user.getGender())
            .category(meetingMember.getMeeting().getCategory())
            .isOwner(meetingMember.isHost())
            .build();
    }

}
