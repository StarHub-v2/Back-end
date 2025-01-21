package com.example.starhub.controller;

import com.example.starhub.dto.request.CreateApplicationRequestDto;
import com.example.starhub.dto.response.ApplicationResponseDto;
import com.example.starhub.dto.security.CustomUserDetails;
import com.example.starhub.response.code.ResponseCode;
import com.example.starhub.response.dto.ResponseDto;
import com.example.starhub.service.ApplicationService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts/{postId}")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    /**
     * 지원서 작성
     */
    @PostMapping("/applications")
    public ResponseEntity<ResponseDto> createApplication(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long postId,
            @RequestBody CreateApplicationRequestDto createApplicationRequestDto) {

        ApplicationResponseDto res = applicationService.createApplication(customUserDetails.getUsername(), postId, createApplicationRequestDto);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_CREATE_APPLICANT.getStatus().value())
                .body(new ResponseDto<>(ResponseCode.SUCCESS_CREATE_APPLICANT, res));
    }

    /**
     * 지원서 목록 불러오기
     */
    @GetMapping("/applications")
    public ResponseEntity<ResponseDto> getApplicationList() {
        return ResponseEntity
                .status(ResponseCode.SUCCESS_GET_APPLICANT_LIST.getStatus().value())
                .body(new ResponseDto<>(ResponseCode.SUCCESS_GET_APPLICANT_LIST, null));
    }


    /**
     * 지원서 상세 불러오기
     */
    @GetMapping("/applications/{applicationId}")
    public ResponseEntity<ResponseDto> getApplicationDetail() {
        return ResponseEntity
                .status(ResponseCode.SUCCESS_GET_APPLICANT_DETAIL.getStatus().value())
                .body(new ResponseDto<>(ResponseCode.SUCCESS_GET_APPLICANT_DETAIL, null));
    }

    /**
     * 지원서 수정하기
     */
    @PatchMapping("/applications/{applicationId}")
    public ResponseEntity<ResponseDto> updateApplication() {
        return ResponseEntity
                .status(ResponseCode.SUCCESS_UPDATE_APPLICANT.getStatus().value())
                .body(new ResponseDto<>(ResponseCode.SUCCESS_UPDATE_APPLICANT, null));
    }

    /**
     * 지원서 삭제하기
     */
    @DeleteMapping("/applications/{applicationId}")
    public ResponseEntity<ResponseDto> deleteApplication() {
        return ResponseEntity
                .status(ResponseCode.SUCCESS_DELETE_APPLICANT.getStatus().value())
                .body(new ResponseDto<>(ResponseCode.SUCCESS_DELETE_APPLICANT, null));
    }
}
