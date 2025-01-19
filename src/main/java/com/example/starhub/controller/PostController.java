package com.example.starhub.controller;

import com.example.starhub.controller.docs.PostControllerDocs;
import com.example.starhub.dto.request.CreatePostRequestDto;
import com.example.starhub.dto.response.PostResponseDto;
import com.example.starhub.dto.response.PostSummaryResponseDto;
import com.example.starhub.dto.security.CustomUserDetails;
import com.example.starhub.response.code.ResponseCode;
import com.example.starhub.response.dto.ResponseDto;
import com.example.starhub.service.PostService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PostController implements PostControllerDocs {

    private final PostService postService;

    /**
     * 포스트(스터디 및 프로젝트) 업로드
     */
    @PostMapping("/posts")
    public ResponseEntity<ResponseDto<PostResponseDto>> createPost(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Valid @RequestBody CreatePostRequestDto createPostRequestDto) {
        PostResponseDto res = postService.createPost(customUserDetails.getUsername(), createPostRequestDto);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_CREATE_POST.getStatus().value())
                .body(new ResponseDto<>(ResponseCode.SUCCESS_CREATE_POST, res));
    }

    /**
     * 포스트 목록 불러오기 (메인 화면에 쓰일 API)
     */
    @GetMapping("/posts")
    public ResponseEntity<ResponseDto> getPostList(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int size)
    {
        Page<PostSummaryResponseDto> res = postService.getPostList(customUserDetails.getUsername(), page, size);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_GET_POST_LIST.getStatus().value())
                .body(new ResponseDto<>(ResponseCode.SUCCESS_GET_POST_LIST, res));
    }

    /**
     * 포스트 상세 불러오기
     */
    @PostMapping("/posts/{id}")
    public ResponseEntity<ResponseDto> getPostDetail() {
        return ResponseEntity
                .status(ResponseCode.SUCCESS_GET_POST_DETAIL.getStatus().value())
                .body(new ResponseDto<>(ResponseCode.SUCCESS_GET_POST_DETAIL, null));
    }

    /**
     * 포스트 수정하기
     */
    @PatchMapping("/posts/{id}")
    public ResponseEntity<ResponseDto> updatePost() {
        return ResponseEntity
                .status(ResponseCode.SUCCESS_UPDATE_POST.getStatus().value())
                .body(new ResponseDto<>(ResponseCode.SUCCESS_UPDATE_POST, null));
    }

    /**
     * 포스트 삭제하기
     */
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<ResponseDto> deletePost() {
        return ResponseEntity
                .status(ResponseCode.SUCCESS_DELETE_POST.getStatus().value())
                .body(new ResponseDto<>(ResponseCode.SUCCESS_DELETE_POST, null));
    }

}
