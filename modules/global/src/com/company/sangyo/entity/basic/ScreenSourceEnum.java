package com.company.sangyo.entity.basic;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum ScreenSourceEnum implements EnumClass<String> {

    IN_PLANT("厂内"),
    UPLOAD_PARAMETER("上传参数");

    private String id;

    ScreenSourceEnum(String value) {
        this.id = value;
    }

    @Override
    public String getId() {
        return id;
    }

    @Nullable
    public static ScreenSourceEnum fromId(String id) {
        for (ScreenSourceEnum at : ScreenSourceEnum.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
