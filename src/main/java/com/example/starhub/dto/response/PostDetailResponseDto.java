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
    private PostResponseDto postInfo; // post 관련 정보
    private LikeDto likeDto; // 좋아요 관련 정보

    /**
     * 포스트 상세 정보
     * - PostEntity로부터 PostDetailResponseDto를 생성합니다.
     *
     * @param isCreator 현재 사용자가 포스트 개설자인지 여부
     * @param applicationStatus 현재 사용자의 지원 상태
     * @param postEntity 포스트 엔티티
     * @param techStacks 기술 스택 이름 목록
     * @param likeDto 좋아요 정보
     * @return 생성된 PostDetailResponseDto
     */
    public static PostDetailResponseDto fromEntity(Boolean isCreator, Boolean applicationStatus, PostEntity postEntity, List<String> techStacks, LikeDto likeDto) {

        // PostResponseDto를 메서드 호출로 간단하게 가져오기
        PostResponseDto postResponseDto = PostResponseDto.fromEntity(postEntity, techStacks);

        return PostDetailResponseDto.builder()
                .isCreator(isCreator)
                .applicationStatus(applicationStatus)
                .postInfo(postResponseDto)
                .likeDto(likeDto)
                .build();
    }
}
