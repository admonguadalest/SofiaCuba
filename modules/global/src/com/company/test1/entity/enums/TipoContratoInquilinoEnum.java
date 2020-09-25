package com.company.test1.entity.enums;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum TipoContratoInquilinoEnum implements EnumClass<Integer> {

    ANTIGUA_LAU(0),
    NUEVA_LAU(1);

    private Integer id;

    TipoContratoInquilinoEnum(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static TipoContratoInquilinoEnum fromId(Integer id) {
        for (TipoContratoInquilinoEnum at : TipoContratoInquilinoEnum.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}