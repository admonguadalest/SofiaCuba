package com.company.test1.entity.enums.recibos;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum ReciboGradoImpago implements EnumClass<Integer> {

    ORDINARIO(1),
    PENDIENTE(2),
    INCOBRABLE(3);

    private Integer id;

    ReciboGradoImpago(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static ReciboGradoImpago fromId(Integer id) {
        for (ReciboGradoImpago at : ReciboGradoImpago.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}