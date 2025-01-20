package com.example.starhub.entity;

import com.example.starhub.dto.request.PostUpdateRequestDto;
import com.example.starhub.entity.enums.Duration;
import com.example.starhub.entity.enums.RecruitmentType;
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
public class PostEntity {

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

    public void updatePost(PostUpdateRequestDto postUpdateRequestDto) {
        this.recruitmentType = postUpdateRequestDto.getRecruitmentType() != null ? postUpdateRequestDto.getRecruitmentType() : this.recruitmentType;
        this.maxParticipants = postUpdateRequestDto.getMaxParticipants() != null ? postUpdateRequestDto.getMaxParticipants() : this.maxParticipants;
        this.duration = postUpdateRequestDto.getDuration() != null ? postUpdateRequestDto.getDuration() : this.duration;
        this.endDate = postUpdateRequestDto.getEndDate() != null ? postUpdateRequestDto.getEndDate() : this.endDate;
        this.location = postUpdateRequestDto.getLocation() != null ? postUpdateRequestDto.getLocation() : this.location;
        this.latitude = postUpdateRequestDto.getLatitude() != null ? postUpdateRequestDto.getLatitude() : this.latitude;
        this.longitude = postUpdateRequestDto.getLongitude() != null ? postUpdateRequestDto.getLongitude() : this.longitude;
        this.title = postUpdateRequestDto.getTitle() != null ? postUpdateRequestDto.getTitle() : this.title;
        this.description = postUpdateRequestDto.getDescription() != null ? postUpdateRequestDto.getDescription() : this.description;
        this.goal = postUpdateRequestDto.getGoal() != null ? postUpdateRequestDto.getGoal() : this.goal;
        this.otherInfo = postUpdateRequestDto.getOtherInfo() != null ? postUpdateRequestDto.getOtherInfo() : this.otherInfo;
    }


}
