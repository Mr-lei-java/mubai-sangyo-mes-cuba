package com.company.sangyo.entity.storage;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum TypeLibrary implements EnumClass<String> {

    SEMI("中转库"),
    FINISHED("成品仓"),
    UNQUALIFIED("不合格品库"),
    RAW("原料库");

    private String id;

    TypeLibrary(String value) {
        this.id = value;
    }

    @Override
    public String getId() {
        return id;
    }

    @Nullable
    public static TypeLibrary fromId(String id) {
        for (TypeLibrary at : TypeLibrary.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
