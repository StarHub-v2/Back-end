package com.example.starhub.service.filter;

import com.example.starhub.service.RedisService;
import com.example.starhub.dto.request.CreateUserRequestDto;
import com.example.starhub.dto.response.UserResponseDto;
import com.example.starhub.dto.response.util.ResponseUtil;
import com.example.starhub.dto.security.CustomUserDetails;
import com.example.starhub.response.code.ErrorCode;
import com.example.starhub.response.code.ResponseCode;
import com.example.starhub.response.dto.ResponseDto;
import com.example.starhub.util.JWTUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collection;
import java.util.Iterator;

/**
 * 로그인 요청을 처리하는 필터
 * - 사용자 인증을 처리하고, 성공 및 실패 시 응답을 정의합니다.
 */
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private static final String REFRESH_TOKEN_PREFIX = "refresh_token:";
    private static final long ACCESS_TOKEN_EXPIRATION = 600000L; // Access 토큰 만료 시간 (10분)
    private static final long REFRESH_TOKEN_EXPIRATION = 86400000L; // Refresh 토큰 만료 시간 (24시간)

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final RedisService redisService;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, RedisService redisService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.redisService = redisService;

        // 로그인 요청 URI
        setFilterProcessesUrl("/api/v1/login");
    }

    /**
     * 인증 요철 처리
     * - 클라이언트으로부터 사용자명과 비밀번호를 받아 인증을 시도합니다.
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // 요청에서 사용자 인증 정보를 추출
        CreateUserRequestDto createUserRequestDto = parseRequest(request);

        String username = createUserRequestDto.getUsername();
        String password = createUserRequestDto.getPassword();

        // 인증 토큰 생성
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

        // 인증 매니저를 통해 인증 수행
        return authenticationManager.authenticate(authToken);
    }

    /**
     * 로그인 성공 시 실행하는 메서드
     * - Access 및 Refresh 토큰을 생성하고 응답으에 포함합니다.
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        CustomUserDetails customUserDetails = (CustomUserDetails) authResult.getPrincipal();

        String username = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        // JWT 토큰 생성
        String access = jwtUtil.createJwt("access", username, role, ACCESS_TOKEN_EXPIRATION);
        String refresh = jwtUtil.createJwt("refresh", username, role, REFRESH_TOKEN_EXPIRATION);

        // Redis에 Refresh 키 저장
        String refreshTokenKey = REFRESH_TOKEN_PREFIX + username;
        redisService.setValues(refreshTokenKey, refresh, Duration.ofMillis(REFRESH_TOKEN_EXPIRATION));

        // 사용자 응답 DTO 생성
        UserResponseDto userResponseDto = UserResponseDto.builder()
                .username(username)
                .isProfileComplete(customUserDetails.getIsProfileComplete())
                .build();

        // 응답 헤더와 쿠키에 토큰 포함
        response.addHeader("Authorization", "Bearer " + access);
        response.addCookie(createCookie("refresh", refresh));
        ResponseUtil.writeSuccessResponse(response, ResponseCode.SUCCESS_LOGIN, userResponseDto);
    }

    /**
     * 로그인 실패 시 실행되는 메서드
     * - 발생한 예외에 따라 적절한 에러 응답을 반환합니다.
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {

        ErrorCode errorCode;

        // 잘못된 자격 증명 (사용자명, 비밀번호 오류)
        if (failed instanceof BadCredentialsException) {
            errorCode = ErrorCode.BAD_CREDENTIALS;
        }
        // 그 외의 인증 실패
        else {
            errorCode = ErrorCode.UNAUTHORIZED;
        }

        ResponseUtil.writeErrorResponse(response, errorCode);
    }

    /**
     * JSON 요청에서 사용자명과 비밀번호를 추출하는 메서드
     * - 요청 데이터를 DTO로 변환합니다.
     */
    private CreateUserRequestDto parseRequest(HttpServletRequest request) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ServletInputStream inputStream = request.getInputStream();
            String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

            return objectMapper.readValue(messageBody, CreateUserRequestDto.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("요청 데이터를 처리하는 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * HTTP 쿠키 생성 메서드
     * - 쿠키에 값을 설정하고 기본 속성을 적용합니다.
     */
    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        //cookie.setSecure(true);
        //cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}
