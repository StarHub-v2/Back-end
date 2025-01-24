package com.example.starhub.repository;

import com.example.starhub.entity.LikeEntity;
import com.example.starhub.entity.MeetingEntity;
import com.example.starhub.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<LikeEntity, Long> {
    Long countByMeeting(MeetingEntity meetingEntity);
    boolean existsByMeetingAndUserUsername(MeetingEntity meetingEntity, String username);
    void deleteByMeeting(MeetingEntity meetingEntity);

    boolean existsByUserAndMeeting(UserEntity user, MeetingEntity meeting);
    Optional<LikeEntity> findByUserAndMeeting(UserEntity user, MeetingEntity meeting);
}
