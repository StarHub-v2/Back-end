package com.example.starhub.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class ApplicationRequestDto {

    @NotBlank(message = "지원서 내용을 입력해주세요.")
    private String content;
}
