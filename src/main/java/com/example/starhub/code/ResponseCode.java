package com.example.starhub.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ResponseCode {
    SUCCESS_REGISTER(HttpStatus.OK, "회원가입을 성공했습니다."),
    SUCCESS_LOGIN(HttpStatus.OK, "로그인을 성공했습니다."),
    SUCCESS_LOGOUT(HttpStatus.OK, "로그아웃을 성공했습니다."),

    SUCCESS_CREATE_POST(HttpStatus.OK, "게시글 작성을 성공했습니다."),
    SUCCESS_GET_POSTS(HttpStatus.OK, "게시글 목록을 가져오기를 성공했습니다."),
    SUCCESS_GET_POST(HttpStatus.OK, "게시글을 가져오기를 성공했습니다."),

    SUCCESS_CREATE_COMMENT(HttpStatus.OK, "댓글 작성을 성공했습니다."),
    SUCCESS_GET_COMMENTS(HttpStatus.OK, "댓글 목록을 가져오기를 성공했습니다."),
    SUCCESS_PICK_COMMENT(HttpStatus.OK, "지원자 선택하기를 성공했습니다."),
    SUCCESS_GET_PICKED_COMMENTS(HttpStatus.OK, "지원자 목록을 가져오기를 성공했습니다.")
    ;
    private final HttpStatus status;
    private final String message;
}
