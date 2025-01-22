package com.example.starhub.dto.response;

import com.example.starhub.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ConfirmMeetingResponseDto {

    private String name;
    private Integer age;
    private String bio;
    private String phoneNumber;
    private String email;

    public static ConfirmMeetingResponseDto fromEntity(UserEntity userEntity) {
        return ConfirmMeetingResponseDto.builder()

                .name(userEntity.getName())
                .age(userEntity.getAge())
                .bio(userEntity.getBio())
                .phoneNumber(userEntity.getPhoneNumber())
                .email(userEntity.getEmail())
                .build();
    }
}
