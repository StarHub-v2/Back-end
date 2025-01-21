package com.example.starhub.dto.response;

import com.example.starhub.entity.ApplicantEntity;
import com.example.starhub.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ApplicantResponseDto {

    private Long id;
    private String content;
    private LocalDateTime updatedAt;
    private ApplicantDto applicant;

    /**
     * 지원서 관련 정보
     * - ApplicantEntity으로부터 ApplicantResponseDto를 생성합니다.
     *
     * @param applicantEntity 지원서 엔티티
     * @return 생성된 ApplicantResponseDto
     */
    public static ApplicantResponseDto fromEntity(ApplicantEntity applicantEntity) {
        UserEntity applicant = applicantEntity.getApplicant();
        ApplicantDto applicantDto = ApplicantDto.builder()
                .username(applicant.getUsername())
                .build();

        return ApplicantResponseDto.builder()
                .id(applicantEntity.getId())
                .content(applicantEntity.getContent())
                .updatedAt(applicantEntity.getUpdatedAt())
                .applicant(applicantDto)
                .build();
    }

}
