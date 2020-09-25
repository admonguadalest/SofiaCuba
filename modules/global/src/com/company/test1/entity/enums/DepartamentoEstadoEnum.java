package com.company.test1.entity.enums;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum DepartamentoEstadoEnum implements EnumClass<String> {

    VACIO("vacio"),
    OCUPADO("ocupado");

    private String id;

    DepartamentoEstadoEnum(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static DepartamentoEstadoEnum fromId(String id) {
        for (DepartamentoEstadoEnum at : DepartamentoEstadoEnum.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}