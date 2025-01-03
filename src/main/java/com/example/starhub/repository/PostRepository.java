package com.example.starhub.repository;

import com.example.starhub.dto.post.PostListResponseDto;
import com.example.starhub.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<PostEntity, Integer> {
    public List<PostListResponseDto> findAllByOrderByCreatedAtDesc();
}
