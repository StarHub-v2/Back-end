package com.example.starhub.repository;

import com.example.starhub.entity.TechStackEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TechStackRepository extends JpaRepository<TechStackEntity, Long> {
    Optional<TechStackEntity> findByName(String name);
}
