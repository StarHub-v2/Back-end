package com.example.starhub.service.filter;

import com.example.starhub.common.redis.RedisService;
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

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private static final String REFRESH_TOKEN_PREFIX = "refresh_token:";
    private static final long ACCESS_TOKEN_EXPIRATION = 600000L;
    private static final long REFRESH_TOKEN_EXPIRATION = 86400000L;

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final RedisService redisService;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, RedisService redisService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.redisService = redisService;

        setFilterProcessesUrl("/api/v1/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        CreateUserRequestDto createUserRequestDto = new CreateUserRequestDto();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ServletInputStream inputStream = request.getInputStream();
            String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

            createUserRequestDto = objectMapper.readValue(messageBody, CreateUserRequestDto.class);
        } catch (IOException e) {

        }

        String username = createUserRequestDto.getUsername();
        String password = createUserRequestDto.getPassword();

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

        return authenticationManager.authenticate(authToken);
    }

    /**
     * 로그인 성공 시 실행하는 메서드
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        CustomUserDetails customUserDetails = (CustomUserDetails) authResult.getPrincipal();

        String username = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();

        // 토큰 생성
        String access = jwtUtil.createJwt("access", username, role, ACCESS_TOKEN_EXPIRATION);
        String refresh = jwtUtil.createJwt("refresh", username, role, REFRESH_TOKEN_EXPIRATION);

        // refresh 키 저장
        String refreshTokenKey = REFRESH_TOKEN_PREFIX + username;
        redisService.setValues(refreshTokenKey, refresh, Duration.ofMillis(REFRESH_TOKEN_EXPIRATION));

        UserResponseDto userResponseDto = UserResponseDto.builder()
                .username(username)
                .isProfileComplete(customUserDetails.getIsProfileComplete())
                .build();
        ResponseDto<UserResponseDto> responseDto = new ResponseDto<>(ResponseCode.SUCCESS_LOGIN, userResponseDto);

        response.addHeader("Authorization", "Bearer " + access);
        response.addCookie(createCookie("refresh", refresh));

        response.setStatus(ResponseCode.SUCCESS_LOGIN.getStatus().value());
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(responseDto));
    }

    /**
     * 로그인 실패 시 실행하는 메서드
     * 시큐리티에서 제공하는 여러 예외 중 서비스에서 사용이 될 것 같은 에외 가지고 예외처리
     * BadCredentialsException: 잘못된 자격 증명(사용자명, 비밀번호)
     * UsernameNotFoundException: 사용자가 존재하지 않는 경우
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {

        ErrorCode errorCode;

        // 잘못된 자격 증명 (사용자명, 비밀번호)
        if (failed instanceof BadCredentialsException) {
            errorCode = ErrorCode.BAD_CREDENTIALS;
        }
        // 사용자가 존재하지 않는 경우
        else if (failed instanceof UsernameNotFoundException) {
            errorCode = ErrorCode.USER_NOT_FOUND;
        }
        // 그 외의 예외는 일반적인 Unauthorized로 처리
        else {
            errorCode = ErrorCode.UNAUTHORIZED;
        }

        ResponseUtil.writeErrorResponse(response, errorCode);
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        //cookie.setSecure(true);
        //cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}
