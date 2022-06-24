package com.company.sangyo.entity.order;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum OrderStatusEnumeration implements EnumClass<String> {

    CREATED("已创建"),
    ISSUED("已下发"),
    OUTWAREHOUSEOFDELIVERY("出仓中/出仓中"),
    WAREHOUSEDELIVERED("已出仓/已出仓"),
    CLOSED("已关闭");

    private String id;

    OrderStatusEnumeration(String value) {
        this.id = value;
    }

    @Override
    public String getId() {
        return id;
    }

    @Nullable
    public static OrderStatusEnumeration fromId(String id) {
        for (OrderStatusEnumeration at : OrderStatusEnumeration.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
