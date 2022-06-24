package com.company.sangyo.entity.basic;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


/**
 * @author leitengfei
 */

public enum CustomerPartNumberNatureEnum implements EnumClass<String> {

    MASS_PRODUCTION("量产"),
    EXPERIMENT("实验");

    private String id;

    CustomerPartNumberNatureEnum(String value) {
        this.id = value;
    }

    @Override
    public String getId() {
        return id;
    }

    @Nullable
    public static CustomerPartNumberNatureEnum fromId(String id) {
        for (CustomerPartNumberNatureEnum at : CustomerPartNumberNatureEnum.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
