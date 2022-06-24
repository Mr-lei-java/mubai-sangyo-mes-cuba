package com.company.sangyo.entity.basic;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum InPlantPartNumbertypeEnumerate implements EnumClass<String> {

    RAWMATERIALS("原辅料"),
    FINISHEDPRODUCT("成品"),
    SEMIFINISHEDPRODUCTS("半成品");

    private String id;

    InPlantPartNumbertypeEnumerate(String value) {
        this.id = value;
    }

    @Override
    public String getId() {
        return id;
    }

    @Nullable
    public static InPlantPartNumbertypeEnumerate fromId(String id) {
        for (InPlantPartNumbertypeEnumerate at : InPlantPartNumbertypeEnumerate.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
