package com.zhuyanlin.labappointment2.CommonEnum;

public enum RoleEnum {
    ADMIN("admin"),
    TEACHER("teacher");

    private final String value;

    RoleEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
