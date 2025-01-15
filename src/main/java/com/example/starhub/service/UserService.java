package com.example.starhub.service;

import com.example.starhub.dto.request.CreateProfileRequestDto;
import com.example.starhub.dto.request.CreateUserRequestDto;
import com.example.starhub.dto.request.UsernameCheckRequestDto;
import com.example.starhub.dto.response.ProfileResponseDto;
import com.example.starhub.dto.response.UserResponseDto;
import com.example.starhub.dto.response.UsernameCheckResponseDto;
import com.example.starhub.entity.UserEntity;
import com.example.starhub.exception.UserNotFoundException;
import com.example.starhub.exception.UsernameAlreadyExistsException;
import com.example.starhub.repository.UserRepository;
import com.example.starhub.response.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * 1차 회원가입
     * 아이디와 비밀번호를 DB에 저장합니다.
     * @param createUserRequestDto 1차 회원가입 요청 DTO
     * @return UserResponseDto 1차 회원가입 응답 DTO
     */
    public UserResponseDto registerUser(CreateUserRequestDto createUserRequestDto) {

        // 아아디 관련 작업
        String username = createUserRequestDto.getUsername();
        if(userRepository.existsByUsername(username)) {
            throw new UsernameAlreadyExistsException(ErrorCode.USERNAME_ALREADY_EXISTS);
        }

        // 비밀번호 암호화
        String password = bCryptPasswordEncoder.encode(createUserRequestDto.getPassword());

        // DB 저장
        UserEntity user = UserEntity.builder()
                .username(username)
                .password(password)
                .build();
        userRepository.save(user);

        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .isProfileComplete(user.getIsProfileComplete())
                .build();
    }

    /**
     * 아이디 중복 확인
     * @param usernameCheckRequestDto 아이디 중복 요청 DTO
     * @return UsernameCheckResponseDto 아이디 중복 여부가 담긴 DTO
     */
    public UsernameCheckResponseDto checkUsernameDuplicate(UsernameCheckRequestDto usernameCheckRequestDto) {
        String username = usernameCheckRequestDto.getUsername();
        boolean isAvailable = !userRepository.existsByUsername(username);
        return new UsernameCheckResponseDto(username, isAvailable);
    }

    /**
     * 프로필 생성하기(2차 회원가입)
     * @param userId 유저 아이디
     * @param createProfileRequestDto 프로필 생성 요청 DTO
     * @return ProfileResponseDto 프로필 생성 응답 DTO
     */
    public ProfileResponseDto createUserProfile(Long userId, CreateProfileRequestDto createProfileRequestDto) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

        user.updateProfile(
                createProfileRequestDto.getProfileImage(),
                createProfileRequestDto.getNickname(),
                createProfileRequestDto.getName(),
                createProfileRequestDto.getAge(),
                createProfileRequestDto.getBio(),
                createProfileRequestDto.getEmail(),
                createProfileRequestDto.getPhoneNumber()
        );

        return ProfileResponseDto.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .build();
    }
}
