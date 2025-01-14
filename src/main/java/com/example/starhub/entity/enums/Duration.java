package com.example.starhub.entity.enums;

public enum Duration {
    ONE_WEEK("1주"),
    TWO_WEEKS("2주"),
    ONE_MONTH("1개월"),
    TWO_MONTHS("2개월"),
    THREE_MONTHS("3개월"),
    SIX_MONTHS("6개월"),
    ONE_YEAR("1년"),
    MORE_THAN_ONE_YEAR("1년 이상");

    private final String description;

    Duration(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
