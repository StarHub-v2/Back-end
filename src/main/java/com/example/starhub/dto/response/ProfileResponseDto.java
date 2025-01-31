package com.example.starhub.dto.response;

import com.example.starhub.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProfileResponseDto {

    private Long id;
    private String profileImage;
    private String name;
    private String nickname;
    private Integer age;
    private String phoneNumber;
    private String email;
    private String bio;


    /**
     * 마이페이지 - 프로필 정보
     * - UserEntity로부터 ProfileResponseDto를 생성합니다.
     *
     * @param userEntity 유저 엔티티
     * @return 생성된 ProfileResponseDto
     */
    public static ProfileResponseDto fromEntity(UserEntity userEntity) {
        return ProfileResponseDto.builder()
                .id(userEntity.getId())
                .profileImage(userEntity.getProfileImage())
                .name(userEntity.getName())
                .nickname(userEntity.getNickname())
                .age(userEntity.getAge())
                .phoneNumber(userEntity.getPhoneNumber())
                .email(userEntity.getEmail())
                .bio(userEntity.getBio())
                .build();
    }

}
