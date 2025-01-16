package com.example.starhub.dto.response.util;

import com.example.starhub.response.code.ErrorCode;
import com.example.starhub.response.dto.ErrorResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ErrorResponseUtil {

    public static void writeErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {

        ErrorResponseDto errorResponseDto = new ErrorResponseDto(errorCode);

        // 에러 응답 DTO의 timestamp
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        response.setStatus(errorResponseDto.getStatus());
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponseDto));
    }
}
