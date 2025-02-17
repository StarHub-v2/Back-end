package com.example.starhub.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class ConfirmMeetingRequestDto {

    private List<Long> applicationIds; // 지원서 아이디
}
