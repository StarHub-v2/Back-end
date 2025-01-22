package com.example.starhub.dto.response;

import com.example.starhub.entity.MeetingEntity;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class MeetingDetailResponseDto {

    private Boolean isApplicant; // 지원자 여부
    private Boolean applicationStatus; // 지원 여부 (지원자의 경우에만)
    private MeetingResponseDto postInfo; // post 관련 정보
    private LikeDto likeDto; // 좋아요 관련 정보

    /**
     * 포스트 상세 정보
     * - PostEntity로부터 PostDetailResponseDto를 생성합니다.
     *
     * @param isApplicant 현재 사용자가 포스트 개설자인지 여부
     * @param applicationStatus 현재 사용자의 지원 상태
     * @param meetingEntity 포스트 엔티티
     * @param techStacks 기술 스택 이름 목록
     * @param likeDto 좋아요 정보
     * @return 생성된 PostDetailResponseDto
     */
    public static MeetingDetailResponseDto fromEntity(Boolean isApplicant, Boolean applicationStatus, MeetingEntity meetingEntity, List<String> techStacks, LikeDto likeDto) {

        // PostResponseDto를 메서드 호출로 간단하게 가져오기
        MeetingResponseDto meetingResponseDto = MeetingResponseDto.fromEntity(meetingEntity, techStacks);

        return MeetingDetailResponseDto.builder()
                .isApplicant(isApplicant)
                .applicationStatus(applicationStatus)
                .postInfo(meetingResponseDto)
                .likeDto(likeDto)
                .build();
    }
}
