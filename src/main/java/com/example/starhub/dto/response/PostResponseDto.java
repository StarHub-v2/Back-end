package com.example.starhub.dto.response;

import com.example.starhub.entity.PostEntity;
import com.example.starhub.entity.enums.Duration;
import com.example.starhub.entity.enums.RecruitmentType;
import com.example.starhub.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    public static PostResponseDto fromEntity(PostEntity postEntity, List<String> techStackNames) {
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
                .techStacks(techStackNames)
                .build();
    }
}
