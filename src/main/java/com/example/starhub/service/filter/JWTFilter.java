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

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // request Authorization 헤더를 찾음
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

        // 토큰의 카테고리 확인 및 검증
        String category = jwtUtil.getCategory(accessToken);
        if (!category.equals("access")) {

            ResponseUtil.writeErrorResponse(response, ErrorCode.INVALID_TOKEN_CATEGORY);
            return;
        }

        String username = jwtUtil.getUsername(accessToken);
        String role = jwtUtil.getRole(accessToken);
        UserEntity user = UserEntity.createUserWithRole(
                username,
                role
        );
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }

    private String extractAccessToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer ")) {
            return authorization.split(" ")[1];
        }
        return null;
    }

}
