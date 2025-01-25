package com.example.starhub.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ApplicationRequestDto {

    @NotBlank(message = "지원서 내용을 입력해주세요.")
    private String content;
}
