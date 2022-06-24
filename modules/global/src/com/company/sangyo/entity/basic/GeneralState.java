package com.company.sangyo.entity.basic;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum GeneralState implements EnumClass<String> {

    ENABLE("启用"),
    DEACTIVATE("停用");

    private String id;

    GeneralState(String value) {
        this.id = value;
    }

    @Override
    public String getId() {
        return id;
    }

    @Nullable
    public static GeneralState fromId(String id) {
        for (GeneralState at : GeneralState.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
