package com.example.starhub.controller.docs;

import com.example.starhub.dto.request.*;
import com.example.starhub.dto.response.*;
import com.example.starhub.dto.security.CustomUserDetails;
import com.example.starhub.response.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Tag(name = "MeetingController", description = "모임(스터디 및 프로젝트) 관련 API")
public interface MeetingControllerDocs {

    /**
     * 모임(스터디 및 프로젝트) 업로드
     */
    @Operation(
            summary = "모임(스터디 및 프로젝트) 업로드",
            description = "모임(스터디 및 프로젝트) 업로드를 진행합니다."
    )
    ResponseEntity<ResponseDto<MeetingResponseDto>> createMeeting(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Valid @RequestBody CreateMeetingRequestDto createMeetingRequestDto);

    /**
     * 모임 목록 불러오기 (메인 화면에 쓰일 API)
     */
    @Operation(
            summary = "모임 목록 불러오기 (메인 화면에 쓰일 API)",
            description = "모임 목록 불러오기를 진행합니다. 모임 카드 형식 정보가 담긴 목록을 불러옵니다."
    )
    ResponseEntity<ResponseDto> getMeetingList(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int size
    );

    /**
     * 모임 상세 불러오기
     */
    @Operation(
            summary = "모임 상세 불러오기",
            description = "모임 상세 불러오기를 진행합니다. 지원자, 개설자, 지원 여부, 모임 확정 여부 등의 요소를 포함합니다."
    )
    ResponseEntity<ResponseDto<MeetingDetailResponseDto>> getMeetingDetail(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long meetingId);

    /**
     * 모임 수정하기
     */
    @Operation(
            summary = "모임 수정하기",
            description = "모임 수정하기를 진행합니다. 개설자만 이 모임을 수정할 권한이 있습니다."
    )
    ResponseEntity<ResponseDto<MeetingResponseDto>> updateMeeting(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long meetingId,
            @RequestBody MeetingUpdateRequestDto meetingUpdateRequestDto);

    /**
     * 모임 삭제하기
     */
    @Operation(
            summary = "모임 삭제하기",
            description = "모임 삭제하기를 진행합니다. 개설자만 이 모임을 삭제할 권한이 있습니다."
    )
    ResponseEntity<ResponseDto> deleteMeeting(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long meetingId);
}
