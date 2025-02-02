package com.example.starhub.repository;

import com.example.starhub.entity.ApplicationEntity;
import com.example.starhub.entity.MeetingEntity;
import com.example.starhub.entity.UserEntity;
import com.example.starhub.entity.enums.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<ApplicationEntity, Long> {
    boolean existsByMeetingAndApplicant(MeetingEntity meetingEntity, UserEntity userEntity);

    @Query("SELECT a FROM ApplicationEntity a JOIN FETCH a.applicant WHERE a.meeting = :meetingEntity")
    List<ApplicationEntity> findByMeeting(MeetingEntity meetingEntity);

    Optional<ApplicationEntity> findByApplicantAndMeeting(UserEntity userEntity, MeetingEntity meetingEntity);

    List<ApplicationEntity> findByMeetingAndStatus(MeetingEntity meetingEntity, ApplicationStatus status);
    List<ApplicationEntity> findTop3ByApplicantOrderByCreatedAtDesc(UserEntity user);
}
