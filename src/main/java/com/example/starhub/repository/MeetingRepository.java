package com.example.starhub.repository;

import com.example.starhub.entity.MeetingEntity;
import com.example.starhub.entity.UserEntity;
import com.example.starhub.entity.enums.RecruitmentType;
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

    @Query("SELECT m FROM MeetingEntity m " +
            "LEFT JOIN LikeEntity l ON l.meeting = m " +
            "WHERE m.recruitmentType = :recruitmentType " +
            "AND m.isConfirmed = false " +
            "GROUP BY m " +
            "ORDER BY COUNT(1) DESC")
    List<MeetingEntity> findTop3PopularMeeting(RecruitmentType recruitmentType, Pageable pageable);

    @Query("SELECT m FROM MeetingEntity m " +
            "LEFT JOIN LikeEntity l ON l.meeting = m " +
            "WHERE m.isConfirmed = false " +
            "AND m.endDate > CURRENT_TIMESTAMP " +
            "GROUP BY m " +
            "ORDER BY m.endDate ASC, COUNT(l) DESC")
    List<MeetingEntity> findTop3ExpiringPopularMeetings(Pageable pageable);



}
