package com.example.starhub.dto.response;

import com.example.starhub.entity.TechStackEntity;
import com.example.starhub.entity.enums.TechCategory;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TechStackResponseDto {
    private Long id;
    private String name;
    private TechCategory category;

    public static TechStackResponseDto fromEntity(TechStackEntity techStack) {
        return TechStackResponseDto.builder()
                .id(techStack.getId())
                .name(techStack.getName())
                .category(techStack.getCategory())
                .build();
    }
}
