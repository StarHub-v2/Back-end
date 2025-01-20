package com.example.starhub.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LikeDto {

    private Long likeCount;
    private Boolean isLiked;
}
