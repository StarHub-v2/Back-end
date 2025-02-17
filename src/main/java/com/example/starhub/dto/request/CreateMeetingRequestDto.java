package com.example.starhub.dto.request;

import com.example.starhub.entity.enums.Duration;
import com.example.starhub.entity.enums.RecruitmentType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Getter
public class CreateMeetingRequestDto {

    @NotNull(message = "모집 유형을 입력해주세요.")
    private RecruitmentType recruitmentType;

    @NotNull(message = "최대 참가자 수를 입력해주세요.")
    @Min(value = 1, message = "최대 참가자 수는 1 이상이어야 합니다.")
    private Integer maxParticipants;

    @NotNull(message = "진행 기간을 입력해주세요.")
    private Duration duration;

    @NotNull(message = "종료 날짜를 입력해주세요.")
    @Future(message = "종료 날짜는 미래의 날짜여야 합니다.")
    private LocalDate endDate;

    @NotBlank(message = "위치를 입력해주세요.")
    private String location;

    @NotNull(message = "위도 값을 입력해주세요.")
    @DecimalMin(value = "-90.0", inclusive = true, message = "위도는 -90에서 90 사이여야 합니다.")
    @DecimalMax(value = "90.0", inclusive = true, message = "위도는 -90에서 90 사이여야 합니다.")
    private Double latitude;

    @NotNull(message = "경도 값을 입력해주세요.")
    @DecimalMin(value = "-180.0", inclusive = true, message = "경도는 -180에서 180 사이여야 합니다.")
    @DecimalMax(value = "180.0", inclusive = true, message = "경도는 -180에서 180 사이여야 합니다.")
    private Double longitude;

    @NotBlank(message = "제목을 입력해주세요.")
    @Size(max = 100, message = "제목은 100자 이내로 작성해야 합니다.")
    private String title;

    @NotBlank(message = "설명을 입력해주세요.")
    private String description;

    @NotBlank(message = "목표를 입력해주세요.")
    private String goal;

    private String otherInfo;

    private List<Long> techStackIds;

    private List<String> otherTechStacks;
}
