package com.example.starhub.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class CreateUserRequestDto {

    @NotBlank(message = "사용자명을 입력해주세요.")
    private String username;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
}
