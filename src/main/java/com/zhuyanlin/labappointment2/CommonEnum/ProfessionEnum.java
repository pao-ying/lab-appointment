package com.zhuyanlin.labappointment2.CommonEnum;

public enum ProfessionEnum {
    PROFESSOR("教授"),
    ASSOCIATE_PROFESSOR("副教授"),
    LECTURER("讲师");

    private final String value;

    ProfessionEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
