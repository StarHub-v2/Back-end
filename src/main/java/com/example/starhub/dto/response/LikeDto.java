package com.example.starhub.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LikeDto {

    private Long likeCount; // 좋아요 수
    private Boolean isLiked; // 좋아요 여부
}
