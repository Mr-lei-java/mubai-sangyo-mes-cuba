package com.company.sangyo.entity.basic;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum TimingCalibrationEnum implements EnumClass<String> {

    CHECK_ON_OUT("CHECK_ON_OUT"),
    CHECK_ON_FILL("CHECK_ON_FILL");

    private String id;

    TimingCalibrationEnum(String value) {
        this.id = value;
    }

    @Override
    public String getId() {
        return id;
    }

    @Nullable
    public static TimingCalibrationEnum fromId(String id) {
        for (TimingCalibrationEnum at : TimingCalibrationEnum.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
