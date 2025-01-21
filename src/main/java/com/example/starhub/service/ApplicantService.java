package com.example.starhub.service;

import com.example.starhub.dto.request.CreateApplicantRequestDto;
import com.example.starhub.dto.response.ApplicantResponseDto;
import com.example.starhub.entity.ApplicantEntity;
import com.example.starhub.entity.PostEntity;
import com.example.starhub.entity.UserEntity;
import com.example.starhub.exception.PostCreatorCannotApplyException;
import com.example.starhub.exception.PostNotFoundException;
import com.example.starhub.exception.UserNotFoundException;
import com.example.starhub.repository.ApplicantRepository;
import com.example.starhub.repository.PostRepository;
import com.example.starhub.repository.UserRepository;
import com.example.starhub.response.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ApplicantService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ApplicantRepository applicantRepository;

    public ApplicantResponseDto createApplicant(String username, CreateApplicantRequestDto createApplicantRequestDto) {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

        PostEntity postEntity = postRepository.findById(createApplicantRequestDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException(ErrorCode.POST_NOT_FOUND));

        // 개설자일 경우 409 예외 처리
        if(postEntity.getCreator().getUsername().equals(username)) {
            throw new PostCreatorCannotApplyException(ErrorCode.POST_CREATOR_CANNOT_APPLY);
        }

        ApplicantEntity applicantEntity = ApplicantEntity.builder()
                .applicant(userEntity)
                .content(createApplicantRequestDto.getContent())
                .post(postEntity)
                .build();

        ApplicantEntity savedApplicantEntity = applicantRepository.save(applicantEntity);

        return ApplicantResponseDto.fromEntity(savedApplicantEntity);
    }
}
