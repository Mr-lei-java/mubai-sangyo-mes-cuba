package com.company.sangyo.entity.storage;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum ReworkType implements EnumClass<String> {

    A("A"),
    B("B");

    private String id;

    ReworkType(String value) {
        this.id = value;
    }

    @Override
    public String getId() {
        return id;
    }

    @Nullable
    public static ReworkType fromId(String id) {
        for (ReworkType at : ReworkType.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
