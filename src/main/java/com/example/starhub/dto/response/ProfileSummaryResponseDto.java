package com.example.starhub.dto.response;

import com.example.starhub.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProfileSummaryResponseDto {

    private Long id;
    private String nickname;
    private String profileImage;

    /**
     * 2차 회원가입 이후 프로필 정보
     * - UserEntity로부터 ProfileResponseDto를 생성합니다.
     *
     * @param userEntity 유저 엔티티
     * @return 생성된 ProfileResponseDto
     */
    public static ProfileSummaryResponseDto fromEntity(UserEntity userEntity) {
        return ProfileSummaryResponseDto.builder()
                .id(userEntity.getId())
                .nickname(userEntity.getNickname())
                .profileImage(userEntity.getProfileImage())
                .build();
    }

}
