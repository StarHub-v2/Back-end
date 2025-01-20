package com.example.starhub.dto.response;

import com.example.starhub.entity.PostEntity;
import com.example.starhub.entity.enums.Duration;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
public class PostSummaryResponseDto {

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

    public static PostSummaryResponseDto fromEntity(PostEntity postEntity, List<String> techStacks, LikeDto likeDto) {
        return PostSummaryResponseDto.builder()
                .id(postEntity.getId())
                .title(postEntity.getTitle())
                .maxParticipants(postEntity.getMaxParticipants())
                .duration(postEntity.getDuration())
                .endDate(postEntity.getEndDate())
                .techStacks(techStacks)
                .location(postEntity.getLocation())
                .latitude(postEntity.getLatitude())
                .longitude(postEntity.getLongitude())
                .likeDto(likeDto)
                .build();
    }
}
