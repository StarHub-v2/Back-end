package com.example.starhub.controller;

import com.example.starhub.response.code.ResponseCode;
import com.example.starhub.response.dto.ResponseDto;
import com.example.starhub.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    /**
     * 포스트(스터디 및 프로젝트) 업로드
     */
    @PostMapping("/posts")
    public ResponseEntity<ResponseDto> createPost() {
        return ResponseEntity
                .status(ResponseCode.SUCCESS_CREATE_POST.getStatus().value())
                .body(new ResponseDto<>(ResponseCode.SUCCESS_CREATE_POST, null));
    }

    /**
     * 포스트 목록 불러오기 (메인 화면에 쓰일 API)
     */
    @GetMapping("/posts")
    public ResponseEntity<ResponseDto> getPostList() {
        return ResponseEntity
                .status(ResponseCode.SUCCESS_GET_POST_LIST.getStatus().value())
                .body(new ResponseDto<>(ResponseCode.SUCCESS_GET_POST_LIST, null));
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
