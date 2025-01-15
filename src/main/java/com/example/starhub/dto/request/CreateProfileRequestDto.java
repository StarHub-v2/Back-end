package com.example.starhub.dto.request;

import lombok.Getter;

import javax.validation.constraints.*;

@Getter
public class CreateProfileRequestDto {

    @NotBlank(message = "프로필 이미지를 입력해주세요.")
    private String profileImage;

    @NotBlank(message = "닉네임을 입력해주세요.")
    private String nickname;

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @Positive(message = "나이는 양수로 입력해주세요.")
    private Integer age;

    @NotBlank(message = "한 줄 소개를 입력해주세요.")
    private String bio;

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "유효한 이메일 형식을 입력해주세요.")
    private String email;

    @NotBlank(message = "전화번호를 입력해주세요.")
    @Pattern(regexp = "^\\d{10,11}$", message = "전화번호는 10~11자리 숫자로 입력해주세요.")
    private String phoneNumber;
}
