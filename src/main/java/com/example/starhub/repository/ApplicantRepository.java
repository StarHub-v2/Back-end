package com.example.starhub.repository;

import com.example.starhub.entity.ApplicantEntity;
import com.example.starhub.entity.PostEntity;
import com.example.starhub.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicantRepository extends JpaRepository<ApplicantEntity, Long> {
    boolean existsByPostAndAuthor(PostEntity postEntity, UserEntity userEntity);
}
