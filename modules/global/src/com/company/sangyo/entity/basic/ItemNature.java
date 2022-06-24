package com.company.sangyo.entity.basic;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum ItemNature implements EnumClass<String> {

    MASS_PRODUCTION("量产"),
    EXPERIMENT("实验"),
    STOCK_UP("备货");

    private String id;

    ItemNature(String value) {
        this.id = value;
    }

    @Override
    public String getId() {
        return id;
    }

    @Nullable
    public static ItemNature fromId(String id) {
        for (ItemNature at : ItemNature.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
