package com.example.starhub.entity;

import com.example.starhub.dto.request.UpdateProfileRequestDto;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
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

    private String role; // 역할

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt; // 생성일

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt; // 수정일

    private Boolean isProfileComplete; // 프로필 생성 여부

    public static UserEntity createUser(String username, String password) {
        UserEntity user = new UserEntity();
        user.username = username;
        user.password = password;
        user.role = "ROLE_USER";
        user.isProfileComplete = false;
        return user;
    }

    public static UserEntity createUserWithRole(String username, String role) {
        UserEntity user = new UserEntity();
        user.username = username;
        user.role = role;
        return user;
    }

    public void createProfile(String profileImage, String nickname, String name, Integer age, String bio, String email, String phoneNumber) {
        this.profileImage = profileImage;
        this.nickname = nickname;
        this.name = name;
        this.age = age;
        this.bio = bio;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.isProfileComplete = true;
    }

    public void updateProfile(UpdateProfileRequestDto updateProfileRequestDto) {
        this.profileImage = updateValue(updateProfileRequestDto.getProfileImage(), this.profileImage);
        this.nickname = updateValue(updateProfileRequestDto.getNickname(), this.nickname);
        this.name = updateValue(updateProfileRequestDto.getName(), this.name);
        this.age = updateValue(updateProfileRequestDto.getAge(), this.age);
        this.bio = updateValue(updateProfileRequestDto.getBio(), this.bio);
        this.email = updateValue(updateProfileRequestDto.getEmail(), this.email);
        this.phoneNumber = updateValue(updateProfileRequestDto.getPhoneNumber(), this.phoneNumber);
    }

    private <T> T updateValue(T newValue, T currentValue) {
        return newValue != null ? newValue : currentValue;
    }



}
