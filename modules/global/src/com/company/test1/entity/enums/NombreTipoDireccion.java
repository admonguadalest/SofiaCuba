package com.company.test1.entity.enums;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum NombreTipoDireccion implements EnumClass<String> {

    DOMICILIO_SOCIAL("DOMICILIO SOCIAL"),
    DOMICILIO_ADMINISTRADOR("DOMICILIO ADMINISTRADOR"),
    DOMICILIO_INQUILINO("DOMICILIO INQUILINO"),
    DOMICILIO_CONTRACTUAL("DOMICILIO CONTRACTUAL");

    private String id;

    NombreTipoDireccion(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static NombreTipoDireccion fromId(String id) {
        for (NombreTipoDireccion at : NombreTipoDireccion.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}