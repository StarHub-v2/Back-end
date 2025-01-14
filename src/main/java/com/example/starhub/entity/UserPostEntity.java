package com.example.starhub.entity;

import com.example.starhub.entity.enums.ApplicationStatus;
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
public class UserPostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status; // 지원상태(지원, 확정)

    private LocalDateTime createdAt;  // 지원한 시간

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;  // 사용자

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private PostEntity post;  // 스터디
}
