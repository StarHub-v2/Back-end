package com.example.starhub.response.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /**
     * 400 BAD_REQUEST: 잘못된 요청
     */
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    USERNAME_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 사용 중인 사용자명입니다."),

    /**
     * 401 UNAUTHORIZED
     */
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증에 실패했습니다."),
    BAD_CREDENTIALS(HttpStatus.UNAUTHORIZED, "잘못된 사용자 이름 또는 비밀번호입니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    INVALID_TOKEN_CATEGORY(HttpStatus.UNAUTHORIZED, "잘못된 토큰 카테고리입니다."),
    TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "Redis에 토큰이 존재하지 않습니다."),
    INVALID_TOKEN_MISMATCH(HttpStatus.UNAUTHORIZED, "요청된 토큰이 서버에 저장된 값과 일치하지 않습니다."),

    /**
     * 403 FORBIDDEN: 접근 금지(권한이 없는 경우)
     */
    POST_FORBIDDEN(HttpStatus.FORBIDDEN, "해당 게시글에 대한 권한이 없습니다."),
    APPLICATION_FORBIDDEN(HttpStatus.FORBIDDEN, "해당 지원서에 대한 권한이 없습니다."),

    /**
     * 404 NOT_FOUND: 리소스를 찾을 수 없음
     */
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사용자를 찾을 수 없습니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 포스트를 찾을 수 없습니다."),
    APPLICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 지원서를 찾을 수 없습니다."),

    /**
     * 409 CONFLICT: 리소스 충돌 (이미 존재하는 경우)
     */
    USER_PROFILE_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 프로필이 생성되었습니다."),
    POST_CREATOR_CANNOT_APPLY(HttpStatus.CONFLICT, "개설자는 자신의 포스트에 지원할 수 없습니다."),

    /**
     * 500 INTERNAL_SERVER_ERROR: 내부 서버 오류
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류입니다."),
    INVALID_RESPONSE_CODE(HttpStatus.INTERNAL_SERVER_ERROR, "유효하지 않은 응답 코드입니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
