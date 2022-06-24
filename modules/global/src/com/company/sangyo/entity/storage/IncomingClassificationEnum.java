package com.company.sangyo.entity.storage;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum IncomingClassificationEnum implements EnumClass<String> {

    PROCESS_ABNORMALITIES("过程异常"),
    CUSTOMER_RETURNS("客户退货");

    private String id;

    IncomingClassificationEnum(String value) {
        this.id = value;
    }

    @Override
    public String getId() {
        return id;
    }

    @Nullable
    public static IncomingClassificationEnum fromId(String id) {
        for (IncomingClassificationEnum at : IncomingClassificationEnum.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
