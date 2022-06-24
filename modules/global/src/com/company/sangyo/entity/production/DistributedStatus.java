package com.company.sangyo.entity.production;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum DistributedStatus implements EnumClass<String> {

    NOT_DISTRIBUTED("未下发"),
    DISTRIBUTING("下发中"),
    DISTRIBUTED("已下发");

    private String id;

    DistributedStatus(String value) {
        this.id = value;
    }

    @Override
    public String getId() {
        return id;
    }

    @Nullable
    public static DistributedStatus fromId(String id) {
        for (DistributedStatus at : DistributedStatus.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
