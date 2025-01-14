package com.example.starhub.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ProfileResponseDto {

    private Long id;
    private String nickname;
}
