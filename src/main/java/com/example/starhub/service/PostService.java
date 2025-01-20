package com.example.starhub.service;

import com.example.starhub.dto.request.CreatePostRequestDto;
import com.example.starhub.dto.request.PostUpdateRequestDto;
import com.example.starhub.dto.response.LikeDto;
import com.example.starhub.dto.response.PostDetailResponseDto;
import com.example.starhub.dto.response.PostResponseDto;
import com.example.starhub.dto.response.PostSummaryResponseDto;
import com.example.starhub.entity.PostEntity;
import com.example.starhub.entity.PostTechStackEntity;
import com.example.starhub.entity.TechStackEntity;
import com.example.starhub.entity.UserEntity;
import com.example.starhub.entity.enums.TechCategory;
import com.example.starhub.exception.PostCreatorAuthorizationException;
import com.example.starhub.exception.PostNotFoundException;
import com.example.starhub.exception.UserNotFoundException;
import com.example.starhub.repository.*;
import com.example.starhub.response.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TechStackRepository techStackRepository;
    private final PostTechStackRepository postTechStackRepository;
    private final LikeRepository likeRepository;
    private final ApplicantsRepository applicantsRepository;

    /**
     * 새로운 포스트(스터디/프로젝트)를 생성하는 메서드
     *
     * @param username JWT를 통해 인증된 사용자명
     * @param createPostRequestDto 포스트 생성에 필요한 데이터를 담고 있는 요청 DTO
     * @return 포스트에 대한 응답 DTO
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
     * 포스트 목록 불러오기 (메인 화면에 쓰일 API)
     * - 포스트 요약 정보가 담긴 목록으로 제공
     * - 페이지네이션을 적용하고, 생성일 기준 내림차순으로 정렬
     *
     * @param username JWT를 통해 인증된 사용자명
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return 포스트 목록 응답 DTO
     */
    @Transactional(readOnly = true)
    public Page<PostSummaryResponseDto> getPostList(String username, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Order.desc("createdAt")));
        Page<PostEntity> postPage = postRepository.findAll(pageRequest);

        return postPage.map(postEntity -> {
            List<String> techStacks = getTechStacksForPost(postEntity);
            LikeDto likeDto = getLikeDtoForPost(postEntity, username);

            return PostSummaryResponseDto.fromEntity(postEntity, techStacks, likeDto);
        });
    }

    /**
     * 특정 포스트의 상세 정보를 가져옵니다.
     * - 포스트의 생성자인지 확인하고, 지원 상태, 기술 스택, 좋아요 정보를 포함한 상세 정보를 반환합니다.
     *
     * @param username 포스트 상세 정보를 요청한 사용자의 사용자명
     * @param id 포스트의 고유 ID
     * @return 포스트의 상세 정보 DTO (PostDetailResponseDto)
     */
    @Transactional(readOnly = true)
    public PostDetailResponseDto getPostDetail(String username, Long id) {
        PostEntity postEntity = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(ErrorCode.POST_NOT_FOUND));

        Boolean isCreator = postEntity.getCreator().getUsername().equals(username);
        Boolean applicationStatus = isCreator ? null : getApplicationStatus(username, postEntity);

        List<String> techStacks = getTechStacksForPost(postEntity);
        LikeDto likeDto = getLikeDtoForPost(postEntity, username);

        return PostDetailResponseDto.fromEntity(isCreator, applicationStatus, postEntity, techStacks, likeDto);
    }

    /**
     * 포스트 수정하기
     * - 개설자만 포스트 정보를 수정할 수 있음
     *
     * @param username JWT를 통해 인증된 사용자명
     * @param id 수정할 포스트 아이디
     * @param postUpdateRequestDto 업데이트할 포스트 정보가 담긴 DTO
     * @return 포스트에 대한 응답 DTO
     */
    public PostResponseDto updatePost(String username, Long id, PostUpdateRequestDto postUpdateRequestDto) {

        PostEntity postEntity = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(ErrorCode.POST_NOT_FOUND));

        // 개설자가 아닌 경우 예외 처리
        if(!postEntity.getCreator().getUsername().equals(username)) {
            throw new PostCreatorAuthorizationException(ErrorCode.POST_MODIFY_FORBIDDEN);
        }

        postEntity.updatePost(postUpdateRequestDto);

        // 기술 스택 업데이트
        updatePostTechStacks(postEntity, postUpdateRequestDto);

        // 저장된 포스트에 연결된 기술 스택 이름들을 리스트로 반환
        List<String> techStackNames = postTechStackRepository.findByPost(postEntity).stream()
                .map(postTechStack -> postTechStack.getTechStack().getName())
                .toList();

        return PostResponseDto.fromEntity(postEntity, techStackNames);

    }

    /**
     * 포스트 삭제하기
     * - 개설자만 포스트 삭제할 수 있음
     *
     * @param username JWT를 통해 인증된 사용자명
     * @param postId 삭제할 포스트 아이디
     */
    public void deletePost(String username, Long postId) {
        PostEntity postEntity = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(ErrorCode.POST_NOT_FOUND));

        // 개설자가 아닌 경우 예외 처리
        if(!postEntity.getCreator().getUsername().equals(username)) {
            throw new PostCreatorAuthorizationException(ErrorCode.POST_MODIFY_FORBIDDEN);
        }

        postTechStackRepository.deleteByPost(postEntity);

        likeRepository.deleteByPost(postEntity);

        postRepository.delete(postEntity);
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

    /**
     * 포스트에 연결된 기술 스택을 업데이트하는 메서드
     *
     * @param postEntity 포스트 엔티티
     * @param postUpdateRequestDto 업데이트할 포스트 정보가 담긴 DTO
     */
    private void updatePostTechStacks(PostEntity postEntity, PostUpdateRequestDto postUpdateRequestDto) {
        if (postUpdateRequestDto.getTechStackIds() != null || postUpdateRequestDto.getOtherTechStacks() != null) {
            postTechStackRepository.deleteByPost(postEntity);
        }

        if (postUpdateRequestDto.getTechStackIds() != null) {
            List<TechStackEntity> techStacks = techStackRepository.findAllById(postUpdateRequestDto.getTechStackIds());
            techStacks.forEach(techStack -> postTechStackRepository.save(
                    PostTechStackEntity.builder()
                            .post(postEntity)
                            .techStack(techStack)
                            .build()
            ));
        }

        if (postUpdateRequestDto.getOtherTechStacks() != null) {
            postUpdateRequestDto.getOtherTechStacks().forEach(otherTech -> {
                TechStackEntity techStack = techStackRepository.findByName(otherTech)
                        .orElseGet(() -> techStackRepository.save(
                                TechStackEntity.builder()
                                        .name(otherTech)
                                        .category(TechCategory.OTHER)
                                        .build()
                        ));
                postTechStackRepository.save(
                        PostTechStackEntity.builder()
                                .post(postEntity)
                                .techStack(techStack)
                                .build()
                );
            });
        }
    }

    /**
     * 포스트에 연결된 기술 스택을 반환하는 메서드
     *
     * @param postEntity 포스트 엔티티
     * @return 기술 스택 이름 리스트
     */
    private List<String> getTechStacksForPost(PostEntity postEntity) {
        return postTechStackRepository.findByPost(postEntity).stream()
                .map(postTechStack -> postTechStack.getTechStack().getName())
                .collect(Collectors.toList());
    }

    /**
     * 포스트에 대한 좋아요 정보 및 내가 좋아요를 눌렀는지 여부를 반환하는 메서드
     *
     * @param postEntity 포스트 엔티티
     * @param username   사용자명
     * @return 좋아요 DTO
     */
    private LikeDto getLikeDtoForPost(PostEntity postEntity, String username) {
        Long likeCount = likeRepository.countByPost(postEntity);
        Boolean isLiked = likeRepository.existsByPostAndUserUsername(postEntity, username);

        return LikeDto.builder()
                .likeCount(likeCount)
                .isLiked(isLiked)
                .build();
    }

    /**
     * 사용자가 해당 포스트에 지원했는지 여부를 반환하는 메서드
     *
     * @param username   사용자명
     * @param postEntity 포스트 엔티티
     * @return 지원 여부
     */
    private Boolean getApplicationStatus(String username, PostEntity postEntity) {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

        return applicantsRepository.existsByPostAndAuthor(postEntity, userEntity);
    }
}
