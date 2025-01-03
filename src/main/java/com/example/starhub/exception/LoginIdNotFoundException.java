package com.example.starhub.exception;

import com.example.starhub.code.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginIdNotFoundException extends RuntimeException{
    private final ErrorCode errorCode;
}