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

    @ExceptionHandler(InvalidResponseCodeException.class)
    protected ResponseEntity<ErrorResponseDto> handleInvalidResponseCodeException(final InvalidResponseCodeException e) {
        log.error("handleInvalidResponseCodeException : {}", e.getErrorCode().getMessage());
        return ResponseEntity
                .status(ErrorCode.INVALID_RESPONSE_CODE.getStatus().value())
                .body(new ErrorResponseDto(ErrorCode.INVALID_RESPONSE_CODE));
    }

    @ExceptionHandler(BadRequestException.class)
    protected ResponseEntity<ErrorResponseDto> handleBadRequestException(final BadRequestException e) {
        log.error("handleBadRequestException : {}", e.getErrorCode().getMessage());
        return ResponseEntity
                .status(ErrorCode.BAD_REQUEST.getStatus().value())
                .body(new ErrorResponseDto(ErrorCode.BAD_REQUEST));
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

    @ExceptionHandler(TokenExpiredException.class)
    protected ResponseEntity<ErrorResponseDto> handleTokenExpiredException(final TokenExpiredException e) {
        log.error("handleTokenExpiredException : {}", e.getErrorCode().getMessage());
        return ResponseEntity
                .status(ErrorCode.TOKEN_EXPIRED.getStatus().value())
                .body(new ErrorResponseDto(ErrorCode.TOKEN_EXPIRED));
    }

    @ExceptionHandler(InvalidTokenCategoryException.class)
    protected ResponseEntity<ErrorResponseDto> handleInvalidTokenCategoryException(final InvalidTokenCategoryException e) {
        log.error("handleInvalidTokenCategoryException : {}", e.getErrorCode().getMessage());
        return ResponseEntity
                .status(ErrorCode.UNAUTHORIZED.getStatus().value())
                .body(new ErrorResponseDto(ErrorCode.UNAUTHORIZED));
    }

    @ExceptionHandler(TokenNotFoundInRedisException.class)
    protected ResponseEntity<ErrorResponseDto> handleTokenNotFoundInRedisException(final TokenNotFoundInRedisException e) {
        log.error("handleTokenNotFoundInRedisException : {}", e.getErrorCode().getMessage());
        return ResponseEntity
                .status(ErrorCode.UNAUTHORIZED.getStatus().value())
                .body(new ErrorResponseDto(ErrorCode.UNAUTHORIZED));
    }

    @ExceptionHandler(InvalidTokenMismatchException.class)
    protected ResponseEntity<ErrorResponseDto> handleInvalidTokenException(final InvalidTokenMismatchException e) {
        log.error("handleInvalidTokenException : {}", e.getErrorCode().getMessage());
        return ResponseEntity
                .status(ErrorCode.UNAUTHORIZED.getStatus().value())
                .body(new ErrorResponseDto(ErrorCode.UNAUTHORIZED));
    }

    @ExceptionHandler(UserProfileAlreadyExistsException.class)
    protected ResponseEntity<ErrorResponseDto> handleUserProfileAlreadyExistsException(final UserProfileAlreadyExistsException e) {
        log.error("handleUserProfileAlreadyExistsException : {}", e.getErrorCode().getMessage());
        return ResponseEntity
                .status(ErrorCode.USER_PROFILE_ALREADY_EXISTS.getStatus().value())
                .body(new ErrorResponseDto(ErrorCode.USER_PROFILE_ALREADY_EXISTS));
    }

    /**
     * MEETING
     */
    @ExceptionHandler(MeetingNotFoundException.class)
    protected ResponseEntity<ErrorResponseDto> handleMeetingNotFoundException(final MeetingNotFoundException e) {
        log.error("handleMeetingNotFoundException : {}", e.getErrorCode().getMessage());
        return ResponseEntity
                .status(ErrorCode.MEETING_NOT_FOUND.getStatus().value())
                .body(new ErrorResponseDto(ErrorCode.MEETING_NOT_FOUND));
    }

    @ExceptionHandler(CreatorAuthorizationException.class)
    protected ResponseEntity<ErrorResponseDto> handleCreatorAuthorizationException(final CreatorAuthorizationException e) {
        log.error("handleCreatorAuthorizationException : {}", e.getErrorCode().getMessage());
        return ResponseEntity
                .status(ErrorCode.MEETING_FORBIDDEN.getStatus().value())
                .body(new ErrorResponseDto(ErrorCode.MEETING_FORBIDDEN));
    }

    /**
     * APPLICATION
     */
    @ExceptionHandler(MeetingCreatorCannotApplyException.class)
    protected ResponseEntity<ErrorResponseDto> handleMeetingCreatorCannotApplyException(final MeetingCreatorCannotApplyException e) {
        log.error("handleMeetingCreatorCannotApplyException : {}", e.getErrorCode().getMessage());
        return ResponseEntity
                .status(ErrorCode.MEETING_CREATOR_CANNOT_APPLY.getStatus().value())
                .body(new ErrorResponseDto(ErrorCode.MEETING_CREATOR_CANNOT_APPLY));
    }

    @ExceptionHandler(ApplicationNotFoundException.class)
    protected ResponseEntity<ErrorResponseDto> handleApplicationNotFoundException(final ApplicationNotFoundException e) {
        log.error("handleApplicationNotFoundException : {}", e.getErrorCode().getMessage());
        return ResponseEntity
                .status(ErrorCode.APPLICATION_NOT_FOUND.getStatus().value())
                .body(new ErrorResponseDto(ErrorCode.APPLICATION_NOT_FOUND));
    }

    @ExceptionHandler(ApplicantAuthorizationException.class)
    protected ResponseEntity<ErrorResponseDto> handleApplicantAuthorizationException(final ApplicantAuthorizationException e) {
        log.error("handleApplicantAuthorizationException : {}", e.getErrorCode().getMessage());
        return ResponseEntity
                .status(ErrorCode.APPLICATION_FORBIDDEN.getStatus().value())
                .body(new ErrorResponseDto(ErrorCode.APPLICATION_FORBIDDEN));
    }

    @ExceptionHandler(StudyConfirmedException.class)
    protected ResponseEntity<ErrorResponseDto> handleStudyConfirmedException(final StudyConfirmedException e) {
        log.error("StudyConfirmedException : {}", e.getErrorCode().getMessage());
        return ResponseEntity
                .status(ErrorCode.STUDY_CONFIRMED.getStatus().value())
                .body(new ErrorResponseDto(ErrorCode.STUDY_CONFIRMED));
    }

    @ExceptionHandler(DuplicateApplicationException.class)
    protected ResponseEntity<ErrorResponseDto> handleDuplicateApplicationException(final DuplicateApplicationException e) {
        log.error("handleDuplicateApplicationException : {}", e.getErrorCode().getMessage());
        return ResponseEntity
                .status(ErrorCode.DUPLICATE_APPLICATION.getStatus().value())
                .body(new ErrorResponseDto(ErrorCode.DUPLICATE_APPLICATION));
    }

}
