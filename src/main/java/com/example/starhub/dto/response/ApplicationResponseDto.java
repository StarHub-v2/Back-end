package com.example.starhub.dto.response;

import com.example.starhub.entity.ApplicationEntity;
import com.example.starhub.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ApplicationResponseDto {

    private Long id;
    private String content;
    private LocalDateTime updatedAt;
    private ApplicantDto applicant; // 지원자 정보

    /**
     * 지원서 관련 정보
     * - ApplicantEntity로부터 ApplicantResponseDto를 생성합니다.
     *
     * @param applicationEntity 지원서 엔티티
     * @return 생성된 ApplicationResponseDto
     */
    public static ApplicationResponseDto fromEntity(ApplicationEntity applicationEntity) {
        UserEntity applicant = applicationEntity.getApplicant();
        ApplicantDto applicantDto = ApplicantDto.builder()
                .nickname(applicant.getNickname())
                .build();

        return ApplicationResponseDto.builder()
                .id(applicationEntity.getId())
                .content(applicationEntity.getContent())
                .updatedAt(applicationEntity.getUpdatedAt())
                .applicant(applicantDto)
                .build();
    }

}
