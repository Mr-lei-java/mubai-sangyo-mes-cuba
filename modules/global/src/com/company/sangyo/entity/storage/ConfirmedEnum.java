package com.company.sangyo.entity.storage;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum ConfirmedEnum implements EnumClass<String> {

    CONFIRMED("已确认"),
    UNCONFIRMED("未确认");

    private String id;

    ConfirmedEnum(String value) {
        this.id = value;
    }

    @Override
    public String getId() {
        return id;
    }

    @Nullable
    public static ConfirmedEnum fromId(String id) {
        for (ConfirmedEnum at : ConfirmedEnum.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
