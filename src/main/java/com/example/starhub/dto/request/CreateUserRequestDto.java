package com.example.starhub.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class CreateUserRequestDto {

    @NotBlank(message = "아이디를 입력해주세요.")
    private String username;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
}
