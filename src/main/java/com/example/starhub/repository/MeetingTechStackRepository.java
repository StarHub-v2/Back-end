package com.example.starhub.repository;

import com.example.starhub.entity.MeetingEntity;
import com.example.starhub.entity.MeetingTechStackEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MeetingTechStackRepository extends JpaRepository<MeetingTechStackEntity, Long> {
    List<MeetingTechStackEntity> findByMeeting(MeetingEntity post);
    void deleteByMeeting(MeetingEntity post);

    @Query("""
        SELECT mts FROM MeetingTechStackEntity mts
        JOIN FETCH mts.meeting m
        LEFT JOIN FETCH mts.techStack ts
        WHERE m.id IN :meetingIds
    """)
    List<MeetingTechStackEntity> findMeetingTechStacksByMeetingIds(@Param("meetingIds") List<Long> meetingIds);

}
