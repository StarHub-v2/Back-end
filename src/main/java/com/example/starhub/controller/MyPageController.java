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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    /**
     * 마이페이지 정보 불러오기 - 사용자 정보
     */
    @GetMapping("/mypage/users")
    public ResponseEntity<ResponseDto<ProfileResponseDto>> getUserProfile(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        ProfileResponseDto res = myPageService.getUserProfile(customUserDetails.getUsername());
        return ResponseEntity
                .status(ResponseCode.SUCCESS_GET_PROFILE.getStatus().value())
                .body(new ResponseDto<>(ResponseCode.SUCCESS_GET_PROFILE, res));
    }

    /**
     * 마이페이지 - 프로필 정보 수정하기
     */
    @PatchMapping("/mypage/users")
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
    @GetMapping("/mypage/meetings/recent")
    public ResponseEntity<ResponseDto<List<MeetingSummaryResponseDto>>> getUserRecentMeetings(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        List<MeetingSummaryResponseDto> res = myPageService.getUserRecentMeetings(customUserDetails.getUsername());
        return ResponseEntity
                .status(ResponseCode.SUCCESS_GET_CREATED_RECENT_MEETINGS.getStatus().value())
                .body(new ResponseDto<>(ResponseCode.SUCCESS_GET_CREATED_RECENT_MEETINGS, res));
    }

    /**
     * 마이페이지 정보 불러오기 - 내가 좋아요 누른 모임 목록 최신 3개
     */
    @GetMapping("/mypage/meetings/liked/recent")
    public ResponseEntity<ResponseDto<List<MeetingSummaryResponseDto>>> getLikedRecentMeetings(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        List<MeetingSummaryResponseDto> res = myPageService.getLikedRecentMeetings(customUserDetails.getUsername());
        return ResponseEntity
                .status(ResponseCode.SUCCESS_GET_LIKED_RECENT_MEETINGS.getStatus().value())
                .body(new ResponseDto<>(ResponseCode.SUCCESS_GET_LIKED_RECENT_MEETINGS, res));
    }

    /**
     * 마이페이지 정보 불러오기 - 내가 참여한 모임 목록 최신 3개
     */
    @GetMapping("/mypage/meetings/applied/recent")
    public ResponseEntity<ResponseDto<List<MeetingSummaryResponseDto>>> getAppliedRecentMeetings(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        List<MeetingSummaryResponseDto> res = myPageService.getAppliedRecentMeetings(customUserDetails.getUsername());
        return ResponseEntity
                .status(ResponseCode.SUCCESS_GET_APPLIED_RECENT_MEETINGS.getStatus().value())
                .body(new ResponseDto<>(ResponseCode.SUCCESS_GET_APPLIED_RECENT_MEETINGS, res));
    }

    /**
     * 내가 작성한 글 목록 불러오기
     */
    @GetMapping("/mypage/meetings/created")
    public ResponseEntity<ResponseDto> getCreatedMeetings() {
        return ResponseEntity
                .status(ResponseCode.SUCCESS_GET_CREATED_MEETINGS.getStatus().value())
                .body(new ResponseDto<>(ResponseCode.SUCCESS_GET_CREATED_MEETINGS, null));
    }

    /**
     * 내가 관심있는 글 목록 불러오기
     */
    @GetMapping("/mypage/meetings/liked")
    public ResponseEntity<ResponseDto> getLikedMeetings() {
        return ResponseEntity
                .status(ResponseCode.SUCCESS_GET_LIKED_MEETINGS.getStatus().value())
                .body(new ResponseDto<>(ResponseCode.SUCCESS_GET_LIKED_MEETINGS, null));
    }

    /**
     * 내가 참여한 모임 글 목록 불러오기
     */
    @GetMapping("/mypage/meetings/applied")
    public ResponseEntity<ResponseDto> getAppliedMeetings() {
        return ResponseEntity
                .status(ResponseCode.SUCCESS_GET_APPLIED_MEETINGS.getStatus().value())
                .body(new ResponseDto<>(ResponseCode.SUCCESS_GET_APPLIED_MEETINGS, null));
    }
}
