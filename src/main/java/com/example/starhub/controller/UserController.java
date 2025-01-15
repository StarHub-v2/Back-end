package com.example.starhub.controller;

import com.example.starhub.dto.request.CreateProfileRequestDto;
import com.example.starhub.dto.request.CreateUserRequestDto;
import com.example.starhub.dto.request.UsernameCheckRequestDto;
import com.example.starhub.dto.response.ProfileResponseDto;
import com.example.starhub.dto.response.UserResponseDto;
import com.example.starhub.dto.response.UsernameCheckResponseDto;
import com.example.starhub.response.code.ResponseCode;
import com.example.starhub.response.dto.ResponseDto;
import com.example.starhub.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/users/{userId}/profile")
    public ResponseEntity<ResponseDto> createUserProfile(@PathVariable Long userId,
                                                         @Valid @RequestBody CreateProfileRequestDto createProfileRequestDto) {
        ProfileResponseDto res = userService.createUserProfile(userId, createProfileRequestDto);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_CREATE_PROFILE.getStatus().value())
                .body(new ResponseDto<>(ResponseCode.SUCCESS_CREATE_PROFILE, res));
    }
}
