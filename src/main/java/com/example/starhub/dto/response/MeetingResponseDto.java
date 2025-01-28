package com.example.starhub.dto.response;

import com.example.starhub.entity.MeetingEntity;
import com.example.starhub.entity.UserEntity;
import com.example.starhub.entity.enums.Duration;
import com.example.starhub.entity.enums.RecruitmentType;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class MeetingResponseDto {

    private Long id;
    private RecruitmentType recruitmentType;
    private Integer maxParticipants;
    private Duration duration;
    private LocalDate endDate;
    private String location;
    private Double latitude;
    private Double longitude;
    private String title;
    private String description;
    private String goal;
    private String otherInfo;
    private Boolean isConfirmed;
    private CreatorDto creator;
    private List<String> techStacks;

    /**
     * 포스트 관련 정보
     * - PostEntity로부터 PostResponseDto를 생성합니다.
     *
     * @param meetingEntity 포스트 엔티티
     * @param techStacks 기술 스택 이름 목록
     * @return 생성된 PostResponseDto
     */
    public static MeetingResponseDto fromEntity(MeetingEntity meetingEntity, List<String> techStacks) {
        UserEntity creator = meetingEntity.getCreator();
        CreatorDto creatorDto = CreatorDto.builder()
                .nickname(creator.getNickname())
                .build();

        return MeetingResponseDto.builder()
                .id(meetingEntity.getId())
                .recruitmentType(meetingEntity.getRecruitmentType())
                .maxParticipants(meetingEntity.getMaxParticipants())
                .duration(meetingEntity.getDuration())
                .endDate(meetingEntity.getEndDate())
                .location(meetingEntity.getLocation())
                .latitude(meetingEntity.getLatitude())
                .longitude(meetingEntity.getLongitude())
                .title(meetingEntity.getTitle())
                .description(meetingEntity.getDescription())
                .goal(meetingEntity.getGoal())
                .otherInfo(meetingEntity.getOtherInfo())
                .isConfirmed(meetingEntity.getIsConfirmed())
                .creator(creatorDto)
                .techStacks(techStacks)
                .build();
    }

    @Getter
    @Builder
    public static class CreatorDto {
        private String nickname; // 개설자 닉네임
    }
}
