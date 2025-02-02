package com.example.starhub.service;

import com.example.starhub.dto.request.UpdateProfileRequestDto;
import com.example.starhub.dto.response.LikeDto;
import com.example.starhub.dto.response.MeetingSummaryResponseDto;
import com.example.starhub.dto.response.ProfileResponseDto;
import com.example.starhub.entity.ApplicationEntity;
import com.example.starhub.entity.LikeEntity;
import com.example.starhub.entity.MeetingEntity;
import com.example.starhub.entity.UserEntity;
import com.example.starhub.exception.UserNotFoundException;
import com.example.starhub.repository.*;
import com.example.starhub.response.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MyPageService {

    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;
    private final MeetingTechStackRepository meetingTechStackRepository;
    private final LikeRepository likeRepository;
    private final ApplicationRepository applicationRepository;

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
    public ProfileResponseDto getUserProfile(String username) {
        UserEntity user = validateAndGetUser(username);
        return ProfileResponseDto.fromEntity(user);
    }

    /**
     * 마이페이지 - 프로필 정보 수정하기
     *
     * @param username 사용자명
     * @param updateProfileRequestDto 업데이트할 프로필 정보
     * @return 사용자 정보가 담긴 DTO
     */
    @Transactional
    public ProfileResponseDto updateUserProfile(String username, UpdateProfileRequestDto updateProfileRequestDto) {
        UserEntity user = validateAndGetUser(username);
        user.updateProfile(updateProfileRequestDto);

        return ProfileResponseDto.fromEntity(user);
    }

    /**
     * 마이페이지 정보 불러오기 - 내가 작성한 모임 목록 최신 3개
     *
     * @param username 사용자명
     * @return 요약된 모임 정보 리스트
     */
    public List<MeetingSummaryResponseDto> getUserRecentMeetings(String username) {
        UserEntity user = validateAndGetUser(username);

        List<MeetingEntity> meetings = meetingRepository.findTop3ByCreatorOrderByCreatedAtDesc(user);

        return meetings.stream().map(meeting -> {
            List<String> techStacks = getTechStacksForMeeting(meeting);
            LikeDto likeDto = getLikeDtoForMeeting(meeting, username);

            return MeetingSummaryResponseDto.fromEntity(meeting, techStacks, likeDto);
        }).collect(Collectors.toList());
    }

    /**
     * 마이페이지 정보 불러오기 - 내가 좋아요 누른 모임 목록 최신 3개
     *
     * @param username 사용자명
     * @return 요약된 모임 정보 리스트
     */
    public List<MeetingSummaryResponseDto> getLikedRecentMeetings(String username) {
        UserEntity user = validateAndGetUser(username);

        List<MeetingEntity> meetings = likeRepository.findTop3ByUserOrderByCreatedAtDesc(user)
                .stream().map(LikeEntity::getMeeting).collect(Collectors.toList());

        return meetings.stream().map(meeting -> {
            List<String> techStacks = getTechStacksForMeeting(meeting);
            LikeDto likeDto = getLikeDtoForMeeting(meeting, username);

            return MeetingSummaryResponseDto.fromEntity(meeting, techStacks, likeDto);
        }).collect(Collectors.toList());
    }

    /**
     * 마이페이지 정보 불러오기 - 내가 참여한 모임 목록 최신 3개
     *
     * @param username 사용자명
     * @return 요약된 모임 정보 리스트
     */
    public List<MeetingSummaryResponseDto> getAppliedRecentMeetings(String username) {
        UserEntity user = validateAndGetUser(username);

        List<MeetingEntity> meetings = applicationRepository.findTop3ByApplicantOrderByCreatedAtDesc(user)
                .stream().map(ApplicationEntity::getMeeting).collect(Collectors.toList());

        return meetings.stream().map(meeting -> {
            List<String> techStacks = getTechStacksForMeeting(meeting);
            LikeDto likeDto = getLikeDtoForMeeting(meeting, username);

            return MeetingSummaryResponseDto.fromEntity(meeting, techStacks, likeDto);
        }).collect(Collectors.toList());
    }

    /**
     * 내가 작성한 모임 목록 (페이지네이션 적용)
     */
    public Page<MeetingSummaryResponseDto> getCreatedMeetings(String username, int page, int size) {
        UserEntity user = validateAndGetUser(username);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        return meetingRepository.findByCreator(user, pageable)
                .map(meeting -> {
                    List<String> techStacks = getTechStacksForMeeting(meeting);
                    LikeDto likeDto = getLikeDtoForMeeting(meeting, username);

                    return MeetingSummaryResponseDto.fromEntity(meeting, techStacks, likeDto);
                });
    }

    /**
     * 내가 좋아요 누른 모임 목록 (페이지네이션 적용)
     */
    public Page<MeetingSummaryResponseDto> getLikedMeetings(String username, int page, int size) {
        UserEntity user = validateAndGetUser(username);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return likeRepository.findByUser(user, pageable)
                .map(like -> {
                    MeetingEntity meeting = like.getMeeting();
                    List<String> techStacks = getTechStacksForMeeting(meeting);
                    LikeDto likeDto = getLikeDtoForMeeting(meeting, username);

                    return MeetingSummaryResponseDto.fromEntity(meeting, techStacks, likeDto);
                });
    }

    /**
     * 내가 참여한 모임 목록 (페이지네이션 적용)
     */
    public Page<MeetingSummaryResponseDto> getAppliedMeetings(String username, int page, int size) {
        UserEntity user = validateAndGetUser(username);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return applicationRepository.findByApplicant(user, pageable)
                .map(participation -> {
                    MeetingEntity meeting = participation.getMeeting();
                    List<String> techStacks = getTechStacksForMeeting(meeting);
                    LikeDto likeDto = getLikeDtoForMeeting(meeting, username);

                    return MeetingSummaryResponseDto.fromEntity(meeting, techStacks, likeDto);
                });
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
