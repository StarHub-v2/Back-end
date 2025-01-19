package com.example.starhub.controller.docs;

import com.example.starhub.dto.request.CreatePostRequestDto;
import com.example.starhub.dto.request.CreateProfileRequestDto;
import com.example.starhub.dto.request.CreateUserRequestDto;
import com.example.starhub.dto.request.UsernameCheckRequestDto;
import com.example.starhub.dto.response.PostResponseDto;
import com.example.starhub.dto.response.ProfileResponseDto;
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

@Tag(name = "PostController", description = "포스트(스터디 및 프로젝트) 관련 API")
public interface PostControllerDocs {

    /**
     * 포스트(스터디 및 프로젝트) 업로드
     */
    @Operation(
            summary = "포스트(스터디 및 프로젝트) 업로드",
            description = "포스트(스터디 및 프로젝트) 업로드를 진행합니다."
    )
    ResponseEntity<ResponseDto<PostResponseDto>> createPost(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                            @Valid @RequestBody CreatePostRequestDto createPostRequestDto);

}
