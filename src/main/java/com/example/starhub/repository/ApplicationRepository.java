package com.example.starhub.repository;

import com.example.starhub.entity.ApplicationEntity;
import com.example.starhub.entity.MeetingEntity;
import com.example.starhub.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<ApplicationEntity, Long> {
    boolean existsByMeetingAndApplicant(MeetingEntity meetingEntity, UserEntity userEntity);
    List<ApplicationEntity> findByMeeting(MeetingEntity meetingEntity);
}
