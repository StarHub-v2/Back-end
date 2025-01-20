package com.example.starhub.service.filter;

import com.example.starhub.dto.response.util.ResponseUtil;
import com.example.starhub.dto.security.CustomUserDetails;
import com.example.starhub.entity.UserEntity;
import com.example.starhub.response.code.ErrorCode;
import com.example.starhub.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
 * JWT 검증 필터
 *
 * - HTTP 요청의 Authorization 헤더에서 JWT를 추출 및 검증합니다.
 * - 유효한 JWT가 확인되면 SecurityContext에 인증 정보를 설정합니다.
 * - 유효하지 않은 JWT 또는 만료된 경우 적절한 에러 응답을 반환하고 요청 처리를 중단합니다.
 */
@Slf4j
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = extractAccessToken(request);

        // Access Token이 없으면 다음 필터로 이동
        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰 유효성 검증
        if (validateAccessToken(response, accessToken)) return;

        // 사용자 정보 설정
        setAuthentication(accessToken);

        // 요청 처리 계속 진행
        filterChain.doFilter(request, response);
    }

    /**
     * Access Token 유효성 검증
     *
     * - 토큰 만료 여부 확인
     * - 토큰 카테고리가 "access"인지 확인
     *
     * @param response 응답 객체
     * @param accessToken Access Token
     * @return 유효성 검증 결과 (true: 실패, false: 성공)
     */
    private boolean validateAccessToken(HttpServletResponse response, String accessToken) {
        String username = jwtUtil.getUsername(accessToken);

        // 만료된 토큰인지 확인
        if (jwtUtil.isExpired(accessToken)) {
            log.error("Access Token expired for user: {}", username);
            ResponseUtil.writeErrorResponse(response, ErrorCode.UNAUTHORIZED);
            return true;
        }

        // 토큰 카테고리가 "access"인지 확인
        if (!"access".equals(jwtUtil.getCategory(accessToken))) {
            log.error("Invalid token category for user: {}", username);
            ResponseUtil.writeErrorResponse(response, ErrorCode.UNAUTHORIZED);
            return true;
        }

        return false; // 유효한 토큰
    }

    /**
     * SecurityContext에 인증 정보 설정
     *
     * - 토큰에서 사용자 정보를 추출하고, SecurityContextHolder에 인증 객체를 설정합니다.
     *
     * @param accessToken Access Token
     */
    private void setAuthentication(String accessToken) {
        String username = jwtUtil.getUsername(accessToken);
        String role = jwtUtil.getRole(accessToken);

        UserEntity user = UserEntity.createUserWithRole(username, role);
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    /**
     * Authorization 헤더에서 Access Token 추출
     *
     * - HTTP 요청의 Authorization 헤더에서 "Bearer "로 시작하는 Access Token을 추출합니다.
     * - 유효한 Bearer 토큰 형식이 아니면 null 반환.
     *
     * @param request HTTP 요청 객체
     * @return Access Token 문자열 또는 null
     */
    private String extractAccessToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith(BEARER_PREFIX)) {
            return authorization.substring(BEARER_PREFIX.length());
        }
        return null;
    }
}
