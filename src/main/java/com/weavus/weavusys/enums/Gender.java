package com.weavus.weavusys.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Gender {
    남성(0, "남성"),
    여성(1, "여성");
    private final int value;
    private final String displayName;

    Gender(int value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    public int getValue() {
        return value;
    }
    @JsonValue // JSON 변환 시 이 값 사용
    public String getDisplayName() {
        return displayName;
    }

    public static Gender fromValue(int value) {
        for (Gender gender : Gender.values()) {
            if (gender.getValue() == value) {
                return gender;
            }
        }
        throw new IllegalArgumentException("유효하지 않은 값: " + value);
    }
    public static Gender fromDisplayName(String displayName) {
        for (Gender gender : Gender.values()) {
            if (gender.getDisplayName().equals(displayName)) {
                return gender;
            }
        }
        throw new IllegalArgumentException("유효하지 않은 성별: " + displayName);
    }
}
