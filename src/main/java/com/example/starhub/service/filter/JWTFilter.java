package com.example.starhub.service.filter;

import com.example.starhub.dto.response.util.ResponseUtil;
import com.example.starhub.dto.security.CustomUserDetails;
import com.example.starhub.entity.UserEntity;
import com.example.starhub.response.code.ErrorCode;
import com.example.starhub.util.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT 검증하는 필터
 *
 * 이 필터는 요청 당 한 번만 실행, HTTP 요청의 Authorization 헤더에서 JWT를 추출 및 검증합니다.
 * 유효한 JWT가 확인된 경우, SecurityContext에 인증 정보를 설정합니다.
 * 유효하지 않은 JWT 또는 만료된 경우, 적절한 에러 응답을 반환하고 필터 체인을 종료합니다.
 */
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // HTTP 요청의 Authorization 헤더에서 Access Token 추출
        String accessToken = extractAccessToken(request);
        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰 만료 확인 및 검증
        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {
            ResponseUtil.writeErrorResponse(response, ErrorCode.TOKEN_EXPIRED);
            return;
        }

        // 토큰 카테고리 검증 (access 토큰이어야 함)
        String category = jwtUtil.getCategory(accessToken);
        if (!category.equals("access")) {
            ResponseUtil.writeErrorResponse(response, ErrorCode.INVALID_TOKEN_CATEGORY);
            return;
        }

        // 토큰에서 사용자 정보 추출 및 인증 객체 생성
        String username = jwtUtil.getUsername(accessToken);
        String role = jwtUtil.getRole(accessToken);
        UserEntity user = UserEntity.createUserWithRole(
                username,
                role
        );
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        // Spring Security의 인증 컨텍스트 설정
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // 요청 처리 계속 진행
        filterChain.doFilter(request, response);
    }

    /**
     * Authorization 헤더에서 Access Token 추출
     *
     * HTTP 요청의 Authorization 헤더에서 "Bearer "로 시작하는 Access Token을 추출합니다.
     * 유효한 Bearer 토큰 형식이 아니면 null을 반환합니다.
     *
     * @param request HTTP 요청 객체
     * @return Access Token 문자열 또는 null
     */
    private String extractAccessToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer ")) {
            return authorization.split(" ")[1];
        }
        return null;
    }

}
