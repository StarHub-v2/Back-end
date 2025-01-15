package com.example.starhub.response.dto;

import com.example.starhub.exception.InvalidResponseCodeException;
import com.example.starhub.response.code.ErrorCode;
import com.example.starhub.response.code.ResponseCode;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponseDto<T> {
    private Integer status;
    private String code;
    private String message;
    private T data;

    public ResponseDto(ResponseCode responseCode, T data) {

        if (responseCode == null) {
            throw new InvalidResponseCodeException(ErrorCode.INVALID_RESPONSE_CODE);
        }

        this.status = responseCode.getStatus().value();
        this.code = responseCode.name();
        this.message = responseCode.getMessage();
        this.data = data;
    }
}
