package com.example.starhub.entity.enums;

public enum ApplicationStatus {
    APPLIED("지원"),
    CONFIRMED("확정");

    private final String description;

    ApplicationStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

