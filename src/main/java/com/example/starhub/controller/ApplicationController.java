package com.example.starhub.controller;

import com.example.starhub.controller.docs.ApplicationControllerDocs;
import com.example.starhub.dto.request.ApplicationRequestDto;
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

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/posts/{postId}")
@RequiredArgsConstructor
public class ApplicationController implements ApplicationControllerDocs {

    private final ApplicationService applicationService;

    /**
     * 지원서 작성
     */
    @PostMapping("/applications")
    public ResponseEntity<ResponseDto<ApplicationResponseDto>> createApplication(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long postId,
            @Valid @RequestBody ApplicationRequestDto applicationRequestDto) {

        ApplicationResponseDto res = applicationService.createApplication(customUserDetails.getUsername(), postId, applicationRequestDto);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_CREATE_APPLICANT.getStatus().value())
                .body(new ResponseDto<>(ResponseCode.SUCCESS_CREATE_APPLICANT, res));
    }

    /**
     * 지원서 목록 불러오기
     */
    @GetMapping("/applications")
    public ResponseEntity<ResponseDto<List<ApplicationResponseDto>>> getApplicationList(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long postId) {

        List<ApplicationResponseDto> res = applicationService.getApplicationList(customUserDetails.getUsername(), postId);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_GET_APPLICANT_LIST.getStatus().value())
                .body(new ResponseDto<>(ResponseCode.SUCCESS_GET_APPLICANT_LIST, res));
    }


    /**
     * 지원서 상세 불러오기
     */
    @GetMapping("/applications/{applicationId}")
    public ResponseEntity<ResponseDto<ApplicationResponseDto>> getApplicationDetail(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long postId,
            @PathVariable Long applicationId) {

        ApplicationResponseDto res = applicationService.getApplicationDetail(customUserDetails.getUsername(), postId, applicationId);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_GET_APPLICANT_DETAIL.getStatus().value())
                .body(new ResponseDto<>(ResponseCode.SUCCESS_GET_APPLICANT_DETAIL, res));
    }

    /**
     * 지원서 수정하기
     */
    @PatchMapping("/applications/{applicationId}")
    public ResponseEntity<ResponseDto<ApplicationResponseDto>> updateApplication(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long postId,
            @PathVariable Long applicationId,
            @Valid @RequestBody ApplicationRequestDto applicationRequestDto) {

        ApplicationResponseDto res = applicationService.updateApplication(customUserDetails.getUsername(), postId, applicationId, applicationRequestDto);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_UPDATE_APPLICANT.getStatus().value())
                .body(new ResponseDto<>(ResponseCode.SUCCESS_UPDATE_APPLICANT, res));
    }

    /**
     * 지원서 삭제하기
     */
    @DeleteMapping("/applications/{applicationId}")
    public ResponseEntity<ResponseDto> deleteApplication(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long postId,
            @PathVariable Long applicationId) {

        applicationService.deleteApplication(customUserDetails.getUsername(), postId, applicationId);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_DELETE_APPLICANT.getStatus().value())
                .body(new ResponseDto<>(ResponseCode.SUCCESS_DELETE_APPLICANT, null));
    }
}
