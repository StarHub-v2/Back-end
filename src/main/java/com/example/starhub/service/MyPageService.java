package com.example.starhub.service;

import com.example.starhub.dto.request.UpdateProfileRequestDto;
import com.example.starhub.dto.response.LikeDto;
import com.example.starhub.dto.response.MeetingSummaryResponseDto;
import com.example.starhub.dto.response.ProfileResponseDto;
import com.example.starhub.entity.MeetingEntity;
import com.example.starhub.entity.UserEntity;
import com.example.starhub.exception.UserNotFoundException;
import com.example.starhub.repository.LikeRepository;
import com.example.starhub.repository.MeetingRepository;
import com.example.starhub.repository.MeetingTechStackRepository;
import com.example.starhub.repository.UserRepository;
import com.example.starhub.response.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MyPageService {

    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;
    private final MeetingTechStackRepository meetingTechStackRepository;
    private final LikeRepository likeRepository;

    /**
     * 공통 검증 로직: 사용자 가져오기
     */
    private UserEntity validateAndGetUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));
    }

    /**
     * 마이페이지 - 사용자 정보 불러오기
     *
     * @param username 사용자명
     * @return 사용자 정보가 담긴 DTO
     */
    @Transactional(readOnly = true)
    public ProfileResponseDto getUserProfile(String username) {
        UserEntity user = validateAndGetUser(username);
        return ProfileResponseDto.fromEntity(user);
    }

    /**
     * 마이페이지 정보 불러오기 - 내가 작성한 모임 목록
     */
    @Transactional(readOnly = true)
    public List<MeetingSummaryResponseDto> getUserRecentMeetings(String username) {
        UserEntity user = validateAndGetUser(username);

        List<MeetingEntity> meetings = meetingRepository.findTop3ByCreatorOrderByCreatedAtDesc(user);

        return meetings.stream().map(meetingEntity -> {
            List<String> techStacks = getTechStacksForMeeting(meetingEntity);
            LikeDto likeDto = getLikeDtoForMeeting(meetingEntity, username);

            return MeetingSummaryResponseDto.fromEntity(meetingEntity, techStacks, likeDto);
        }).collect(Collectors.toList());
    }

    /**
     * 마이페이지 - 프로필 정보 수정하기
     *
     * @param username 사용자명
     * @param updateProfileRequestDto 업데이트할 프로필 정보
     * @return 사용자 정보가 담긴 DTO
     */
    public ProfileResponseDto updateUserProfile(String username, UpdateProfileRequestDto updateProfileRequestDto) {
        UserEntity user = validateAndGetUser(username);
        user.updateProfile(updateProfileRequestDto);

        return ProfileResponseDto.fromEntity(user);
    }

    /**
     * 모임에 연결된 기술 스택을 반환하는 메서드
     *
     * @param meetingEntity 모임 엔티티
     * @return 기술 스택 이름 리스트
     */
    private List<String> getTechStacksForMeeting(MeetingEntity meetingEntity) {
        return meetingTechStackRepository.findByMeeting(meetingEntity).stream()
                .map(meetingTechStack -> meetingTechStack.getTechStack().getName())
                .collect(Collectors.toList());
    }

    /**
     * 모임에 대한 좋아요 정보 및 내가 좋아요를 눌렀는지 여부를 반환하는 메서드
     *
     * @param meetingEntity 모임 엔티티
     * @param username   사용자명
     * @return 좋아요 DTO
     */
    private LikeDto getLikeDtoForMeeting(MeetingEntity meetingEntity, String username) {
        Long likeCount = likeRepository.countByMeeting(meetingEntity);

        Boolean isLiked = likeRepository.existsByMeetingAndUserUsername(meetingEntity, username);

        return LikeDto.builder()
                .likeCount(likeCount)
                .isLiked(isLiked)
                .build();
    }
}
