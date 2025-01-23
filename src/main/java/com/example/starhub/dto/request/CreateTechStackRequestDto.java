package com.example.starhub.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
public class CreateTechStackRequestDto {

    @NotNull(message = "기술 스택 목록은 필수입니다")
    @Size(min = 1, message = "최소 하나 이상의 기술 스택이 필요합니다")
    private List<TechStackDto> techStacks;
}

