package com.example.starhub.dto.response.util;

import com.example.starhub.response.code.ErrorCode;
import com.example.starhub.response.code.ResponseCode;
import com.example.starhub.response.dto.ErrorResponseDto;
import com.example.starhub.response.dto.ResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class ResponseUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 성공적인 응답을 처리하는 메서드
     */
    public static <T> void writeSuccessResponse(HttpServletResponse response, ResponseCode responseCode, T responseDto) {
        try {
            ResponseDto<T> successResponseDto = new ResponseDto<>(responseCode, responseDto);

            response.setStatus(responseCode.getStatus().value());
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().write(new ObjectMapper().writeValueAsString(successResponseDto));
        } catch (IOException e) {
            handleIOException(response, e);
        }
    }

    /**
     * 예외 응답을 처리하는 메소드
     */
    public static void writeErrorResponse(HttpServletResponse response, ErrorCode errorCode) {
        try {
            ErrorResponseDto errorResponseDto = new ErrorResponseDto(errorCode);

            // 에러 응답 DTO의 timestamp
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

            response.setStatus(errorResponseDto.getStatus());
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(errorResponseDto));
        } catch (IOException e) {
            handleIOException(response, e);
        }
    }

    /**
     * IOException 발생 시, 처리하는 메소드
     */
    private static void handleIOException(HttpServletResponse response, IOException e) {

        log.error("응답을 작성하는 동안 IOException 발생: {}", e.getMessage(), e);

        try {
            // IOException이 발생한 경우, 적절한 에러 응답을 클라이언트에게 보내기
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().write("{\"error\":\"Internal Server Error\"}");
        } catch (IOException innerException) {
            log.error("내부 예외 처리 중 IOException 발생: {}", innerException.getMessage(), innerException);
        }
    }
}

