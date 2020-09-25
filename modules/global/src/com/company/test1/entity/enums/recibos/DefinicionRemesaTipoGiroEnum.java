package com.company.test1.entity.enums.recibos;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum DefinicionRemesaTipoGiroEnum implements EnumClass<Integer> {

    BANCARIA(1),
    ADMINISTRACION(2),
    AUTOFACTURA(3);

    private Integer id;

    DefinicionRemesaTipoGiroEnum(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static DefinicionRemesaTipoGiroEnum fromId(Integer id) {
        for (DefinicionRemesaTipoGiroEnum at : DefinicionRemesaTipoGiroEnum.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}