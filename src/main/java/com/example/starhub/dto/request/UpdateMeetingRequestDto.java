package com.example.starhub.dto.request;

import com.example.starhub.entity.enums.Duration;
import com.example.starhub.entity.enums.RecruitmentType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Getter
public class UpdateMeetingRequestDto {

    private RecruitmentType recruitmentType;
    private Integer maxParticipants;
    private Duration duration;
    private LocalDate endDate;
    private String location;
    private Double latitude;
    private Double longitude;
    private String title;
    private String description;
    private String goal;
    private String otherInfo;
    private List<Long> techStackIds;
    private List<String> otherTechStacks;
}
