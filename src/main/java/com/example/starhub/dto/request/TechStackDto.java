package com.example.starhub.dto.request;

import com.example.starhub.entity.enums.TechCategory;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Builder
@Getter
public class TechStackDto {

    @NotBlank(message = "기술 스택 이름은 필수입니다")
    @Size(max = 100, message = "기술 스택 이름은 100자를 초과할 수 없습니다")
    private String name;

    @NotNull(message = "카테고리는 필수입니다")
    private TechCategory category;
}
