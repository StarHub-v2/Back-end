package com.example.starhub.repository;

import com.example.starhub.entity.ApplicantsEntity;
import com.example.starhub.entity.PostEntity;
import com.example.starhub.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicantsRepository extends JpaRepository<ApplicantsEntity, Long> {
    boolean existsByPostAndAuthor(PostEntity postEntity, UserEntity userEntity);
}
