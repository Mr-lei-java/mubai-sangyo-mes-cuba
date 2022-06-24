package com.company.sangyo.entity.basic;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum ParameterstypeEnum implements EnumClass<String> {

    TEXT("文本"),
    DIGITAL("数字"),
    FILE("文件（包含PDF、word、其他不可预览文件）"),
    TIME("时间"),
    DROP_DOWNBOX("下拉框"),
    PICTURE("图片");

    private String id;

    ParameterstypeEnum(String value) {
        this.id = value;
    }

    @Override
    public String getId() {
        return id;
    }

    @Nullable
    public static ParameterstypeEnum fromId(String id) {
        for (ParameterstypeEnum at : ParameterstypeEnum.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
