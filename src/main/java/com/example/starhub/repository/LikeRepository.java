package com.example.starhub.repository;

import com.example.starhub.entity.LikeEntity;
import com.example.starhub.entity.MeetingEntity;
import com.example.starhub.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<LikeEntity, Long> {
    Long countByMeeting(MeetingEntity meetingEntity);
    boolean existsByMeetingAndUserUsername(MeetingEntity meetingEntity, String username);
    void deleteByMeeting(MeetingEntity meetingEntity);

    boolean existsByUserAndMeeting(UserEntity user, MeetingEntity meeting);
    Optional<LikeEntity> findByUserAndMeeting(UserEntity user, MeetingEntity meeting);

    @Query("SELECT l FROM LikeEntity l " +
            "JOIN FETCH l.meeting m " +
            "WHERE l.user = :user " +
            "ORDER BY l.createdAt DESC")
    List<LikeEntity> findTop3ByUserWithMeeting(@Param("user") UserEntity user);
}
