package com.example.starhub.entity;

import com.example.starhub.dto.request.CreateMeetingRequestDto;
import com.example.starhub.dto.request.MeetingUpdateRequestDto;
import com.example.starhub.entity.enums.Duration;
import com.example.starhub.entity.enums.RecruitmentType;
import com.example.starhub.exception.StudyConfirmedException;
import com.example.starhub.response.code.ErrorCode;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class MeetingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 스터디 고유 식별자

    @Enumerated(EnumType.STRING)
    private RecruitmentType recruitmentType;  // 모집 구분 (스터디 / 프로젝트)

    private Integer maxParticipants;  // 모집 인원

    @Enumerated(EnumType.STRING)
    private Duration duration;  // 진행 기간

    private LocalDate endDate;  // 마감 날짜

    @Column(length = 100)
    private String location;  // 진행 장소

    private BigDecimal latitude;  // 위도

    private BigDecimal longitude;  // 경도

    @Column(length = 100)
    private String title;  // 스터디 제목

    @Column(columnDefinition = "TEXT")
    private String description;  // 스터디 소개

    @Column(columnDefinition = "TEXT")
    private String goal;  // 스터디 목표

    @Column(columnDefinition = "TEXT")
    private String otherInfo;  // 기타 정보

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;  // 생성일

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;  // 수정일

    private Boolean isConfirmed;  // 모임 확정 여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private UserEntity creator;  // 사용자(작성자)

    public static MeetingEntity createMeeting(UserEntity user, CreateMeetingRequestDto createMeetingRequestDto) {
        return MeetingEntity.builder()
                .recruitmentType(createMeetingRequestDto.getRecruitmentType())
                .maxParticipants(createMeetingRequestDto.getMaxParticipants())
                .duration(createMeetingRequestDto.getDuration())
                .endDate(createMeetingRequestDto.getEndDate())
                .location(createMeetingRequestDto.getLocation())
                .latitude(createMeetingRequestDto.getLatitude())
                .longitude(createMeetingRequestDto.getLongitude())
                .title(createMeetingRequestDto.getTitle())
                .description(createMeetingRequestDto.getDescription())
                .goal(createMeetingRequestDto.getGoal())
                .otherInfo(createMeetingRequestDto.getOtherInfo())
                .isConfirmed(false) // 기본값 설정
                .creator(user)
                .build();
    }

    public void updateMeeting(MeetingUpdateRequestDto meetingUpdateRequestDto) {
        this.recruitmentType = meetingUpdateRequestDto.getRecruitmentType() != null ? meetingUpdateRequestDto.getRecruitmentType() : this.recruitmentType;
        this.maxParticipants = meetingUpdateRequestDto.getMaxParticipants() != null ? meetingUpdateRequestDto.getMaxParticipants() : this.maxParticipants;
        this.duration = meetingUpdateRequestDto.getDuration() != null ? meetingUpdateRequestDto.getDuration() : this.duration;
        this.endDate = meetingUpdateRequestDto.getEndDate() != null ? meetingUpdateRequestDto.getEndDate() : this.endDate;
        this.location = meetingUpdateRequestDto.getLocation() != null ? meetingUpdateRequestDto.getLocation() : this.location;
        this.latitude = meetingUpdateRequestDto.getLatitude() != null ? meetingUpdateRequestDto.getLatitude() : this.latitude;
        this.longitude = meetingUpdateRequestDto.getLongitude() != null ? meetingUpdateRequestDto.getLongitude() : this.longitude;
        this.title = meetingUpdateRequestDto.getTitle() != null ? meetingUpdateRequestDto.getTitle() : this.title;
        this.description = meetingUpdateRequestDto.getDescription() != null ? meetingUpdateRequestDto.getDescription() : this.description;
        this.goal = meetingUpdateRequestDto.getGoal() != null ? meetingUpdateRequestDto.getGoal() : this.goal;
        this.otherInfo = meetingUpdateRequestDto.getOtherInfo() != null ? meetingUpdateRequestDto.getOtherInfo() : this.otherInfo;
    }

    public void confirm() {

        this.isConfirmed = true;
    }

}
