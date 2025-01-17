package com.example.starhub.service.filter;

import com.example.starhub.dto.response.util.ResponseUtil;
import com.example.starhub.response.code.ErrorCode;
import com.example.starhub.response.code.ResponseCode;
import com.example.starhub.service.RedisService;
import com.example.starhub.util.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Optional;

/**
 * 로그아웃을 처리하는 필터
 * - 사용자의 로그아웃 요청을 처리하고, 해당 사용자의 refresh 토큰을 검증 및 삭제합니다.
 */
@RequiredArgsConstructor
public class LogoutFilter extends GenericFilterBean {

    private static final String REFRESH_TOKEN_PREFIX = "refresh_token:";

    private final JWTUtil jwtUtil;
    private final RedisService redisService;

    /**
     * doFilter 메서드
     * - 로그아웃 요청을 필터링하고, 로그아웃을 수행합니다.
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        doFilter((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, filterChain);
    }

    /**
     * 실제 로그아웃 처리를 담당하는 메서드
     * - POST 방식으로 "/logout" URI 요청이 들어오면 refresh 토큰을 검증하고 로그아웃을 진행합니다.
     */
    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        String requestUri = request.getRequestURI();
        if (!requestUri.equals("/api/v1/logout")) {
            filterChain.doFilter(request, response);
            return;
        }

        String requestMethod = request.getMethod();
        // 요청 메서드가 POST가 아닌 경우 필터 체인에 요청을 넘깁니다.
        if (!requestMethod.equals("POST")) {
            filterChain.doFilter(request, response);
            return;
        }

        // refresh 토큰을 쿠키에서 추출
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refresh")) {
                    refresh = cookie.getValue();
                }
            }
        }

        // refresh 토큰이 없는 경우 오류 응답
        if (refresh == null) {
            ResponseUtil.writeErrorResponse(response, ErrorCode.BAD_REQUEST);
            return;
        }

        if (validateRefreshToken(response, refresh)) return;

        String refreshTokenKey = REFRESH_TOKEN_PREFIX + jwtUtil.getUsername(refresh);
        if (validateRedisToken(response, refreshTokenKey, refresh)) return;

        // 로그아웃 진행
        // Redis에서 해당 refresh 토큰 제거
        redisService.deleteValues(refreshTokenKey);

        // Refresh 토큰을 제거하기 위해 쿠키에 값을 0으로 설정
        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);  // 쿠키 만료 시간을 0으로 설정하여 삭제 효과
        cookie.setPath("/");  // 쿠키 경로 설정
        response.addCookie(cookie);

        ResponseUtil.writeSuccessResponse(response, ResponseCode.SUCCESS_LOGOUT, null);
    }

    private boolean validateRedisToken(HttpServletResponse response, String refreshTokenKey, String refresh) {
        Optional<String> storedTokenOptional = redisService.getValues(refreshTokenKey);

        if (storedTokenOptional.isEmpty()) {
            ResponseUtil.writeErrorResponse(response, ErrorCode.TOKEN_NOT_FOUND);
            return true;
        }

        if (!MessageDigest.isEqual(refresh.getBytes(), storedTokenOptional.get().getBytes())) {
            ResponseUtil.writeErrorResponse(response, ErrorCode.INVALID_TOKEN);
            return true;
        }
        return false;
    }

    private boolean validateRefreshToken(HttpServletResponse response, String refresh) throws IOException {
        // refresh 토큰 만료 확인
        if (jwtUtil.isExpired(refresh)) {
            ResponseUtil.writeErrorResponse(response, ErrorCode.TOKEN_EXPIRED);
            return true;
        }

        // 토큰이 refresh 토큰인지 확인 (페이로드에 "refresh"로 지정된 값)
        String category = jwtUtil.getCategory(refresh);
        if (!category.equals("refresh")) {
            ResponseUtil.writeErrorResponse(response, ErrorCode.INVALID_TOKEN_CATEGORY);
            return true;
        }
        return false;
    }
}
