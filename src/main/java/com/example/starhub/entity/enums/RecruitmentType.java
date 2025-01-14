package com.example.starhub.entity.enums;

public enum RecruitmentType {
    STUDY("스터디"),
    PROJECT("프로젝트");

    private final String description;

    RecruitmentType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

