package com.example.starhub.dto.post;

import com.example.starhub.entity.PostEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PostListResponseDto {
    private Integer postId;
    private String skill;
    private String place;
    private Float latitude;
    private Float longitude;

    private String progress;

    private String peopleNum;

    private String deadline;

    private String type;

    private String title;

    private LocalDate createdAt;

    private String userName;

    public PostListResponseDto(PostEntity post) {
        this.postId=post.getPostId();
        this.skill=post.getSkill();
        this.place=post.getPlace();
        this.longitude=post.getLongitude();
        this.latitude=post.getLatitude();
        this.progress=post.getProgress();
        this.peopleNum=post.getPeopleNum();
        this.deadline=post.getDeadline();
        this.type=post.getType();
        this.createdAt= LocalDate.from(post.getCreatedAt());
        this.title = post.getTitle();
        this.userName=post.getUser().getName();
    }

}