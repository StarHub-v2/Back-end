package com.example.starhub.controller.docs;

import com.example.starhub.dto.response.TechStackResponseDto;
import com.example.starhub.response.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "TechStackController", description = "기술 스택 관련 API")
public interface TechStackControllerDocs {

    /**
     * 기술 스택 불러오기
     */
    @Operation(
            summary = "기술 스택 불러오기",
            description = "기술 스택 불러오기를 진행합니다."
    )
    ResponseEntity<ResponseDto<List<TechStackResponseDto>>> getTechStack();

}
