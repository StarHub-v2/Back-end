package com.example.starhub.service;

import com.example.starhub.dto.request.ApplicationRequestDto;
import com.example.starhub.dto.response.ApplicationResponseDto;
import com.example.starhub.entity.ApplicationEntity;
import com.example.starhub.entity.MeetingEntity;
import com.example.starhub.entity.UserEntity;
import com.example.starhub.exception.*;
import com.example.starhub.repository.ApplicationRepository;
import com.example.starhub.repository.MeetingRepository;
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
public class ApplicationService {

    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;
    private final ApplicationRepository applicationRepository;

    /**
     * 공통 검증 로직: 모임 가져오기 및 상태 확인
     * - 모임이 확정이 안된 상태이여야 함
     * - 개설자 정보와 같이 JOIN FETCH
     */
    private MeetingEntity validateAndGetMeeting(Long meetingId) {
        MeetingEntity meetingEntity = meetingRepository.findWithCreatorById(meetingId)
                .orElseThrow(() -> new MeetingNotFoundException(ErrorCode.MEETING_NOT_FOUND));

        if (meetingEntity.getIsConfirmed()) {
            throw new StudyConfirmedException(ErrorCode.STUDY_CONFIRMED);
        }

        return meetingEntity;
    }

    /**
     * 공통 검증 로직: 사용자가 모임의 개설자인지 확인
     */
    private void validateMeetingCreator(MeetingEntity meetingEntity, String username) {
        if (!meetingEntity.getCreator().getUsername().equals(username)) {
            throw new CreatorAuthorizationException(ErrorCode.MEETING_FORBIDDEN);
        }
    }

    /**
     * 공통 검증 로직: 사용자가 모임의 지원자인지 확인
     */
    private void validateMeetingApplicant(MeetingEntity meetingEntity, String username) {
        if (meetingEntity.getCreator().getUsername().equals(username)) {
            throw new ApplicantAuthorizationException(ErrorCode.APPLICATION_FORBIDDEN);
        }
    }

    /**
     * 공통 검증 로직: 사용자 가져오기
     */
    private UserEntity validateAndGetUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));
    }

    /**
     * 지원서 작성하기
     * - 개설자는 지원서를 작성하지 못합니다
     *
     * @param username JWT를 통해 인증된 사용자명
     * @param meetingId 모임 아이디
     * @param applicationRequestDto 지원서 관련 정보가 담긴 DTO
     * @return 지원서 응답에 대한 DTO
     */
    public ApplicationResponseDto createApplication(String username, Long meetingId, ApplicationRequestDto applicationRequestDto) {

        UserEntity userEntity = validateAndGetUser(username);
        MeetingEntity meetingEntity = validateAndGetMeeting(meetingId);

        // 개설자일 경우 409 예외 처리
        if(meetingEntity.getCreator().getUsername().equals(username)) {
            throw new MeetingCreatorCannotApplyException(ErrorCode.MEETING_CREATOR_CANNOT_APPLY);
        }

        // 이미 지원한 경우 예외 처리
        if(applicationRepository.existsByMeetingAndApplicant(meetingEntity, userEntity)) {
            throw new DuplicateApplicationException(ErrorCode.DUPLICATE_APPLICATION);
        }

        ApplicationEntity applicationEntity = ApplicationEntity.createApplication(userEntity, meetingEntity, applicationRequestDto);
        ApplicationEntity savedApplicationEntity = applicationRepository.save(applicationEntity);

        return ApplicationResponseDto.fromEntity(savedApplicationEntity);
    }

    /**
     * 지원서 목록 불러오기
     * - 개설자만 지원서 목록을 불러올 수 있음
     *
     * @param username JWT를 통해 인증된 사용자명
     * @param meetingId 모임 아이디
     * @return 지원서 응답에 대한 목록 DTO
     */
    @Transactional(readOnly = true)
    public List<ApplicationResponseDto> getApplicationList(String username, Long meetingId) {

        MeetingEntity meetingEntity = validateAndGetMeeting(meetingId);

        // 개설자가 아닌 경우 예외 처리
        validateMeetingCreator(meetingEntity, username);

        List<ApplicationEntity> applicantEntities = applicationRepository.findByMeeting(meetingEntity);

        return applicantEntities.stream()
                .map(applicant -> ApplicationResponseDto.fromEntity(applicant))
                .collect(Collectors.toList());
    }

    /**
     * 특정 지원서의 상세 정보를 불러오는 메소드
     * - 작성자만 불러올 수 있음
     *
     * @param username JWT를 통해 인증된 사용자명
     * @param meetingId 모임 아이디
     * @return 지원서 상세 정보 DTO
     */
    @Transactional(readOnly = true)
    public ApplicationResponseDto getApplicationDetail(String username, Long meetingId) {

        UserEntity userEntity = validateAndGetUser(username);
        MeetingEntity meetingEntity = validateAndGetMeeting(meetingId);

        // 개설자가 아님을 확인해야 함
        validateMeetingApplicant(meetingEntity, username);

        ApplicationEntity applicationEntity = applicationRepository.findByApplicantAndMeeting(userEntity, meetingEntity)
                .orElseThrow(() -> new ApplicationNotFoundException(ErrorCode.APPLICATION_NOT_FOUND));

        // 지원서 상세 정보 반환
        return ApplicationResponseDto.fromEntity(applicationEntity);
    }

    /**
     * 지원서 수정하기
     * - 작성자만 수정할 수 있음
     *
     * @param username JWT를 통해 인증된 사용자명
     * @param meetingId 모임 아이디
     * @param applicationRequestDto 수정할 지원서 내용
     * @return 수정된 지원서에 대한 DTO
     */
    public ApplicationResponseDto updateApplication(String username, Long meetingId, ApplicationRequestDto applicationRequestDto) {

        UserEntity userEntity = validateAndGetUser(username);
        MeetingEntity meetingEntity = validateAndGetMeeting(meetingId);

        // 개설자가 아님을 확인해야 함
        validateMeetingApplicant(meetingEntity, username);

        ApplicationEntity applicationEntity = applicationRepository.findByApplicantAndMeeting(userEntity, meetingEntity)
                .orElseThrow(() -> new ApplicationNotFoundException(ErrorCode.APPLICATION_NOT_FOUND));

        applicationEntity.updateContent(applicationRequestDto.getContent());

        return ApplicationResponseDto.fromEntity(applicationEntity);
    }

    /**
     * 지원서 삭제하기
     * - 작성자만 삭제할 수 있음
     *
     * @param username JWT를 통해 인증된 사용자명
     * @param meetingId 모임 아이디
     */
    public void deleteApplication(String username, Long meetingId) {

        UserEntity userEntity = validateAndGetUser(username);
        MeetingEntity meetingEntity = validateAndGetMeeting(meetingId);

        // 개설자가 아님을 확인해야 함
        validateMeetingApplicant(meetingEntity, username);

        ApplicationEntity applicationEntity = applicationRepository.findByApplicantAndMeeting(userEntity, meetingEntity)
                .orElseThrow(() -> new ApplicationNotFoundException(ErrorCode.APPLICATION_NOT_FOUND));

        applicationRepository.delete(applicationEntity);
    }
}
