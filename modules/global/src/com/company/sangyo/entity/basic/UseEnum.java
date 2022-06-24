package com.company.sangyo.entity.basic;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum UseEnum implements EnumClass<String> {

    UPLOADPARAMETERS("上传参数"),
    PARTNUMBERPARAMETERS("料号参数");

    private String id;

    UseEnum(String value) {
        this.id = value;
    }

    @Override
    public String getId() {
        return id;
    }

    @Nullable
    public static UseEnum fromId(String id) {
        for (UseEnum at : UseEnum.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
