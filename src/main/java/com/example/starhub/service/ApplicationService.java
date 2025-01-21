package com.example.starhub.service;

import com.example.starhub.dto.request.CreateApplicationRequestDto;
import com.example.starhub.dto.response.ApplicationResponseDto;
import com.example.starhub.entity.ApplicationEntity;
import com.example.starhub.entity.PostEntity;
import com.example.starhub.entity.UserEntity;
import com.example.starhub.exception.PostCreatorAuthorizationException;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ApplicationService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ApplicationRepository applicationRepository;

    /**
     * 지원서 작성하기
     * - 개설자는 지원서를 작성하지 못합니다
     *
     * @param username JWT를 통해 인증된 사용자명
     * @param postId 포스트 아이디
     * @param createApplicationRequestDto 지원서 관련 정보가 담긴 DTO
     * @return 지원서 응답에 대한 DTO
     */
    public ApplicationResponseDto createApplication(String username, Long postId, CreateApplicationRequestDto createApplicationRequestDto) {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

        PostEntity postEntity = postRepository.findById(postId)
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

    /**
     * 지원서 목록 불러오기
     * - 개설자만 지원서 목록을 불러올 수 있음
     *
     * @param username JWT를 통해 인증된 사용자명
     * @param postId 포스트 아이디
     * @return 지원서 응답에 대한 목록 DTO
     */
    @Transactional(readOnly = true)
    public List<ApplicationResponseDto> getApplicationList(String username, Long postId) {

        PostEntity postEntity = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(ErrorCode.POST_NOT_FOUND));

        // 개설자가 아닌 경우 예외 처리
        if(!postEntity.getCreator().getUsername().equals(username)) {
            throw new PostCreatorAuthorizationException(ErrorCode.POST_MODIFY_FORBIDDEN);
        }

        List<ApplicationEntity> applicantEntities = applicationRepository.findByPost(postEntity);

        return applicantEntities.stream()
                .map(applicant -> ApplicationResponseDto.fromEntity(applicant))
                .collect(Collectors.toList());
    }

}
