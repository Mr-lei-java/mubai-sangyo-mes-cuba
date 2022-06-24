package com.company.sangyo.entity.basic;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


/**
 * @author leitengfei
 */

public enum MultiCheckType implements EnumClass<String> {

    A("A"),
    B("B"),
    C("C");

    private String id;

    MultiCheckType(String value) {
        this.id = value;
    }

    @Override
    public String getId() {
        return id;
    }

    @Nullable
    public static MultiCheckType fromId(String id) {
        for (MultiCheckType at : MultiCheckType.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
