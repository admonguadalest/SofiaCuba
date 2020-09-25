package com.company.test1.entity.enums;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum EstadoCivilEnum implements EnumClass<String> {

    SOLTERO("soltero"),
    CASADO("casado"),
    DIVORCIADO("divorciado");

    private String id;

    EstadoCivilEnum(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static EstadoCivilEnum fromId(String id) {
        for (EstadoCivilEnum at : EstadoCivilEnum.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}