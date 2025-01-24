package com.example.starhub.controller.docs;

import com.example.starhub.dto.security.CustomUserDetails;
import com.example.starhub.response.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "LikeController", description = "좋아요 관련 API")
public interface LikeControllerDocs {

    /**
     * 좋아요 생성하기
     */
    @Operation(
            summary = "좋아요 생성하기",
            description = "좋아요 생성하기를 진행합니다."
    )
    ResponseEntity<ResponseDto> createLike(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long meetingId);

    /**
     * 좋아요 삭제하기
     */
    @Operation(
            summary = "좋아요 삭제하기",
            description = "좋아요 삭제하기를 진행합니다."
    )
    ResponseEntity<ResponseDto> deleteLike(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long meetingId);

}
