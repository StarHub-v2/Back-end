package com.example.starhub.repository;

import com.example.starhub.entity.LikeEntity;
import com.example.starhub.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<LikeEntity, Long> {
    Long countByPost(PostEntity postEntity);
    Boolean existsByPostAndUserUsername(PostEntity postEntity, String username);
    void deleteByPost(PostEntity postEntity);
}
