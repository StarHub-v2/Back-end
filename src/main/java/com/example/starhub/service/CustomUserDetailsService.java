package com.example.starhub.service;

import com.example.starhub.dto.security.CustomUserDetails;
import com.example.starhub.entity.UserEntity;
import com.example.starhub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<UserEntity> userData = userRepository.findByUsername(username);

        if (userData.isPresent()) {
            return new CustomUserDetails(userData.get());
        }

        return null;
    }
}
