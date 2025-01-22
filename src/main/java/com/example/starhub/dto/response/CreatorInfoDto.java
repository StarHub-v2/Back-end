package com.example.starhub.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreatorInfoDto {
    private String name;
    private Integer age;
    private String bio;
    private String phoneNumber;
    private String email;
}

