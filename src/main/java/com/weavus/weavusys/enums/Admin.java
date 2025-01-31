package com.weavus.weavusys.enums;

public enum Admin {
    GENERAL(0),
    ADMIN(1);

   private final int value;

    Admin(int value) {
         this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Admin fromValue(int value) {
        for (Admin status : Admin.values()) {
            if (status.getValue() == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("유효하지 않은 값: " + value);
    }

}
