package com.example.starhub.dto.response;

import com.example.starhub.entity.PostEntity;
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
public class PostResponseDto {

    private Long id;
    private RecruitmentType recruitmentType;
    private Integer maxParticipants;
    private Duration duration;
    private LocalDate endDate;
    private String location;
    private BigDecimal latitude;
    private BigDecimal longitude;
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
     * @param postEntity 포스트 엔티티
     * @param techStacks 기술 스택 이름 목록
     * @return 생성된 PostResponseDto
     */
    public static PostResponseDto fromEntity(PostEntity postEntity, List<String> techStacks) {
        UserEntity creator = postEntity.getCreator();
        CreatorDto creatorDto = CreatorDto.builder()
                .username(creator.getUsername())
                .build();

        return PostResponseDto.builder()
                .id(postEntity.getId())
                .recruitmentType(postEntity.getRecruitmentType())
                .maxParticipants(postEntity.getMaxParticipants())
                .duration(postEntity.getDuration())
                .endDate(postEntity.getEndDate())
                .location(postEntity.getLocation())
                .latitude(postEntity.getLatitude())
                .longitude(postEntity.getLongitude())
                .title(postEntity.getTitle())
                .description(postEntity.getDescription())
                .goal(postEntity.getGoal())
                .otherInfo(postEntity.getOtherInfo())
                .isConfirmed(postEntity.getIsConfirmed())
                .creator(creatorDto)
                .techStacks(techStacks)
                .build();
    }
}
