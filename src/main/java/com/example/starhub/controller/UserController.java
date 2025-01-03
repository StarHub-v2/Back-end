package com.example.starhub.controller;

import com.example.starhub.code.ResponseCode;
import com.example.starhub.dto.post.PostResponseDto;
import com.example.starhub.dto.response.ResponseDTO;
import com.example.starhub.dto.user.UserLoginDTO;
import com.example.starhub.dto.user.UserRegisterDTO;
import com.example.starhub.entity.UserEntity;
import com.example.starhub.projection.user.GetUser;
import com.example.starhub.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController // JSON 형태의 결과값 반환
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;


    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> register(@RequestParam("info") String userRegisterDTO)throws JsonProcessingException
//                                                @RequestParam("image") MultipartFile image) throws JsonProcessingException
    {

        ObjectMapper mapper = new ObjectMapper();
        UserRegisterDTO mapperUploadPostDTO = mapper.readValue(userRegisterDTO, UserRegisterDTO.class);
        GetUser res = userService.register(mapperUploadPostDTO);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_REGISTER.getStatus().value())
                .body(new ResponseDTO(ResponseCode.SUCCESS_REGISTER, res));
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> login(@RequestBody UserLoginDTO userLoginDTO) {
        GetUser res = userService.login(userLoginDTO);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_LOGIN.getStatus().value())
                .body(new ResponseDTO(ResponseCode.SUCCESS_LOGIN, res));
    }


    @GetMapping("/logout")
    public ResponseEntity<ResponseDTO> logout(@RequestParam("loginId") String loginId) {
        userService.logout(loginId);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_LOGOUT.getStatus().value())
                .body(new ResponseDTO(ResponseCode.SUCCESS_LOGOUT, null));
    }


}