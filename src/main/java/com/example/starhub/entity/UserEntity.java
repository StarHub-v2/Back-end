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
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 유저 고유 식별자

    @Column(nullable = false, unique = true, length = 50)
    private String username; // 사용자 아이디

    @Column(nullable = false)
    private String password; // 비밀번호

    @Column(length = 255)
    private String profileImage; // 프로필 이미지

    @Column(length = 50)
    private String nickname; // 닉네임

    @Column(length = 50)
    private String name; // 사용자 이름

    private Integer age; // 나이

    @Column(length = 255)
    private String bio; // 한 줄 소개

    @Column(length = 100)
    private String email; // 이메일

    @Column(length = 15)
    private String phoneNumber; // 전화번호

    @Column(nullable = false, updatable = false)
    private LocalDateTime created_at; // 생성일

    @Column(nullable = false)
    private LocalDateTime updated_at; // 수정일

    private Boolean isProfileComplete; // 프로필 생성 여부
}
