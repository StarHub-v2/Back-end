package com.example.starhub.repository;

import com.example.starhub.entity.PostEntity;
import com.example.starhub.entity.PostTechStackEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostTechStackRepository extends JpaRepository<PostTechStackEntity, Long> {
    List<PostTechStackEntity> findByPost(PostEntity post);
    void deleteByPost(PostEntity post);
}
