package com.example.starhub.dto.response;

import com.example.starhub.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDto {

    private String username;
    private Boolean isProfileComplete;

    /**
     * 1차 회원가입 이후 사용자 관련 정보
     * - UserEntity로부터 UserResponseDto를 생성합니다.
     *
     * @param userEntity 유저 엔티티
     * @return 생성된 UserResponseDto
     */
    public static UserResponseDto fromEntity(UserEntity userEntity) {
        return UserResponseDto.builder()
                .username(userEntity.getUsername())
                .isProfileComplete(userEntity.getIsProfileComplete())
                .build();
    }
}
