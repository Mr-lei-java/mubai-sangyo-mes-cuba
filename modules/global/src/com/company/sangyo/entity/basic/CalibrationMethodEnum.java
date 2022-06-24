package com.company.sangyo.entity.basic;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum CalibrationMethodEnum implements EnumClass<String> {

    LESS_THAN("<"),
    GREATER(">"),
    EQUAL("="),
    GREATER_EQUAL(">="),
    LESS_THAN_EQUAL("<="),
    CONTAINS("Contains");

    private String id;

    CalibrationMethodEnum(String value) {
        this.id = value;
    }

    @Override
    public String getId() {
        return id;
    }

    @Nullable
    public static CalibrationMethodEnum fromId(String id) {
        for (CalibrationMethodEnum at : CalibrationMethodEnum.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
