package com.example.starhub.dto.comment;

import com.example.starhub.entity.CommentEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class CommentRequestDto {

    private String content;

    private LocalDate createdAt;

    private boolean pick;

    private Integer postId;

    private Integer userId;




}
