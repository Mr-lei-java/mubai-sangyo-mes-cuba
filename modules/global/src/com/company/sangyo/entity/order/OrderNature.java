package com.company.sangyo.entity.order;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum OrderNature implements EnumClass<String> {

    INPLANTCHOICE("厂内备货"),
    INPLANTEXPERIMENT("实验"),
    MARKETORDES("销售订单");

    private String id;

    OrderNature(String value) {
        this.id = value;
    }

    @Override
    public String getId() {
        return id;
    }

    @Nullable
    public static OrderNature fromId(String id) {
        for (OrderNature at : OrderNature.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
