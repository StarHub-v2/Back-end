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
     * @return 프로필 정보가 담긴 DTO
     */
    public ProfileResponseDto getUserProfile(String username) {
        return ProfileResponseDto.fromEntity(validateAndGetUser(username));
    }

    /**
     * 마이페이지 - 프로필 정보 수정하기
     *
     * @param username 사용자명
     * @param updateProfileRequestDto 업데이트할 프로필 정보 DTO
     * @return 프로필 정보가 담긴 DTO
     */
    @Transactional
    public ProfileResponseDto updateUserProfile(String username, UpdateProfileRequestDto updateProfileRequestDto) {
        UserEntity user = validateAndGetUser(username);
        user.updateProfile(updateProfileRequestDto);
        return ProfileResponseDto.fromEntity(user);
    }

    /**
     * 마이페이지 - 내가 작성한 모임 목록 최신 3개
     *
     * @param username 사용자명
     * @return 모임 요약 정보가 담긴 DTO 리스트
     */
    public List<MeetingSummaryResponseDto> getUserRecentMeetings(String username) {
        return getRecentMeetings(meetingRepository.findTop3ByCreatorOrderByCreatedAtDesc(validateAndGetUser(username)), username);
    }

    /**
     * 마이페이지 - 내가 좋아요 누른 모임 목록 최신 3개
     *
     * @param username 사용자명
     * @return 모임 요약 정보가 담긴 DTO 리스트
     */
    public List<MeetingSummaryResponseDto> getLikedRecentMeetings(String username) {
        List<MeetingEntity> meetings = likeRepository.findTop3ByUserOrderByCreatedAtDesc(validateAndGetUser(username))
                .stream().map(LikeEntity::getMeeting).collect(Collectors.toList());
        return getRecentMeetings(meetings, username);
    }

    /**
     * 마이페이지 - 내가 참여한 모임 목록 최신 3개
     *
     * @param username 사용자명
     * @return 모임 요약 정보가 담긴 DTO 리스트
     */
    public List<MeetingSummaryResponseDto> getAppliedRecentMeetings(String username) {
        List<MeetingEntity> meetings = applicationRepository.findTop3ByApplicantOrderByCreatedAtDesc(validateAndGetUser(username))
                .stream().map(ApplicationEntity::getMeeting).collect(Collectors.toList());
        return getRecentMeetings(meetings, username);
    }

    /**
     * 내가 작성한 모임 목록 (페이지네이션 적용)
     *
     * @param username 사용자명
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return 모임 요약 정보가 담긴 DTO 리스트 - 페이지네이션 적용
     */
    public Page<MeetingSummaryResponseDto> getCreatedMeetings(String username, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return getMeetingsPage(meetingRepository.findByCreator(validateAndGetUser(username), pageable), username);
    }

    /**
     * 내가 좋아요 누른 모임 목록 (페이지네이션 적용)
     *
     * @param username 사용자명
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return 모임 요약 정보가 담긴 DTO 리스트 - 페이지네이션 적용
     */
    public Page<MeetingSummaryResponseDto> getLikedMeetings(String username, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<LikeEntity> likedMeetingsPage = likeRepository.findByUser(validateAndGetUser(username), pageable);
        return getMeetingsPage(likedMeetingsPage.map(LikeEntity::getMeeting), username);
    }

    /**
     * 내가 참여한 모임 목록 (페이지네이션 적용)
     *
     * @param username 사용자명
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return 모임 요약 정보가 담긴 DTO 리스트 - 페이지네이션 적용
     */
    public Page<MeetingSummaryResponseDto> getAppliedMeetings(String username, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<ApplicationEntity> appliedMeetingsPage = applicationRepository.findByApplicant(validateAndGetUser(username), pageable);
        return getMeetingsPage(appliedMeetingsPage.map(ApplicationEntity::getMeeting), username);
    }

    /**
     * 최근 모임 목록을 가져오는 공통 로직
     */
    private List<MeetingSummaryResponseDto> getRecentMeetings(List<MeetingEntity> meetings, String username) {
        return meetings.stream().map(meeting ->
                MeetingSummaryResponseDto.fromEntity(meeting, getTechStacksForMeeting(meeting), getLikeDtoForMeeting(meeting, username))
        ).collect(Collectors.toList());
    }

    /**
     * 페이징된 모임 목록을 가져오는 공통 로직
     */
    private Page<MeetingSummaryResponseDto> getMeetingsPage(Page<MeetingEntity> meetingsPage, String username) {
        return meetingsPage.map(meeting ->
                MeetingSummaryResponseDto.fromEntity(meeting, getTechStacksForMeeting(meeting), getLikeDtoForMeeting(meeting, username))
        );
    }

    /**
     * 모임에 연결된 기술 스택을 반환하는 메서드
     */
    private List<String> getTechStacksForMeeting(MeetingEntity meetingEntity) {
        return meetingTechStackRepository.findByMeeting(meetingEntity).stream()
                .map(meetingTechStack -> meetingTechStack.getTechStack().getName())
                .collect(Collectors.toList());
    }

    /**
     * 모임에 대한 좋아요 정보 및 내가 좋아요를 눌렀는지 여부를 반환하는 메서드
     */
    private LikeDto getLikeDtoForMeeting(MeetingEntity meetingEntity, String username) {
        return LikeDto.builder()
                .likeCount(likeRepository.countByMeeting(meetingEntity))
                .isLiked(likeRepository.existsByMeetingAndUserUsername(meetingEntity, username))
                .build();
    }
}

