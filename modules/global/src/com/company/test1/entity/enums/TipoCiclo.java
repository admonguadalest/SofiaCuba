package com.company.test1.entity.enums;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum TipoCiclo implements EnumClass<String> {

    OPERATIVO("OPERATIVO"),
    ADMINISTRATIVO("ADMINISTRATIVO");

    private String id;

    TipoCiclo(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static TipoCiclo fromId(String id) {
        for (TipoCiclo at : TipoCiclo.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}