package com.example.starhub.dto.response.util;

import com.example.starhub.response.code.ErrorCode;
import com.example.starhub.response.code.ResponseCode;
import com.example.starhub.response.dto.ErrorResponseDto;
import com.example.starhub.response.dto.ResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResponseUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 성공적인 응답을 처리하는 메서드
     */
    public static <T> void writeSuccessResponse(HttpServletResponse response, ResponseCode responseCode, T responseDto) throws IOException {
        ResponseDto<T> successResponseDto = new ResponseDto<>(responseCode, responseDto);

        response.setStatus(responseCode.getStatus().value());
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(successResponseDto));
    }

    /**
     * 예외 응답을 처리하는 메소드
     */
    public static void writeErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {

        ErrorResponseDto errorResponseDto = new ErrorResponseDto(errorCode);

        // 에러 응답 DTO의 timestamp
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        response.setStatus(errorResponseDto.getStatus());
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponseDto));
    }
}
