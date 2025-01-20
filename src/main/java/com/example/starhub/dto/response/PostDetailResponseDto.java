package com.example.starhub.dto.response;

import com.example.starhub.entity.PostEntity;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class PostDetailResponseDto {

    private Boolean isCreator; // "지원자" 또는 "개설자"
    private Boolean applicationStatus; // 지원 여부 (지원자의 경우에만)
    private PostResponseDto postInfo;
    private LikeDto likeDto;

    public static PostDetailResponseDto fromEntity(Boolean isCreator, Boolean applicationStatus, PostEntity postEntity, List<String> techStackNames, LikeDto likeDto) {

        // PostResponseDto를 메서드 호출로 간단하게 가져오기
        PostResponseDto postResponseDto = PostResponseDto.fromEntity(postEntity, techStackNames);

        return PostDetailResponseDto.builder()
                .isCreator(isCreator)
                .applicationStatus(applicationStatus)
                .postInfo(postResponseDto)
                .likeDto(likeDto)
                .build();
    }
}
