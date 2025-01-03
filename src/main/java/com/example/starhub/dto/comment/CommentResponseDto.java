package com.example.starhub.dto.comment;

import com.example.starhub.entity.CommentEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDto {
    private Integer commentId;
    private Integer postId;
    private String content;

    private LocalDate createdAt;
    private String userName;

    private boolean pick;


    public CommentResponseDto(CommentEntity comment){
        this.commentId = comment.getId();
        this.content=comment.getContent();
        this.createdAt=LocalDate.from(comment.getCreatedAt());
        this.pick=comment.isPick();
        this.postId=comment.getPost().getPostId();
        this.userName=comment.getUser().getName();
    }
}
