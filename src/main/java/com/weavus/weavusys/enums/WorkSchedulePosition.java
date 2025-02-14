package com.weavus.weavusys.enums;

public enum WorkSchedulePosition {
   현장(0),
    본사(1),
    재택근무(2),
    휴가(3),;



   private final int value;

    WorkSchedulePosition(int value) {
         this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static WorkSchedulePosition fromValue(int value) {
        for (WorkSchedulePosition status : WorkSchedulePosition.values()) {
            if (status.getValue() == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("유효하지 않은 값: " + value);
    }

}
