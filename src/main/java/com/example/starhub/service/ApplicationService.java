package com.example.starhub.service;

import com.example.starhub.dto.request.CreateApplicationRequestDto;
import com.example.starhub.dto.response.ApplicationResponseDto;
import com.example.starhub.entity.ApplicationEntity;
import com.example.starhub.entity.PostEntity;
import com.example.starhub.entity.UserEntity;
import com.example.starhub.exception.PostCreatorCannotApplyException;
import com.example.starhub.exception.PostNotFoundException;
import com.example.starhub.exception.UserNotFoundException;
import com.example.starhub.repository.ApplicationRepository;
import com.example.starhub.repository.PostRepository;
import com.example.starhub.repository.UserRepository;
import com.example.starhub.response.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ApplicationService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ApplicationRepository applicationRepository;

    public ApplicationResponseDto createApplication(String username, CreateApplicationRequestDto createApplicationRequestDto) {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

        PostEntity postEntity = postRepository.findById(createApplicationRequestDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException(ErrorCode.POST_NOT_FOUND));

        // 개설자일 경우 409 예외 처리
        if(postEntity.getCreator().getUsername().equals(username)) {
            throw new PostCreatorCannotApplyException(ErrorCode.POST_CREATOR_CANNOT_APPLY);
        }

        ApplicationEntity applicationEntity = ApplicationEntity.builder()
                .applicant(userEntity)
                .content(createApplicationRequestDto.getContent())
                .post(postEntity)
                .build();

        ApplicationEntity savedApplicationEntity = applicationRepository.save(applicationEntity);

        return ApplicationResponseDto.fromEntity(savedApplicationEntity);
    }
}
