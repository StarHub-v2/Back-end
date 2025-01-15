package com.example.starhub.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class UsernameCheckRequestDto {

    @NotBlank(message = "아이디를 입력해주세요.")
    private String username;
}
