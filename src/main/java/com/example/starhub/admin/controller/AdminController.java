package com.example.starhub.admin.controller;

import com.example.starhub.admin.service.AdminService;
import com.example.starhub.dto.request.CreateTechStackRequestDto;
import com.example.starhub.response.code.ResponseCode;
import com.example.starhub.response.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    /**
     * 기술 스택 추가하기
     */
    @Hidden
    @PostMapping
    public ResponseEntity<ResponseDto> createTechStack(@Valid @RequestBody CreateTechStackRequestDto createTechStackRequestDto) {
        adminService.createTechStack(createTechStackRequestDto.getTechStacks());
        return ResponseEntity
                .status(ResponseCode.SUCCESS_CREATE_TECH_STACK.getStatus().value())
                .body(new ResponseDto<>(ResponseCode.SUCCESS_CREATE_TECH_STACK, null));
    }
}
