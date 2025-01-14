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
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username; // 사용자 아이디

    @Column(nullable = false)
    private String password;

    @Column(length = 255)
    private String profileImage;

    @Column(length = 50)
    private String nickname;

    @Column(length = 50)
    private String name; // 사용자 이름

    private Integer age;

    @Column(length = 255)
    private String bio;

    @Column(length = 100)
    private String email;

    @Column(length = 15)
    private String phoneNumber;

    @Column(nullable = false, updatable = false)
    private LocalDateTime created_at;

    @Column(nullable = false)
    private LocalDateTime updated_at;

    private Boolean isProfileComplete;
}
