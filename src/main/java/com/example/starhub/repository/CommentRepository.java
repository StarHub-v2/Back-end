package com.example.starhub.repository;

import com.example.starhub.dto.comment.CommentResponseDto;
import com.example.starhub.entity.CommentEntity;
import com.example.starhub.projection.comment.GetCommentList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {
    List<CommentResponseDto> findAllByPostPostId(Integer postId);
    List<CommentEntity> findAllByIdInAndPickIsTrue(List<Integer> commentIdList);

    List<CommentEntity> findByPickIsTrueAndPostPostId(Integer postId);

}
