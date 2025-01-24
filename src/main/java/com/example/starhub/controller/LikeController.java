package com.example.starhub.controller;

import com.example.starhub.dto.security.CustomUserDetails;
import com.example.starhub.response.code.ResponseCode;
import com.example.starhub.response.dto.ResponseDto;
import com.example.starhub.service.LikeService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/meetings/{meetingId}/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    /**
     * 좋아요 생성하기
     */
    @PostMapping
    public ResponseEntity<ResponseDto> createLike(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long meetingId) {

        return ResponseEntity
                .status(ResponseCode.SUCCESS_CREATE_LIKE.getStatus().value())
                .body(new ResponseDto<>(ResponseCode.SUCCESS_CREATE_LIKE, null));
    }

    /**
     * 좋아요 삭제하기
     */
    @DeleteMapping
    public ResponseEntity<ResponseDto> deleteLike(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long meetingId) {

        return ResponseEntity
                .status(ResponseCode.SUCCESS_DELETE_LIKE.getStatus().value())
                .body(new ResponseDto<>(ResponseCode.SUCCESS_DELETE_LIKE, null));
    }
}
