package com.example.starhub.dto.response;

import com.example.starhub.entity.MeetingEntity;
import com.example.starhub.entity.enums.Duration;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
public class MeetingSummaryResponseDto {

    private Long id;
    private String title;
    private Integer maxParticipants;
    private Duration duration;
    private LocalDate endDate;
    private List<String> techStacks;  // 기술 스택 목록
    private String location;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private LikeDto likeDto; // 좋아요 관련 정보

    /**
     * 요약된 포스트 정보
     * - PostEntity로부터 PostSummaryResponseDto를 생성합니다.
     * @param meetingEntity 포스트 엔티티
     * @param techStacks 기술 스택 이름 목록
     * @param likeDto 좋아요 관련 정보
     * @return 생성된 PostSummaryResponseDto
     */
    public static MeetingSummaryResponseDto fromEntity(MeetingEntity meetingEntity, List<String> techStacks, LikeDto likeDto) {
        return MeetingSummaryResponseDto.builder()
                .id(meetingEntity.getId())
                .title(meetingEntity.getTitle())
                .maxParticipants(meetingEntity.getMaxParticipants())
                .duration(meetingEntity.getDuration())
                .endDate(meetingEntity.getEndDate())
                .techStacks(techStacks)
                .location(meetingEntity.getLocation())
                .latitude(meetingEntity.getLatitude())
                .longitude(meetingEntity.getLongitude())
                .likeDto(likeDto)
                .build();
    }
}
