package com.company.test1.entity;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum SiNoEnum implements EnumClass<String> {

    SI("Si"),
    NO("No");

    private String id;

    SiNoEnum(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static SiNoEnum fromId(String id) {
        for (SiNoEnum at : SiNoEnum.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}