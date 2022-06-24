package com.company.sangyo.entity.basic;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum PerfectStateEnum implements EnumClass<String> {

    NOT_PERFECTION("未完善"),
    COMPLETED("已完善");

    private String id;

    PerfectStateEnum(String value) {
        this.id = value;
    }

    @Override
    public String getId() {
        return id;
    }

    @Nullable
    public static PerfectStateEnum fromId(String id) {
        for (PerfectStateEnum at : PerfectStateEnum.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
