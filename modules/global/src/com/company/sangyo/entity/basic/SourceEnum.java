package com.company.sangyo.entity.basic;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum SourceEnum implements EnumClass<String> {

    INPLANT("厂内"),
    CUSTOMER("客户");

    private String id;

    SourceEnum(String value) {
        this.id = value;
    }

    @Override
    public String getId() {
        return id;
    }

    @Nullable
    public static SourceEnum fromId(String id) {
        for (SourceEnum at : SourceEnum.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
