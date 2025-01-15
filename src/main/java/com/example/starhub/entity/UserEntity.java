package com.example.starhub.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @PrePersist
    private void prePersist() {
        if (isProfileComplete == null) {
            isProfileComplete = false;
        }
    }

    @Builder
    public UserEntity(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void updateProfile(String profileImage, String nickname, String name, Integer age, String bio, String email, String phoneNumber) {
        this.profileImage = profileImage;
        this.nickname = nickname;
        this.name = name;
        this.age = age;
        this.bio = bio;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.isProfileComplete = true;
    }
}
