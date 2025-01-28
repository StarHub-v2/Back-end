package com.example.starhub.repository;

import com.example.starhub.entity.TechStackEntity;
import com.example.starhub.entity.enums.TechCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TechStackRepository extends JpaRepository<TechStackEntity, Long> {
    Optional<TechStackEntity> findByName(String name);
    List<TechStackEntity> findByCategoryNot(TechCategory category);
    List<TechStackEntity> findAllByNameIn(List<String> names);
}
