package com.example.starhub.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer postId; // 포스트 id

    @Column(nullable = false, length=20)
    private String skill;

    @Column(nullable = false, length=50)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false, length=20)
    private String progress;

    @Column(nullable = false, length=10)
    private String peopleNum;

    @Column(nullable = false, length=20)
    private String deadline;

    @CreatedDate
    private LocalDate createdAt;

    @Column(nullable = false, length=20)
    private String type;

    @Column(nullable = false, length=255)
    private String place;

    @Column(nullable = false)
    private Float latitude;

    @Column(nullable = false)
    private Float longitude;

    @Column
    private Boolean done;

}