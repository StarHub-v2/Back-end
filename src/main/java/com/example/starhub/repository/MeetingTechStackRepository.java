package com.example.starhub.repository;

import com.example.starhub.entity.MeetingEntity;
import com.example.starhub.entity.MeetingTechStackEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeetingTechStackRepository extends JpaRepository<MeetingTechStackEntity, Long> {
    List<MeetingTechStackEntity> findByMeeting(MeetingEntity post);
    void deleteByMeeting(MeetingEntity post);
}
