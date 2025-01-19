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

    public PostResponseDto createPost(String username, CreatePostRequestDto createPostRequestDto) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

        PostEntity postEntity = buildPostEntity(user, createPostRequestDto);
        PostEntity savedPost = postRepository.save(postEntity);

        savePostTechStacks(savedPost, createPostRequestDto);

        List<String> techStackNames = postTechStackRepository.findByPost(savedPost).stream()
                .map(postTechStack -> postTechStack.getTechStack().getName())
                .toList();

        return PostResponseDto.fromEntity(savedPost, techStackNames);
    }

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
                .isConfirmed(false)
                .creator(user)
                .build();
    }

    private void savePostTechStacks(PostEntity postEntity, CreatePostRequestDto createPostRequestDto) {
        // 기존 기술 스택 처리
        if (createPostRequestDto.getTechStackIds() != null) {
            List<TechStackEntity> techStacks = techStackRepository.findAllById(createPostRequestDto.getTechStackIds());
            techStacks.forEach(techStack -> postTechStackRepository.save(
                    PostTechStackEntity.builder()
                            .post(postEntity)
                            .techStack(techStack)
                            .build()
            ));
        }

        // 기타 기술 스택 처리
        if (createPostRequestDto.getOtherTechStacks() != null) {
            createPostRequestDto.getOtherTechStacks().forEach(otherTech -> {
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

}
