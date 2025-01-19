package com.example.starhub.service;

import com.example.starhub.dto.request.CreatePostRequestDto;
import com.example.starhub.dto.response.PostResponseDto;
import com.example.starhub.entity.PostEntity;
import com.example.starhub.entity.PostTechStackEntity;
import com.example.starhub.entity.TechStackEntity;
import com.example.starhub.entity.UserEntity;
import com.example.starhub.entity.enums.TechCategory;
import com.example.starhub.exception.UserNotFoundException;
import com.example.starhub.repository.PostRepository;
import com.example.starhub.repository.PostTechStackRepository;
import com.example.starhub.repository.TechStackRepository;
import com.example.starhub.repository.UserRepository;
import com.example.starhub.response.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TechStackRepository techStackRepository;
    private final PostTechStackRepository postTechStackRepository;

    /**
     * 새로운 포스트(스터디/프로젝트)를 생성하는 메서드
     *
     * @param username JWT를 통해 인증된 사용자명
     * @param createPostRequestDto 포스트 생성에 필요한 데이터를 담고 있는 요청 DTO
     * @return 생성된 포스트에 대한 응답 DTO
     */
    public PostResponseDto createPost(String username, CreatePostRequestDto createPostRequestDto) {

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

        PostEntity postEntity = buildPostEntity(user, createPostRequestDto);
        PostEntity savedPost = postRepository.save(postEntity);

        // 기술 스택 정보를 처리하여 포스트와 연결
        savePostTechStacks(savedPost, createPostRequestDto);

        // 저장된 포스트에 연결된 기술 스택 이름들을 리스트로 반환
        List<String> techStackNames = postTechStackRepository.findByPost(savedPost).stream()
                .map(postTechStack -> postTechStack.getTechStack().getName())
                .toList();

        return PostResponseDto.fromEntity(savedPost, techStackNames);
    }

    /**
     * 포스트 엔티티를 생성하는 메서드
     *
     * @param user 포스트를 생성한 사용자 (개설자)
     * @param createPostRequestDto 포스트 생성을 위한 요청 DTO
     * @return 생성된 PostEntity
     */
    private PostEntity buildPostEntity(UserEntity user, CreatePostRequestDto createPostRequestDto) {
        return PostEntity.builder()
                .recruitmentType(createPostRequestDto.getRecruitmentType())
                .maxParticipants(createPostRequestDto.getMaxParticipants())
                .duration(createPostRequestDto.getDuration())
                .endDate(createPostRequestDto.getEndDate())
                .location(createPostRequestDto.getLocation())
                .latitude(createPostRequestDto.getLatitude())
                .longitude(createPostRequestDto.getLongitude())
                .title(createPostRequestDto.getTitle())
                .description(createPostRequestDto.getDescription())
                .goal(createPostRequestDto.getGoal())
                .otherInfo(createPostRequestDto.getOtherInfo())
                .isConfirmed(false) // 확인되지 않은 상태로 설정
                .creator(user) // 포스트 개설자 지정
                .build();
    }

    /**
     * 포스트에 연결된 기술 스택을 저장하는 메서드
     *
     * @param postEntity 생성된 포스트 엔티티
     * @param createPostRequestDto 포스트 생성 요청 DTO
     */
    private void savePostTechStacks(PostEntity postEntity, CreatePostRequestDto createPostRequestDto) {
        // 기존 기술 스택 처리 (기술 스택 ID 목록을 사용하여 처리)
        if (createPostRequestDto.getTechStackIds() != null) {
            List<TechStackEntity> techStacks = techStackRepository.findAllById(createPostRequestDto.getTechStackIds());
            techStacks.forEach(techStack -> postTechStackRepository.save(
                    PostTechStackEntity.builder()
                            .post(postEntity)
                            .techStack(techStack)
                            .build()
            ));
        }

        // 사용자가 입력한 기타 기술 스택 처리
        if (createPostRequestDto.getOtherTechStacks() != null) {
            createPostRequestDto.getOtherTechStacks().forEach(otherTech -> {
                // 기술 스택이 이미 존재하는지 확인하고, 없으면 새로 생성
                TechStackEntity techStack = techStackRepository.findByName(otherTech)
                        .orElseGet(() -> techStackRepository.save(
                                TechStackEntity.builder()
                                        .name(otherTech)
                                        .category(TechCategory.OTHER)
                                        .build()
                        ));

                // 포스트와 기술 스택을 연결하여 저장
                postTechStackRepository.save(
                        PostTechStackEntity.builder()
                                .post(postEntity)
                                .techStack(techStack)
                                .build()
                );
            });
        }
    }
}
