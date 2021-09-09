package com.company.test1.entity.enums;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum EstadoFianzaEnum implements EnumClass<Integer> {

    _0_NO_INGRESADA_EN_ADMON(0),
    _1_EN_ADMON(1),
    _2_EN_CAMARA(2),
    _3_SOLICITADA_DEVOLUCION(3),
    _4_DEVUELTA_ADMON(4),
    _5_DEVUELTA_INQUILINO(4);

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