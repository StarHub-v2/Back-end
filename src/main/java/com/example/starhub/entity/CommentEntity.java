package com.example.starhub.entity;

import com.example.starhub.dto.comment.CommentRequestDto;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;

import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String content;

    @CreatedDate
    private LocalDate createdAt;

    @Column
    private boolean pick;


    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JsonIgnore
    @JoinColumn(name = "post_id")
    private PostEntity post;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public CommentEntity(UserEntity user,PostEntity post, CommentRequestDto requestDto){
        this.content=requestDto.getContent();
        this.createdAt= requestDto.getCreatedAt();
        this.pick= requestDto.isPick();
        this.user=user;
        this.post=post;
    }


}
