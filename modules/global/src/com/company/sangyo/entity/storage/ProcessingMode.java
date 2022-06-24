package com.company.sangyo.entity.storage;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum ProcessingMode implements EnumClass<String> {

    WAITING_PROCESS("待处理"),
    NORMAL("正常流转"),
    SCRAP("报废"),
    REWORK("返工"),
    TUNE_WAREHOUSE("调库"),
    OUT_WAREHOUSE("出仓"),
    RETURN_WAREHOUSE("退仓");

    private String id;

    ProcessingMode(String value) {
        this.id = value;
    }

    @Override
    public String getId() {
        return id;
    }

    @Nullable
    public static ProcessingMode fromId(String id) {
        for (ProcessingMode at : ProcessingMode.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
