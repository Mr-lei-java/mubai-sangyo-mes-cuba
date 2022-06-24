package com.company.sangyo.entity.basic;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum ScreenStatusEnum implements EnumClass<String> {

    NOTOUT("未出仓"),
    OUTSTOCK("已出仓"),
    CANMATCHWORKORDER("可配单"),
    REWORK("返工"),
    SCRAPPED("报废"),
    ALREADY_MATCH_ORDER("已配单");

    private String id;

    ScreenStatusEnum(String value) {
        this.id = value;
    }

    @Override
    public String getId() {
        return id;
    }

    @Nullable
    public static ScreenStatusEnum fromId(String id) {
        for (ScreenStatusEnum at : ScreenStatusEnum.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
