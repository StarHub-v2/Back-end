package com.example.starhub.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ApplicationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 지원서 고유 식별자

    @Column(columnDefinition = "TEXT")
    private String content;  // 지원서 내용

    @CreatedDate
    private LocalDateTime createdAt;  // 작성 시간

    @LastModifiedDate
    private LocalDateTime updatedAt;  // 수정 시간

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicant_id", nullable = false)
    private UserEntity applicant;  // 사용자(작성자)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private PostEntity post;  // 해당 포스트

    public void updateContent(String content) {
        this.content = content != null ? content : this.content;
    }
}
