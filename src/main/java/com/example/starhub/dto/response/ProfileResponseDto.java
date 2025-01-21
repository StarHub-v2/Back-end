package com.example.starhub.dto.response;

import com.example.starhub.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProfileResponseDto {

    private Long id;
    private String nickname;

    /**
     * 2차 회원가입 이후 프로필 정보
     * - UserEntity로부터 ProfileResponseDto를 생성합니다.
     *
     * @param userEntity 유저 엔티티
     * @return 생성된 ProfileResponseDto
     */
    public static ProfileResponseDto fromEntity(UserEntity userEntity) {
        return ProfileResponseDto.builder()
                .id(userEntity.getId())
                .nickname(userEntity.getNickname())
                .build();
    }
}
