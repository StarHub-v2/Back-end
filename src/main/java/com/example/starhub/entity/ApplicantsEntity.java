package com.example.starhub.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicantsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 댓글 고유 식별자

    @Column(columnDefinition = "TEXT")
    private String content;  // 지원서 내용

    private LocalDateTime createdAt;  // 작성 시간

    private LocalDateTime updatedAt;  // 수정 시간

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;  // 작성자 (유저와 연관)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private PostEntity post;  // 관련 스터디 (스터디 및 프로젝트와 연관)
}
