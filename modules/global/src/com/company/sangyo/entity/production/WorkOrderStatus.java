package com.company.sangyo.entity.production;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum WorkOrderStatus implements EnumClass<String> {

    NOT_STARTED("未开始"),
    IN_PROGRESS("进行中"),
    COMPLETED("已完成");

    private String id;

    WorkOrderStatus(String value) {
        this.id = value;
    }

    @Override
    public String getId() {
        return id;
    }

    @Nullable
    public static WorkOrderStatus fromId(String id) {
        for (WorkOrderStatus at : WorkOrderStatus.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
