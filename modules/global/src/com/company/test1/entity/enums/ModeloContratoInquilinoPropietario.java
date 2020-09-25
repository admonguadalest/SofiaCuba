package com.company.test1.entity.enums;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum ModeloContratoInquilinoPropietario implements EnumClass<String> {

    INQUILINO("inquilino"),
    PROPIETARIO("propietario");

    private String id;

    ModeloContratoInquilinoPropietario(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static ModeloContratoInquilinoPropietario fromId(String id) {
        for (ModeloContratoInquilinoPropietario at : ModeloContratoInquilinoPropietario.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}