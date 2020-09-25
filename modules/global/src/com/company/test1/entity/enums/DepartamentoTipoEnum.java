package com.company.test1.entity.enums;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum DepartamentoTipoEnum implements EnumClass<String> {

    VIVIENDA("vivienda"),
    LOCAL("local");

    private String id;

    DepartamentoTipoEnum(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static DepartamentoTipoEnum fromId(String id) {
        for (DepartamentoTipoEnum at : DepartamentoTipoEnum.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}