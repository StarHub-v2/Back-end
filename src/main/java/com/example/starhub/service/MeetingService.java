package com.example.starhub.service;

import com.example.starhub.dto.request.CreateMeetingRequestDto;
import com.example.starhub.dto.request.MeetingUpdateRequestDto;
import com.example.starhub.dto.response.LikeDto;
import com.example.starhub.dto.response.MeetingDetailResponseDto;
import com.example.starhub.dto.response.MeetingResponseDto;
import com.example.starhub.dto.response.MeetingSummaryResponseDto;
import com.example.starhub.entity.MeetingEntity;
import com.example.starhub.entity.MeetingTechStackEntity;
import com.example.starhub.entity.TechStackEntity;
import com.example.starhub.entity.UserEntity;
import com.example.starhub.entity.enums.TechCategory;
import com.example.starhub.exception.CreatorAuthorizationException;
import com.example.starhub.exception.MeetingNotFoundException;
import com.example.starhub.exception.UserNotFoundException;
import com.example.starhub.repository.*;
import com.example.starhub.response.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
     * 새로운 모임(스터디/프로젝트)를 생성하기
     *
     * @param username JWT를 통해 인증된 사용자명
     * @param createMeetingRequestDto 모임 생성에 필요한 데이터를 담고 있는 요청 DTO
     * @return 모임에 대한 응답 DTO
     */
    public MeetingResponseDto createMeeting(String username, CreateMeetingRequestDto createMeetingRequestDto) {

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

        MeetingEntity meetingEntity = buildMeetingEntity(user, createMeetingRequestDto);
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

        MeetingEntity meetingEntity = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new MeetingNotFoundException(ErrorCode.MEETING_NOT_FOUND));

        // 개설자가 아닌 경우 예외 처리
        if(!meetingEntity.getCreator().getUsername().equals(username)) {
            throw new CreatorAuthorizationException(ErrorCode.MEETING_FORBIDDEN);
        }

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
        MeetingEntity meetingEntity = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new MeetingNotFoundException(ErrorCode.MEETING_NOT_FOUND));

        // 개설자가 아닌 경우 예외 처리
        if(!meetingEntity.getCreator().getUsername().equals(username)) {
            throw new CreatorAuthorizationException(ErrorCode.MEETING_FORBIDDEN);
        }

        meetingTechStackRepository.deleteByMeeting(meetingEntity);

        likeRepository.deleteByMeeting(meetingEntity);

        meetingRepository.delete(meetingEntity);
    }

    /**
     * 모임 엔티티를 생성하는 메서드
     *
     * @param user 모임을 생성한 사용자 (개설자)
     * @param createMeetingRequestDto 모임 생성을 위한 요청 DTO
     * @return 생성된 MeetingEntity
     */
    private MeetingEntity buildMeetingEntity(UserEntity user, CreateMeetingRequestDto createMeetingRequestDto) {
        return MeetingEntity.builder()
                .recruitmentType(createMeetingRequestDto.getRecruitmentType())
                .maxParticipants(createMeetingRequestDto.getMaxParticipants())
                .duration(createMeetingRequestDto.getDuration())
                .endDate(createMeetingRequestDto.getEndDate())
                .location(createMeetingRequestDto.getLocation())
                .latitude(createMeetingRequestDto.getLatitude())
                .longitude(createMeetingRequestDto.getLongitude())
                .title(createMeetingRequestDto.getTitle())
                .description(createMeetingRequestDto.getDescription())
                .goal(createMeetingRequestDto.getGoal())
                .otherInfo(createMeetingRequestDto.getOtherInfo())
                .isConfirmed(false) // 확인되지 않은 상태로 설정
                .creator(user) // 모임 개설자 지정
                .build();
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
