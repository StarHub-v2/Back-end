package com.example.starhub.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponseDto {

    private String username;
    private Boolean isProfileComplete;
    private String nickname;
    private String profileImage;

}
