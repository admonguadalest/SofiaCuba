package com.company.test1.entity.enums;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum PisoEnum implements EnumClass<String> {

    PLANTA_BAJA("PLANTA BAJA"),
    PRINCIPAL("PRINCIPAL"),
    ENTRESUELO("ENTRESUELO"),
    PRIMERO("PRIMERO"),
    SEGUNDO("SEGUNDO");

    private String id;

    PisoEnum(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static PisoEnum fromId(String id) {
        for (PisoEnum at : PisoEnum.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }

    public String toString(){
        return this.id;
    }
}