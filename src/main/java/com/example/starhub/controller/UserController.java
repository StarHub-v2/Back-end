package com.example.starhub.controller;

import com.example.starhub.dto.request.CreateUserRequestDto;
import com.example.starhub.dto.response.UserResponseDto;
import com.example.starhub.response.code.ResponseCode;
import com.example.starhub.response.dto.ResponseDTO;
import com.example.starhub.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 자체 회원가입
     */
    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> registerUser(@RequestBody CreateUserRequestDto createUserRequestDto) {
        UserResponseDto res = userService.registerUser(createUserRequestDto);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_CREATE_USER.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_CREATE_USER, res));
    }

}
