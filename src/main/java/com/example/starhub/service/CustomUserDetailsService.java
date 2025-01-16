package com.example.starhub.service;

import com.example.starhub.dto.security.CustomUserDetails;
import com.example.starhub.entity.UserEntity;
import com.example.starhub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Spring Security에서 제공하는 UserDetailsService를 구현한 커스텀 서비스.
 * 사용자의 인증 정보를 데이터베이스에서 조회하여 반환합니다.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * 사용자의 이름(username)을 기반으로 사용자 정보를 로드하는 메서드.
     *
     * @param username 인증 요청 시 입력된 사용자명
     * @return UserDetails 사용자 정보를 담고 있는 객체 (CustomUserDetails)
     * @throws UsernameNotFoundException 사용자명이 데이터베이스에 없을 경우 예외 발생
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<UserEntity> userData = userRepository.findByUsername(username);

        if (userData.isPresent()) {
            return new CustomUserDetails(userData.get());
        }

        throw new UsernameNotFoundException("사용자명을 찾을 수 없습니다");
    }
}
