package com.example.starhub.controller;

import com.example.starhub.controller.docs.TechStackControllerDocs;
import com.example.starhub.dto.request.CreateTechStackRequestDto;
import com.example.starhub.dto.response.TechStackResponseDto;
import com.example.starhub.response.code.ResponseCode;
import com.example.starhub.response.dto.ResponseDto;
import com.example.starhub.service.TechStackService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/techStacks")
@RequiredArgsConstructor
public class TechStackController implements TechStackControllerDocs {

    private final TechStackService techStackService;

    /**
     * 기술 스택 불러오기
     */
    @GetMapping
    public ResponseEntity<ResponseDto<List<TechStackResponseDto>>> getTechStack() {
        List<TechStackResponseDto> res = techStackService.getTechStack();
        return ResponseEntity
                .status(ResponseCode.SUCCESS_GET_TECH_STACK.getStatus().value())
                .body(new ResponseDto<>(ResponseCode.SUCCESS_GET_TECH_STACK, res));
    }

    /**
     * 기술 스택 추가하기
     */
    @Hidden
    @PostMapping
    public ResponseEntity<ResponseDto> createTechStack(@RequestBody CreateTechStackRequestDto createTechStackRequestDto) {
        techStackService.createTechStack(createTechStackRequestDto.getTechStacks());
        return ResponseEntity
                .status(ResponseCode.SUCCESS_CREATE_TECH_STACK.getStatus().value())
                .body(new ResponseDto<>(ResponseCode.SUCCESS_CREATE_TECH_STACK, null));
    }

}
