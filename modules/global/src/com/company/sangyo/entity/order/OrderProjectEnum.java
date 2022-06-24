package com.company.sangyo.entity.order;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum OrderProjectEnum implements EnumClass<String> {

    CUSTOMER_ORDER("客户订单"),
    CUSTOMER_TEST("客户测试"),
    REPLACEMENT_DEFECTIVEPRO_DUCTS("不良品换货"),
    GIVE_AWAY("赠送"),
    IN_PLANT_EXPERIMENT("厂内实验");

    private String id;

    OrderProjectEnum(String value) {
        this.id = value;
    }

    @Override
    public String getId() {
        return id;
    }

    @Nullable
    public static OrderProjectEnum fromId(String id) {
        for (OrderProjectEnum at : OrderProjectEnum.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
