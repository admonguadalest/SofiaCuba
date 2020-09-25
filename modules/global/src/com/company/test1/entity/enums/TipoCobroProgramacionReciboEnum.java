package com.company.test1.entity.enums;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum TipoCobroProgramacionReciboEnum implements EnumClass<Integer> {

    BANCARIO(1),
    ADMINISTRACION(2),
    AUTOFACTURA(3);

    private Integer id;

    TipoCobroProgramacionReciboEnum(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static TipoCobroProgramacionReciboEnum fromId(Integer id) {
        for (TipoCobroProgramacionReciboEnum at : TipoCobroProgramacionReciboEnum.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}