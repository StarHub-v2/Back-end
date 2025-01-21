package com.example.starhub.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LikeDto {

    private Long likeCount; // 좋아요 수
    private Boolean isLiked; // 좋아요 여부
}
