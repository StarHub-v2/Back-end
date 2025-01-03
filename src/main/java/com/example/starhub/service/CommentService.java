package com.example.starhub.service;

import com.example.starhub.dto.comment.CommentRequestDto;
import com.example.starhub.dto.comment.CommentResponseDto;
import com.example.starhub.entity.UserEntity;
import com.example.starhub.projection.comment.GetCommentList;

import java.util.List;

public interface CommentService {


    CommentResponseDto createComment(CommentRequestDto requestDto);

    List<CommentResponseDto> readAllComments(Integer id);

    List<GetCommentList> pickComments(List<Integer> commentIdList);

    List<GetCommentList> getPickedComments(Integer postId);


}
