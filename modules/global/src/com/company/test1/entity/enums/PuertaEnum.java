package com.company.test1.entity.enums;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum PuertaEnum implements EnumClass<String> {

    PRIMERA("primera"),
    SEGUNDA("SEGUNDA");

    private String id;

    PuertaEnum(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static PuertaEnum fromId(String id) {
        for (PuertaEnum at : PuertaEnum.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}