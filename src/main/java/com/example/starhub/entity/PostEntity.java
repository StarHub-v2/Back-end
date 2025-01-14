package com.example.starhub.entity;

import com.example.starhub.entity.enums.Duration;
import com.example.starhub.entity.enums.RecruitmentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    private LocalDateTime createdAt;  // 생성일

    private LocalDateTime updatedAt;  // 수정일

    private Boolean isConfirmed;  // 모임 확정 여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private UserEntity creator;  // 사용자(작성자)

}
