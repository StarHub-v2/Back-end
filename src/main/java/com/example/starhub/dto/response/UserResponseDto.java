package com.example.starhub.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDto {

    private Long id;
    private String username;
    private Boolean isProfileComplete;
}
