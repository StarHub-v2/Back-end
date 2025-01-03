package com.example.starhub.dto.post;

import com.example.starhub.entity.PostEntity;
import com.example.starhub.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDto {
    private String skill;

    private String place;

    private Float latitude;
    private Float longitude;

    private String progress;

    private String peopleNum;

    private String deadline;


    private String type;

    private Boolean done;

    private String title;

    private String content;

    private LocalDate createdAt;

    private String userName;


    public PostResponseDto(PostEntity post) {
        this.skill=post.getSkill();
        this.place=post.getPlace();
        this.longitude=post.getLongitude();
        this.latitude=post.getLatitude();
        this.progress=post.getProgress();
        this.peopleNum=post.getPeopleNum();
        this.deadline=post.getDeadline();
        this.type=post.getType();
        this.done=post.getDone();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.createdAt = LocalDate.from(post.getCreatedAt());
        this.userName=post.getUser().getName();
    }
}
