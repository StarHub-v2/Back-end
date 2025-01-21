package com.example.starhub.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UsernameCheckResponseDto {
    private String username;
    private boolean isAvailable;

    /**
     * 사용자명 중복 여부
     *
     * @param username 확인할 사용자명
     * @param isAvailable isAvailable 사용자명 사용 가능 여부 (true: 사용 가능, false: 사용 불가능)
     * @return 생성된 UsernameCheckResponseDto
     */
    public static UsernameCheckResponseDto from(String username, boolean isAvailable) {
        return UsernameCheckResponseDto.builder()
                .username(username)
                .isAvailable(isAvailable)
                .build();
    }
}
