package com.company.test1.entity.enums;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum EstadoFianzaEnum implements EnumClass<Integer> {

    NO_INGRESADA_EN_ADMON(0),
    EN_ADMON(1),
    EN_CAMARA(2),
    SOLICITADA_DEVOLUCION(3),
    DEVUELTA(4);

    private Integer id;

    EstadoFianzaEnum(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static EstadoFianzaEnum fromId(Integer id) {
        for (EstadoFianzaEnum at : EstadoFianzaEnum.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}