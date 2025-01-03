package com.example.starhub.repository;

import com.example.starhub.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
  Optional<UserEntity> findByName(String name);
  Optional<UserEntity> findByLoginId(String loginId);

}
