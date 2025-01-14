package com.example.starhub.entity.enums;

public enum TechCategory {
    BACKEND("백엔드 개발"),
    FRONTEND("프론트엔드 개발"),
    MOBILE("모바일 개발"),
    OTHER("기타 기술 스택");

    private final String description;

    TechCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
