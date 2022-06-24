package com.company.sangyo.entity.production;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum DispatchListType implements EnumClass<String> {

    NORMAL("NORMAL"),
    REWORK("REWORK");

    private String id;

    DispatchListType(String value) {
        this.id = value;
    }

    @Override
    public String getId() {
        return id;
    }

    @Nullable
    public static DispatchListType fromId(String id) {
        for (DispatchListType at : DispatchListType.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
