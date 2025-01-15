package com.example.starhub.exception;

import com.example.starhub.response.code.ErrorCode;
import com.example.starhub.response.dto.ErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 입력값 검증
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder builder = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            builder.append(fieldError.getDefaultMessage());
        }

        log.error("handleMethodArgumentNotValidException : {}", builder.toString());
        return ResponseEntity
                .status(ErrorCode.BAD_REQUEST.getStatus().value())
                .body(new ErrorResponseDto(ErrorCode.BAD_REQUEST, builder.toString()));
    }

    /**
     * User
     */
    @ExceptionHandler(UsernameAlreadyExistsException.class)
    protected ResponseEntity<ErrorResponseDto> handleUsernameAlreadyExistsException(final UsernameAlreadyExistsException e) {
        log.error("handleUsernameAlreadyExistsException : {}", e.getErrorCode().getMessage());
        return ResponseEntity
                .status(ErrorCode.USERNAME_ALREADY_EXISTS.getStatus().value())
                .body(new ErrorResponseDto(ErrorCode.USERNAME_ALREADY_EXISTS));
    }

    @ExceptionHandler(UserNotFoundException.class)
    protected ResponseEntity<ErrorResponseDto> handleUserNotFoundException(final UserNotFoundException e) {
        log.error("handleUserNotFoundException : {}", e.getErrorCode().getMessage());
        return ResponseEntity
                .status(ErrorCode.USER_NOT_FOUND.getStatus().value())
                .body(new ErrorResponseDto(ErrorCode.USER_NOT_FOUND));
    }


}
