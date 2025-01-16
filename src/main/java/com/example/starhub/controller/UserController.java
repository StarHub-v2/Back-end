package com.example.starhub.controller;

import com.example.starhub.dto.request.CreateProfileRequestDto;
import com.example.starhub.dto.request.CreateUserRequestDto;
import com.example.starhub.dto.request.UsernameCheckRequestDto;
import com.example.starhub.dto.response.ProfileResponseDto;
import com.example.starhub.dto.response.UserResponseDto;
import com.example.starhub.dto.response.UsernameCheckResponseDto;
import com.example.starhub.dto.security.CustomUserDetails;
import com.example.starhub.response.code.ResponseCode;
import com.example.starhub.response.dto.ResponseDto;
import com.example.starhub.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 1차 회원가입
     */
    @PostMapping("/register")
    public ResponseEntity<ResponseDto> registerUser(@Valid @RequestBody CreateUserRequestDto createUserRequestDto) {
        UserResponseDto res = userService.registerUser(createUserRequestDto);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_CREATE_USER.getStatus().value())
                .body(new ResponseDto<>(ResponseCode.SUCCESS_CREATE_USER, res));
    }

    /**
     * 아이디 중복 확인
     */
    @PostMapping("/users/check")
    public ResponseEntity<ResponseDto> checkUsernameDuplicate(@Valid @RequestBody UsernameCheckRequestDto usernameCheckRequestDto) {
        UsernameCheckResponseDto res = userService.checkUsernameDuplicate(usernameCheckRequestDto);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_CHECK_ID.getStatus().value())
                .body(new ResponseDto<>(ResponseCode.SUCCESS_CHECK_ID, res));
    }

    /**
     * 프로필 생성하기(2차 회원가입)
     */
    @PostMapping("/users/profile")
    public ResponseEntity<ResponseDto> createUserProfile(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                         @Valid @RequestBody CreateProfileRequestDto createProfileRequestDto) {
        Long userId = userService.findUserIdByUsername(customUserDetails.getUsername());

        ProfileResponseDto res = userService.createUserProfile(userId, createProfileRequestDto);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_CREATE_PROFILE.getStatus().value())
                .body(new ResponseDto<>(ResponseCode.SUCCESS_CREATE_PROFILE, res));
    }

    /**
     * Refresh 토큰 재발급
     */
    @PostMapping("/reissue")
    public ResponseEntity<?> reissueToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = null;

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refresh".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                }
            }
        }

        String tokens = userService.reissueToken(refreshToken);
        String[] tokenArray = tokens.split(",");

        // 새로운 Access 및 Refresh 토큰 응답에 설정
        response.addHeader("Authorization", "Bearer " + tokenArray[0]);
        response.addCookie(createCookie("refresh", tokenArray[1]));

        return ResponseEntity
                .status(ResponseCode.SUCCESS_REISSUE_TOKEN.getStatus().value())
                .body(new ResponseDto<>(ResponseCode.SUCCESS_REISSUE_TOKEN, null));
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        //cookie.setSecure(true);
        //cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}
