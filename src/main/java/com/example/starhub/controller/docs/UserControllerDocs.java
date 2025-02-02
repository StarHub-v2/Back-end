package com.example.starhub.controller.docs;

import com.example.starhub.dto.request.CreateProfileRequestDto;
import com.example.starhub.dto.request.CreateUserRequestDto;
import com.example.starhub.dto.request.UsernameCheckRequestDto;
import com.example.starhub.dto.response.ProfileSummaryResponseDto;
import com.example.starhub.dto.response.UserResponseDto;
import com.example.starhub.dto.response.UsernameCheckResponseDto;
import com.example.starhub.dto.security.CustomUserDetails;
import com.example.starhub.response.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Tag(name = "UserController", description = "유저 관련 API")
public interface UserControllerDocs {

    /**
     * 1차 회원가입
     */
    @Operation(
            summary = "1차 회원가입",
            description = "사용자명, 비밀번호를 이용해 회원가입을 진행합니다."
    )
    ResponseEntity<ResponseDto<UserResponseDto>> registerUser(@Valid @RequestBody CreateUserRequestDto createUserRequestDto);

    /**
     * 사용자명 중복 확인
     */
    @Operation(
            summary = "사용자명 중복 확인",
            description = "사용자명 중복 여부를 확인합니다."
    )
    ResponseEntity<ResponseDto<UsernameCheckResponseDto>> checkUsernameDuplicate(@Valid @RequestBody UsernameCheckRequestDto usernameCheckRequestDto);

    /**
     * 프로필 생성하기(2차 회원가입)
     */
    @Operation(
            summary = "프로필 생성하기(2차 회원가입)",
            description = "사용자의 프로필 정보를 이용해 2차 회원가입을 진행합니다."
    )
    ResponseEntity<ResponseDto<ProfileSummaryResponseDto>> createUserProfile(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                                             @Valid @RequestBody CreateProfileRequestDto createProfileRequestDto);
    /**
     * 토큰 재발급
     */
    @Operation(
            summary = "토큰 재발급",
            description = "Access 및 Refresh 토큰을 재발급을 진행합니다."
    )
    ResponseEntity<ResponseDto> reissueToken(HttpServletRequest request, HttpServletResponse response);

}
