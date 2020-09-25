package com.company.test1.entity.enums;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum FormaDeCobroContratoInquilinoEnum implements EnumClass<Integer> {

    ADMINISTRACION(0),
    DOMICILIACION(1);

    private Integer id;

    FormaDeCobroContratoInquilinoEnum(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static FormaDeCobroContratoInquilinoEnum fromId(Integer id) {
        for (FormaDeCobroContratoInquilinoEnum at : FormaDeCobroContratoInquilinoEnum.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}