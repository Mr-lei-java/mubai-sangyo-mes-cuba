package com.company.sangyo.entity.storage;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;

public enum FinishedScreenStatus implements EnumClass<String> {

    NOT_OUT_WAREHOUSE("未出仓"),
    ALREADY_OUT_WAREHOUSE("已出仓"),
    CAN_MATE_ORDER("可配单"),
    REWORK("返工"),
    SCRAPPED("报废"),
    MATE_ORDER("已配单");

    private String id;

    FinishedScreenStatus(String value) {
        this.id = value;
    }

    @Override
    public String getId() {
        return id;
    }

    @Nullable
    public static FinishedScreenStatus fromId(String id) {
        for (FinishedScreenStatus at : FinishedScreenStatus.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
