package com.company.sangyo.entity.order;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


/**
 * @author leitengfei
 */

public enum OrderDataUpdateEnum implements EnumClass<String> {

    CONFIRMED("是"),
    UNCONFIRMED("否");

    private String id;

    OrderDataUpdateEnum(String value) {
        this.id = value;
    }

    @Override
    public String getId() {
        return id;
    }

    @Nullable
    public static OrderDataUpdateEnum fromId(String id) {
        for (OrderDataUpdateEnum at : OrderDataUpdateEnum.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
