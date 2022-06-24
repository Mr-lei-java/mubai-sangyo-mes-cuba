package com.company.sangyo.entity.production;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum ShipStatusEnum implements EnumClass<String> {

    NOT_SHIP("尚未出仓"),
    IN_SHIP("出仓中"),
    ALREADYSHIP("已出仓");

    private String id;

    ShipStatusEnum(String value) {
        this.id = value;
    }

    @Override
    public String getId() {
        return id;
    }

    @Nullable
    public static ShipStatusEnum fromId(String id) {
        for (ShipStatusEnum at : ShipStatusEnum.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
