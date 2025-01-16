package com.example.starhub.dto.security;

import com.example.starhub.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/**
 * CustomUserDetails는 Spring Security에서 사용자 인증 정보를 제공하기 위한 클래스입니다.
 * UserDetails 인터페이스를 구현하여 사용자의 정보를 Spring Security가 이해할 수 있도록 변환합니다.
 */
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final UserEntity userEntity;

    /**
     * 사용자에게 부여된 권한(역할)을 반환합니다.
     *
     * @return 사용자의 권한 목록
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return userEntity.getRole();
            }
        });

        return collection;
    }

    /**
     * 사용자 프로필 완성 여부를 반환합니다.
     *
     * @return 프로필이 완성되었으면 true, 아니면 false
     */
    public Boolean getIsProfileComplete() {
        return userEntity.getIsProfileComplete();
    }

    /**
     * 사용자의 비밀번호를 반환합니다.
     *
     * @return 사용자의 비밀번호
     */
    @Override
    public String getPassword() {
        return userEntity.getPassword();
    }

    /**
     * 사용자의 사용자명을 반환합니다.
     *
     * @return 사용자의 사용자명
     */
    @Override
    public String getUsername() {
        return userEntity.getUsername();
    }

    /**
     * 계정이 만료되지 않았는지 확인합니다.
     * 항상 true를 반환하여 계정 만료 여부를 비활성화합니다.
     *
     * @return 계정 만료 여부 (항상 true)
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 계정이 잠겨 있지 않은지 확인합니다.
     * 항상 true를 반환하여 계정 잠금 여부를 비활성화합니다.
     *
     * @return 계정 잠금 여부 (항상 true)
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 사용자의 자격 증명이 만료되지 않았는지 확인합니다.
     * 항상 true를 반환하여 자격 증명 만료 여부를 비활성화합니다.
     *
     * @return 자격 증명 만료 여부 (항상 true)
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 사용자가 활성화되어 있는지 확인합니다.
     * 항상 true를 반환하여 사용자가 활성화된 상태임을 보장합니다.
     *
     * @return 사용자 활성화 여부 (항상 true)
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
