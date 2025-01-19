package com.example.starhub.service;

import com.example.starhub.dto.request.CreatePostRequestDto;
import com.example.starhub.entity.PostEntity;
import com.example.starhub.entity.UserEntity;
import com.example.starhub.exception.UserNotFoundException;
import com.example.starhub.repository.PostRepository;
import com.example.starhub.repository.UserRepository;
import com.example.starhub.response.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public void createPost(String username, CreatePostRequestDto createPostRequestDto) {

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

        // PostEntity 객체 생성
        PostEntity postEntity = PostEntity.builder()
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

        // 포스트 저장
        postRepository.save(postEntity);

    }
}
