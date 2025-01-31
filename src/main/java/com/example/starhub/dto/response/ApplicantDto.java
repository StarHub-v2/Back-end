package com.example.starhub.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApplicantDto {
    private String nickname; // 지원자 닉네임
    private String profileImage; // 지원자 프로필 이미지
}
