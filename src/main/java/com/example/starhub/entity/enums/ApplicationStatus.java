package com.example.starhub.entity.enums;

public enum ApplicationStatus {
    PENDING("지원 대기 중"),     // 대기 중인 상태
    APPROVED("모임원 확정"),    // 모임원으로 확정된 상태
    REJECTED("모임원 거절");    // 거절된 상태

    private final String description;

    ApplicationStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
