package com.example.starhub.repository;

import com.example.starhub.entity.MeetingEntity;
import com.example.starhub.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MeetingRepository extends JpaRepository<MeetingEntity, Long> {

    @Query("SELECT m FROM MeetingEntity m JOIN FETCH m.creator WHERE m.id = :meetingId")
    Optional<MeetingEntity> findWithCreatorById(@Param("meetingId") Long meetingId);
    List<MeetingEntity> findTop3ByCreatorOrderByCreatedAtDesc(UserEntity creator);
    Page<MeetingEntity> findByCreator(UserEntity creator, Pageable pageable);
}
