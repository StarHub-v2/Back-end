package com.example.starhub.controller;

import com.example.starhub.dto.request.UpdateProfileRequestDto;
import com.example.starhub.dto.response.MeetingSummaryResponseDto;
import com.example.starhub.dto.response.ProfileResponseDto;
import com.example.starhub.dto.security.CustomUserDetails;
import com.example.starhub.response.code.ResponseCode;
import com.example.starhub.response.dto.ResponseDto;
import com.example.starhub.service.MyPageService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/mypage")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    /**
     * 마이페이지 정보 불러오기 - 사용자 정보
     */
    @GetMapping("/users")
    public ResponseEntity<ResponseDto<ProfileResponseDto>> getUserProfile(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        ProfileResponseDto res = myPageService.getUserProfile(customUserDetails.getUsername());
        return ResponseEntity
                .status(ResponseCode.SUCCESS_GET_PROFILE.getStatus().value())
                .body(new ResponseDto<>(ResponseCode.SUCCESS_GET_PROFILE, res));
    }

    /**
     * 마이페이지 - 프로필 정보 수정하기
     */
    @PatchMapping("/users")
    public ResponseEntity<ResponseDto<ProfileResponseDto>> updateUserProfile(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                         @Valid @RequestBody UpdateProfileRequestDto updateProfileRequestDto) {
        ProfileResponseDto res = myPageService.updateUserProfile(customUserDetails.getUsername(), updateProfileRequestDto);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_UPDATE_PROFILE.getStatus().value())
                .body(new ResponseDto<>(ResponseCode.SUCCESS_UPDATE_PROFILE, res));
    }

    /**
     * 마이페이지 정보 불러오기 - 내가 작성한 모임 목록 최신 3개
     */
    @GetMapping("/meetings/created/recent")
    public ResponseEntity<ResponseDto<List<MeetingSummaryResponseDto>>> getUserRecentMeetings(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        List<MeetingSummaryResponseDto> res = myPageService.getUserRecentMeetings(customUserDetails.getUsername());
        return ResponseEntity
                .status(ResponseCode.SUCCESS_GET_CREATED_RECENT_MEETINGS.getStatus().value())
                .body(new ResponseDto<>(ResponseCode.SUCCESS_GET_CREATED_RECENT_MEETINGS, res));
    }

    /**
     * 마이페이지 정보 불러오기 - 내가 좋아요 누른 모임 목록 최신 3개
     */
    @GetMapping("/meetings/liked/recent")
    public ResponseEntity<ResponseDto<List<MeetingSummaryResponseDto>>> getLikedRecentMeetings(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        List<MeetingSummaryResponseDto> res = myPageService.getLikedRecentMeetings(customUserDetails.getUsername());
        return ResponseEntity
                .status(ResponseCode.SUCCESS_GET_LIKED_RECENT_MEETINGS.getStatus().value())
                .body(new ResponseDto<>(ResponseCode.SUCCESS_GET_LIKED_RECENT_MEETINGS, res));
    }

    /**
     * 마이페이지 정보 불러오기 - 내가 참여한 모임 목록 최신 3개
     */
    @GetMapping("/meetings/applied/recent")
    public ResponseEntity<ResponseDto<List<MeetingSummaryResponseDto>>> getAppliedRecentMeetings(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        List<MeetingSummaryResponseDto> res = myPageService.getAppliedRecentMeetings(customUserDetails.getUsername());
        return ResponseEntity
                .status(ResponseCode.SUCCESS_GET_APPLIED_RECENT_MEETINGS.getStatus().value())
                .body(new ResponseDto<>(ResponseCode.SUCCESS_GET_APPLIED_RECENT_MEETINGS, res));
    }

    /**
     * 내가 작성한 글 목록 불러오기
     */
    @GetMapping("/meetings/created")
    public ResponseEntity<ResponseDto<Page<MeetingSummaryResponseDto>>> getCreatedMeetings(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<MeetingSummaryResponseDto> res = myPageService.getCreatedMeetings(customUserDetails.getUsername(), page, size);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_GET_CREATED_MEETINGS.getStatus().value())
                .body(new ResponseDto<>(ResponseCode.SUCCESS_GET_CREATED_MEETINGS, res));
    }

    /**
     * 내가 관심있는 글 목록 불러오기
     */
    @GetMapping("/meetings/liked")
    public ResponseEntity<ResponseDto<Page<MeetingSummaryResponseDto>>> getLikedMeetings(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<MeetingSummaryResponseDto> res = myPageService.getLikedMeetings(customUserDetails.getUsername(), page, size);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_GET_LIKED_MEETINGS.getStatus().value())
                .body(new ResponseDto<>(ResponseCode.SUCCESS_GET_LIKED_MEETINGS, res));
    }

    /**
     * 내가 참여한 모임 글 목록 불러오기
     */
    @GetMapping("/meetings/applied")
    public ResponseEntity<ResponseDto<Page<MeetingSummaryResponseDto>>> getAppliedMeetings(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<MeetingSummaryResponseDto> res = myPageService.getAppliedMeetings(customUserDetails.getUsername(), page, size);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_GET_APPLIED_MEETINGS.getStatus().value())
                .body(new ResponseDto<>(ResponseCode.SUCCESS_GET_APPLIED_MEETINGS, res));
    }
}
