package com.example.starhub.exception;

import com.example.starhub.response.code.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class InvalidTokenMismatchException extends RuntimeException {
    private final ErrorCode errorCode;
}
