package com.example.starhub.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponseDto {

    private String username;
    private Boolean isProfileComplete;
    private String nickname;

}
