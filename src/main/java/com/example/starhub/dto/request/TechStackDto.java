package com.example.starhub.dto.request;

import com.example.starhub.entity.enums.TechCategory;
import lombok.Getter;

@Getter
public class TechStackDto {
    private String name;
    private TechCategory category;

}
