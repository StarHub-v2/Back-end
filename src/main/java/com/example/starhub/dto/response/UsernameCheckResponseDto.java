package com.example.starhub.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UsernameCheckResponseDto {
    private String username;
    private boolean isAvailable;
}
