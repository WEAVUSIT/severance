package com.weavus.weavusys.enums;

public enum WorkScheduleType {
   출근(0),
    결근(1),
    유급휴가(2),
    대휴(3),
    특별휴가(4),
    휴일대체(5),
    경조휴가(6),
    휴일출근(7);



   private final int value;

    WorkScheduleType(int value) {
         this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static WorkScheduleType fromValue(int value) {
        for (WorkScheduleType status : WorkScheduleType.values()) {
            if (status.getValue() == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("유효하지 않은 값: " + value);
    }

}
