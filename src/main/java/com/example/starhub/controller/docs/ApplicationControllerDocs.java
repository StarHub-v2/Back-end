package com.example.starhub.controller.docs;

import com.example.starhub.dto.request.ApplicationRequestDto;
import com.example.starhub.dto.response.ApplicationResponseDto;
import com.example.starhub.dto.security.CustomUserDetails;
import com.example.starhub.response.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "ApplicationController", description = "지원서 관련 API")
public interface ApplicationControllerDocs {

    /**
     * 지원서 작성
     */
    @Operation(
            summary = "지원서 작성하기",
            description = "지원서 작성하기를 진행합니다. 지원자만 지원할 권한이 있습니다."
    )
    ResponseEntity<ResponseDto<ApplicationResponseDto>> createApplication(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long meetingId,
            @Valid @RequestBody ApplicationRequestDto applicationRequestDto);

    /**
     * 지원서 목록 불러오기
     */
    @Operation(
            summary = "지원서 목록 불러오기",
            description = "지원서 목록 불러오기를 진행합니다."
    )
    ResponseEntity<ResponseDto<List<ApplicationResponseDto>>> getApplicationList(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long meetingId);

    /**
     * 지원서 상세 불러오기
     */
    @Operation(
            summary = "지원서 상세 불러오기",
            description = "지원서 상세 불러오기를 진행합니다. 지원서만 불러올 권한이 있습니다."
    )
    ResponseEntity<ResponseDto<ApplicationResponseDto>> getApplicationDetail(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long meetingId,
            @PathVariable Long applicationId);

    /**
     * 지원서 수정하기
     */
    @Operation(
            summary = "지원서 수정하기",
            description = "지원서 수정하기를 진행합니다. 지원자만 수정할 권한이 있습니다."
    )
    ResponseEntity<ResponseDto<ApplicationResponseDto>> updateApplication(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long meetingId,
            @PathVariable Long applicationId,
            @Valid @RequestBody ApplicationRequestDto applicationRequestDto);

    /**
     * 지원서 삭제하기
     */
    @Operation(
            summary = "지원서 삭제하기",
            description = "지원서 삭제하기를 진행합니다. 지원자만 삭제할 권한이 있습니다."
    )
    ResponseEntity<ResponseDto> deleteApplication(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long meetingId,
            @PathVariable Long applicationId);

}
