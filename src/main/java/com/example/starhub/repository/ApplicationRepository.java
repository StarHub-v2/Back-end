package com.example.starhub.repository;

import com.example.starhub.entity.ApplicationEntity;
import com.example.starhub.entity.PostEntity;
import com.example.starhub.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<ApplicationEntity, Long> {
    boolean existsByPostAndApplicant(PostEntity postEntity, UserEntity userEntity);
}
