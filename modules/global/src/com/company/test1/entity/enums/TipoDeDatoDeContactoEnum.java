package com.company.test1.entity.enums;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum TipoDeDatoDeContactoEnum implements EnumClass<String> {

    TELEFONO("TELEFONO"),
    MOBIL("MOBIL"),
    CORREO_ELECTRONICO("CORREO ELECTRONICO"),
    OTRO("OTRO");

    private String id;

    TipoDeDatoDeContactoEnum(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static TipoDeDatoDeContactoEnum fromId(String id) {
        for (TipoDeDatoDeContactoEnum at : TipoDeDatoDeContactoEnum.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}