package com.example.starhub.controller;


import com.example.starhub.dto.response.MeetingSummaryResponseDto;
import com.example.starhub.dto.security.CustomUserDetails;
import com.example.starhub.response.code.ResponseCode;
import com.example.starhub.response.dto.ResponseDto;
import com.example.starhub.service.PopularMeetingService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/meetings")
@RequiredArgsConstructor
public class PopularMeetingController {

    private final PopularMeetingService popularMeetingService;

    /**
     * 인기글 페이지 - 프로젝트 인기글 3개
     *
     */
    @GetMapping("/popular/projects")
    public ResponseEntity<ResponseDto<List<MeetingSummaryResponseDto>>> getPopularProjects(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        // 익명 사용자일 경우 null 전달, 인증된 사용자일 경우 customUserDetails 전달
        String username = customUserDetails != null ? customUserDetails.getUsername() : null;

        List<MeetingSummaryResponseDto> res = popularMeetingService.getPopularProjects(username);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_GET_POPULAR_PROJECTS.getStatus().value())
                .body(new ResponseDto<>(ResponseCode.SUCCESS_GET_POPULAR_PROJECTS, res));
    }

    /**
     * 인기글 페이지 - 스터디 인기글 3개
     */
    @GetMapping("/popular/studies")
    public ResponseEntity<ResponseDto<List<MeetingSummaryResponseDto>>> getPopularStudies(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        String username = customUserDetails != null ? customUserDetails.getUsername() : null;
        List<MeetingSummaryResponseDto> res = popularMeetingService.getPopularStudies(username);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_GET_POPULAR_STUDIES.getStatus().value())
                .body(new ResponseDto<>(ResponseCode.SUCCESS_GET_POPULAR_STUDIES, res));
    }

    /**
     * 인기글 페이지 - 마감입박 인기글 3개
     */
    @GetMapping("/popular/expiring")
    public ResponseEntity<ResponseDto> getExpiringPopularMeetings(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        String username = customUserDetails != null ? customUserDetails.getUsername() : null;
        List<MeetingSummaryResponseDto> res = popularMeetingService.getExpiringPopularMeetings(username);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_GET_POPULAR_EXPIRING.getStatus().value())
                .body(new ResponseDto<>(ResponseCode.SUCCESS_GET_POPULAR_EXPIRING, res));
    }
}
