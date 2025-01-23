package com.example.starhub.service;

import com.example.starhub.dto.request.ConfirmMeetingRequestDto;
import com.example.starhub.dto.request.CreateMeetingRequestDto;
import com.example.starhub.dto.request.MeetingUpdateRequestDto;
import com.example.starhub.dto.response.*;
import com.example.starhub.entity.*;
import com.example.starhub.entity.enums.ApplicationStatus;
import com.example.starhub.entity.enums.TechCategory;
import com.example.starhub.exception.*;
import com.example.starhub.repository.*;
import com.example.starhub.response.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository;
    private final TechStackRepository techStackRepository;
    private final MeetingTechStackRepository meetingTechStackRepository;
    private final LikeRepository likeRepository;
    private final ApplicationRepository applicationRepository;

    /**
     * 공통 검증 로직: 게시글 가져오기 및 상태 확인
     */
    private MeetingEntity validateAndGetMeeting(Long meetingId) {
        MeetingEntity meetingEntity = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new MeetingNotFoundException(ErrorCode.MEETING_NOT_FOUND));

        if (meetingEntity.getIsConfirmed()) {
            throw new StudyConfirmedException(ErrorCode.STUDY_CONFIRMED);
        }

        return meetingEntity;
    }

    /**
     * 공통 검증 로직: 게시글 가져오기 및 상태 확인
     */
    private MeetingEntity validateAndGetConfirmedMeeting(Long meetingId) {
        MeetingEntity meetingEntity = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new MeetingNotFoundException(ErrorCode.MEETING_NOT_FOUND));

        if (!meetingEntity.getIsConfirmed()) {
            throw new StudyNotConfirmedException(ErrorCode.STUDY_NOT_CONFIRMED);
        }

        return meetingEntity;
    }

    /**
     * 공통 검증 로직: 사용자가 게시글의 개설자인지 확인
     */
    private void validateMeetingCreator(MeetingEntity meetingEntity, String username) {
        if (!meetingEntity.getCreator().getUsername().equals(username)) {
            throw new CreatorAuthorizationException(ErrorCode.MEETING_FORBIDDEN);
        }
    }

    /**
     * 새로운 모임(스터디/프로젝트)를 생성하기
     *
     * @param username JWT를 통해 인증된 사용자명
     * @param createMeetingRequestDto 모임 생성에 필요한 데이터를 담고 있는 요청 DTO
     * @return 모임에 대한 응답 DTO
     */
    public MeetingResponseDto createMeeting(String username, CreateMeetingRequestDto createMeetingRequestDto) {

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

        MeetingEntity meetingEntity = MeetingEntity.createMeeting(user, createMeetingRequestDto);
        MeetingEntity savedMeeting = meetingRepository.save(meetingEntity);

        // 기술 스택 정보를 처리하여 모임와 연결
        saveMeetingTechStacks(savedMeeting, createMeetingRequestDto);

        // 저장된 모임에 연결된 기술 스택 이름들을 리스트로 반환
        List<String> techStacks = meetingTechStackRepository.findByMeeting(savedMeeting).stream()
                .map(meetingTechStack -> meetingTechStack.getTechStack().getName())
                .toList();

        return MeetingResponseDto.fromEntity(savedMeeting, techStacks);
    }

    /**
     * 모임 목록 불러오기 (메인 화면에 쓰일 API)
     * - 모임 요약 정보가 담긴 목록으로 제공
     * - 페이지네이션을 적용하고, 생성일 기준 내림차순으로 정렬
     *
     * @param username JWT를 통해 인증된 사용자명
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return 모임 목록 응답 DTO
     */
    @Transactional(readOnly = true)
    public Page<MeetingSummaryResponseDto> getMeetingList(String username, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Order.desc("createdAt")));
        Page<MeetingEntity> meetingPage = meetingRepository.findAll(pageRequest);

        return meetingPage.map(meetingEntity -> {
            List<String> techStacks = getTechStacksForMeeting(meetingEntity);
            LikeDto likeDto = getLikeDtoForMeeting(meetingEntity, username);

            return MeetingSummaryResponseDto.fromEntity(meetingEntity, techStacks, likeDto);
        });
    }

    /**
     * 특정 모임의 상세 정보를 가져옵니다.
     * - 모임의 생성자인지 확인하고, 지원 상태, 기술 스택, 좋아요 정보를 포함한 상세 정보를 반환합니다.
     *
     * @param username 모임 상세 정보를 요청한 사용자의 사용자명
     * @param meetingId 모임의 고유 ID
     * @return 모임의 상세 정보 DTO (MeetingDetailResponseDto)
     */
    @Transactional(readOnly = true)
    public MeetingDetailResponseDto getMeetingDetail(String username, Long meetingId) {
        MeetingEntity meetingEntity = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new MeetingNotFoundException(ErrorCode.MEETING_NOT_FOUND));

        Boolean isApplicant = !meetingEntity.getCreator().getUsername().equals(username);
        Boolean applicationStatus = isApplicant ? null : getApplicationStatus(username, meetingEntity);

        List<String> techStacks = getTechStacksForMeeting(meetingEntity);
        LikeDto likeDto = getLikeDtoForMeeting(meetingEntity, username);

        return MeetingDetailResponseDto.fromEntity(isApplicant, applicationStatus, meetingEntity, techStacks, likeDto);
    }

    /**
     * 모임 수정하기
     * - 개설자만 모임 정보를 수정할 수 있음
     *
     * @param username JWT를 통해 인증된 사용자명
     * @param meetingId 수정할 모임 아이디
     * @param meetingUpdateRequestDto 업데이트할 모임 정보가 담긴 DTO
     * @return 모임에 대한 응답 DTO
     */
    public MeetingResponseDto updateMeeting(String username, Long meetingId, MeetingUpdateRequestDto meetingUpdateRequestDto) {

        MeetingEntity meetingEntity = validateAndGetMeeting(meetingId);

        // 개설자가 아닌 경우 예외 처리
        validateMeetingCreator(meetingEntity, username);

        meetingEntity.updateMeeting(meetingUpdateRequestDto);

        // 기술 스택 업데이트
        updateMeetingTechStacks(meetingEntity, meetingUpdateRequestDto);

        // 저장된 모임에 연결된 기술 스택 이름들을 리스트로 반환
        List<String> techStacks = meetingTechStackRepository.findByMeeting(meetingEntity).stream()
                .map(meetingTechStack -> meetingTechStack.getTechStack().getName())
                .toList();

        return MeetingResponseDto.fromEntity(meetingEntity, techStacks);

    }

    /**
     * 모임 삭제하기
     * - 개설자만 모임 삭제할 수 있음
     *
     * @param username JWT를 통해 인증된 사용자명
     * @param meetingId 삭제할 모임 아이디
     */
    public void deleteMeeting(String username, Long meetingId) {

        MeetingEntity meetingEntity = validateAndGetMeeting(meetingId);

        // 개설자가 아닌 경우 예외 처리
        validateMeetingCreator(meetingEntity, username);

        meetingTechStackRepository.deleteByMeeting(meetingEntity);

        likeRepository.deleteByMeeting(meetingEntity);

        meetingRepository.delete(meetingEntity);
    }

    /**
     * 모임원 확정하기
     *
     * @param username JWT를 통해 인증된 사용자명
     * @param meetingId 삭제할 모임 아이디
     * @param confirmMeetingRequestDto 모임원들의 지원서 아이디 리스트
     * @return 모임원들의 정보가 담긴 DTO
     */
    public List<ConfirmMeetingResponseDto> confirmMeetingMember(String username, Long meetingId, ConfirmMeetingRequestDto confirmMeetingRequestDto) {

        MeetingEntity meetingEntity = validateAndGetMeeting(meetingId);

        // 개설자가 아닌 경우 예외 처리
        validateMeetingCreator(meetingEntity, username);

        // 지원자 정보와 상태 업데이트
        Set<Long> applicationIds = new HashSet<>(confirmMeetingRequestDto.getApplicationIds());
        List<ApplicationEntity> applications = applicationRepository.findByMeeting(meetingEntity);

        List<ConfirmMeetingResponseDto> responseDtos = new ArrayList<>();
        responseDtos.add(convertUserToDto(meetingEntity.getCreator())); // 개설자 처리
        processApplications(applications, applicationIds, responseDtos); // 지원자 처리

        // 미팅 상태를 확정
        meetingEntity.confirm();

        // 승인된 지원자 정보를 반환
        return responseDtos;
    }

    /**
     * 확정된 모임원 불러오기
     *
     * @param username JWT를 통해 인증된 사용자명
     * @param meetingId 삭제할 모임 아이디
     * @return 모임원들의 정보가 담긴 DTO
     */
    public List<ConfirmMeetingResponseDto> getConfirmedMembers(String username, Long meetingId) {
        // 모임이 확정된 상태인지 확인
        MeetingEntity meetingEntity = validateAndGetConfirmedMeeting(meetingId);

        // 사용자가 존재하는지 확인
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

        // 지원서가 존재하는지 확인
        ApplicationEntity applicant = applicationRepository.findByApplicantAndMeeting(user, meetingEntity)
                .orElseThrow(() -> new ApplicationNotFoundException(ErrorCode.APPLICATION_NOT_FOUND));

        // username이 개설자가 아니거나, 거절된 상태이면 예외 처리
        if (meetingEntity.getCreator().getUsername().equals(username) || applicant.getStatus() == ApplicationStatus.REJECTED) {
            throw new StudyNotConfirmedException(ErrorCode.STUDY_NOT_CONFIRMED);
        }

        // 개설자 정보
        UserEntity creator = meetingEntity.getCreator();
        ConfirmMeetingResponseDto creatorInfo = convertUserToDto(creator);

        // meetingId에 해당되고, 상태가 APPROVED인 지원서 조회
        List<ApplicationEntity> approvedApplications = applicationRepository
                .findByMeetingAndStatus(meetingEntity, ApplicationStatus.APPROVED);

        // DTO로 변환 (개설자 정보 포함)
        List<ConfirmMeetingResponseDto> responseDtos = approvedApplications.stream()
                .map(application -> ConfirmMeetingResponseDto.fromEntity(application.getApplicant()))
                .collect(Collectors.toList());

        responseDtos.add(0, creatorInfo);

        return responseDtos;
    }

    /**
     * 유저 엔티티를 통해 모임 확정 DTO로 바꾸는 메서드
     *
     * @param userEntity 유저 엔티티
     * @return 모임원들의 정보가 담긴 DTO
     */
    private ConfirmMeetingResponseDto convertUserToDto(UserEntity userEntity) {
        return ConfirmMeetingResponseDto.fromEntity(userEntity);
    }

    /**
     * 지원자들의 처리
     * - 유효하지 않은 아이디 제거
     * - 모임 확정된 지원자들은 지원으로 status 변경, 아닌 경우는 거절로 status 변경
     *
     * @param applications 지원서 엔티티
     * @param applicationIds 모임원들의 지원서 아이디 리스트
     * @param responseDtos 모임원들의 정보가 담긴 DTO
     */
    private void processApplications(List<ApplicationEntity> applications, Set<Long> applicationIds, List<ConfirmMeetingResponseDto> responseDtos) {
        // 지원서 ID가 유효한지 확인
        Set<Long> validApplicationIds = new HashSet<>(applicationIds); // Set으로 변환하여 중복 제거
        List<ApplicationEntity> validApplications = new ArrayList<>();

        for (ApplicationEntity applicationEntity : applications) {
            if (validApplicationIds.contains(applicationEntity.getId())) {
                validApplications.add(applicationEntity);
                validApplicationIds.remove(applicationEntity.getId()); // 유효한 ID를 처리 후 제거
            }
        }

        // 유효하지 않은 ID가 있는지 체크
        if (!validApplicationIds.isEmpty()) {
            throw new InvalidApplicationIdException(ErrorCode.INVALID_APPLICATION_ID);
        }

        // 유효한 지원서에 대해서 승인 또는 거절 처리
        for (ApplicationEntity applicationEntity : validApplications) {
            applicationEntity.approve(); // 지원 확정
            responseDtos.add(convertUserToDto(applicationEntity.getApplicant())); // 승인된 지원자 DTO 변환 후 추가
        }

        // 유효하지 않은 지원서에 대해서 거절 처리
        for (ApplicationEntity applicationEntity : applications) {
            if (!validApplications.contains(applicationEntity)) {
                applicationEntity.reject(); // 지원 거절
            }
        }
    }

    /**
     * 모임에 연결된 기술 스택을 저장하는 메서드
     *
     * @param meetingEntity 생성된 모임 엔티티
     * @param createMeetingRequestDto 모임 생성 요청 DTO
     */
    private void saveMeetingTechStacks(MeetingEntity meetingEntity, CreateMeetingRequestDto createMeetingRequestDto) {
        // 기존 기술 스택 처리 (기술 스택 ID 목록을 사용하여 처리)
        if (createMeetingRequestDto.getTechStackIds() != null) {
            List<TechStackEntity> techStacks = techStackRepository.findAllById(createMeetingRequestDto.getTechStackIds());
            techStacks.forEach(techStack -> meetingTechStackRepository.save(
                    MeetingTechStackEntity.builder()
                            .meeting(meetingEntity)
                            .techStack(techStack)
                            .build()
            ));
        }

        // 사용자가 입력한 기타 기술 스택 처리
        if (createMeetingRequestDto.getOtherTechStacks() != null) {
            createMeetingRequestDto.getOtherTechStacks().forEach(otherTech -> {
                // 기술 스택이 이미 존재하는지 확인하고, 없으면 새로 생성
                TechStackEntity techStack = techStackRepository.findByName(otherTech)
                        .orElseGet(() -> techStackRepository.save(
                                TechStackEntity.builder()
                                        .name(otherTech)
                                        .category(TechCategory.OTHER)
                                        .build()
                        ));

                // 모임과 기술 스택을 연결하여 저장
                meetingTechStackRepository.save(
                        MeetingTechStackEntity.builder()
                                .meeting(meetingEntity)
                                .techStack(techStack)
                                .build()
                );
            });
        }
    }

    /**
     * 모임 연결된 기술 스택을 업데이트하는 메서드
     *
     * @param meetingEntity 모임 엔티티
     * @param meetingUpdateRequestDto 업데이트할 모임 정보가 담긴 DTO
     */
    private void updateMeetingTechStacks(MeetingEntity meetingEntity, MeetingUpdateRequestDto meetingUpdateRequestDto) {
        if (meetingUpdateRequestDto.getTechStackIds() != null || meetingUpdateRequestDto.getOtherTechStacks() != null) {
            meetingTechStackRepository.deleteByMeeting(meetingEntity);
        }

        if (meetingUpdateRequestDto.getTechStackIds() != null) {
            List<TechStackEntity> techStacks = techStackRepository.findAllById(meetingUpdateRequestDto.getTechStackIds());
            techStacks.forEach(techStack -> meetingTechStackRepository.save(
                    MeetingTechStackEntity.builder()
                            .meeting(meetingEntity)
                            .techStack(techStack)
                            .build()
            ));
        }

        if (meetingUpdateRequestDto.getOtherTechStacks() != null) {
            meetingUpdateRequestDto.getOtherTechStacks().forEach(otherTech -> {
                TechStackEntity techStack = techStackRepository.findByName(otherTech)
                        .orElseGet(() -> techStackRepository.save(
                                TechStackEntity.builder()
                                        .name(otherTech)
                                        .category(TechCategory.OTHER)
                                        .build()
                        ));
                meetingTechStackRepository.save(
                        MeetingTechStackEntity.builder()
                                .meeting(meetingEntity)
                                .techStack(techStack)
                                .build()
                );
            });
        }
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

        Boolean isLiked = null;
        // 인증된 사용자일 경우에만 like 여부를 확인
        if (username != null) {
            isLiked = likeRepository.existsByMeetingAndUserUsername(meetingEntity, username);
        }

        return LikeDto.builder()
                .likeCount(likeCount)
                .isLiked(isLiked)
                .build();
    }

    /**
     * 사용자가 해당 모임에 지원했는지 여부를 반환하는 메서드
     *
     * @param username   사용자명
     * @param meetingEntity 모임 엔티티
     * @return 지원 여부
     */
    private Boolean getApplicationStatus(String username, MeetingEntity meetingEntity) {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

        return applicationRepository.existsByMeetingAndApplicant(meetingEntity, userEntity);
    }
}
