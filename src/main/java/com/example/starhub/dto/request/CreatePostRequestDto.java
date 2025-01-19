package com.example.starhub.dto.request;

import com.example.starhub.entity.enums.Duration;
import com.example.starhub.entity.enums.RecruitmentType;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
public class CreatePostRequestDto {

    private RecruitmentType recruitmentType;
    private Integer maxParticipants;
    private Duration duration;
    private LocalDate endDate;
    private String location;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String title;
    private String description;
    private String goal;
    private String otherInfo;
    private List<Long> techStackIds;  // 기술 스택 ID 목록
    private List<String> otherTechStacks; // 사용자가 입력한 기타 기술 스택
}
