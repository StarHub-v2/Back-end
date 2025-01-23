package com.example.starhub.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateTechStackRequestDto {
    private List<TechStackDto> techStacks;
}

