package com.example.starhub.response.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ResponseCode {

    /**
     * status 200
     */


    /**
     * status 201
     */
    SUCCESS_CREATE_USER(HttpStatus.CREATED, "사용자 회원가입을 성공했습니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
