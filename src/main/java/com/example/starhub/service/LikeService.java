package com.example.starhub.service;

import com.example.starhub.entity.LikeEntity;
import com.example.starhub.entity.MeetingEntity;
import com.example.starhub.entity.UserEntity;
import com.example.starhub.exception.LikeAlreadyExistsException;
import com.example.starhub.exception.MeetingNotFoundException;
import com.example.starhub.exception.UserNotFoundException;
import com.example.starhub.repository.LikeRepository;
import com.example.starhub.repository.MeetingRepository;
import com.example.starhub.repository.UserRepository;
import com.example.starhub.response.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository;

    /**
     * 공통 검증 로직: 모임 가져오기
     */
    private MeetingEntity validateAndGetMeeting(Long meetingId) {
        MeetingEntity meetingEntity = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new MeetingNotFoundException(ErrorCode.MEETING_NOT_FOUND));

        return meetingEntity;
    }

    /**
     * 공통 검증 로직: 사용자 가져오기
     */
    private UserEntity validateAndGetUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));
    }

    public void createLike(String username, Long meetingId) {
        UserEntity userEntity = validateAndGetUser(username);
        MeetingEntity meetingEntity = validateAndGetMeeting(meetingId);

        if (likeRepository.existsByUserAndMeeting(userEntity, meetingEntity)) {
            throw new LikeAlreadyExistsException(ErrorCode.LIKE_ALREADY_EXISTS);
        }

        LikeEntity likeEntity = LikeEntity.createLike(userEntity, meetingEntity);

        likeRepository.save(likeEntity);
    }
}
