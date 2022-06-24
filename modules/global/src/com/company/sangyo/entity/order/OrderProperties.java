package com.company.sangyo.entity.order;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum OrderProperties implements EnumClass<String> {

    INSELL("内销"),
    ONSELL("外销");

    private String id;

    OrderProperties(String value) {
        this.id = value;
    }

    @Override
    public String getId() {
        return id;
    }

    @Nullable
    public static OrderProperties fromId(String id) {
        for (OrderProperties at : OrderProperties.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
