package com.example.starhub.response.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ResponseCode {

    /**
     * USER
     */
    SUCCESS_CREATE_USER(HttpStatus.CREATED, "사용자 회원가입을 성공했습니다."),
    SUCCESS_CHECK_ID(HttpStatus.OK, "아이디 중복 확인이 완료되었습니다."),
    SUCCESS_CREATE_PROFILE(HttpStatus.CREATED, "프로필이 성공적으로 생성되었습니다."),
    SUCCESS_LOGIN(HttpStatus.OK, "사용자 로그인을 성공했습니다."),
    SUCCESS_LOGOUT(HttpStatus.OK, "사용자 로그아웃을 성공했습니다."),
    SUCCESS_REISSUE_TOKEN(HttpStatus.OK, "토큰 재발급을 성공했습니다."),

    /**
     * POST
     */
    SUCCESS_CREATE_POST(HttpStatus.CREATED, "포스트가 성공적으로 생성되었습니다."),
    SUCCESS_GET_POST_LIST(HttpStatus.OK, "포스트 목록을 성공적으로 불러왔습니다."),
    SUCCESS_GET_POST_DETAIL(HttpStatus.OK, "포스트 상세 정보를 성공적으로 불러왔습니다."),
    SUCCESS_UPDATE_POST(HttpStatus.OK, "포스트가 성공적으로 수정되었습니다."),
    SUCCESS_DELETE_POST(HttpStatus.OK, "포스트가 성공적으로 삭제되었습니다."),

    ;

    private final HttpStatus status;
    private final String message;
}
