package com.example.starhub.service;

import com.example.starhub.dto.post.PostListResponseDto;
import com.example.starhub.dto.post.PostRequestDto;
import com.example.starhub.dto.post.PostResponseDto;

import java.util.List;


public interface PostService {

    PostResponseDto createPost(PostRequestDto requestDto);

    List<PostListResponseDto> findAllPost();

    PostResponseDto findOnePost(Integer id);
}
