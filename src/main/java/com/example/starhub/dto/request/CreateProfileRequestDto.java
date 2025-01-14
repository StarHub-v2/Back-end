package com.example.starhub.dto.request;

import lombok.Getter;

@Getter
public class CreateProfileRequestDto {

    private String profileImage;
    private String nickname;
    private String name;
    private Integer age;
    private String bio;
    private String email;
    private String phoneNumber;
}
