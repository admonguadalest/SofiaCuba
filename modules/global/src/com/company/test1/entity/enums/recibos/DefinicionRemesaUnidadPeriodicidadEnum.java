package com.company.test1.entity.enums.recibos;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum DefinicionRemesaUnidadPeriodicidadEnum implements EnumClass<Integer> {

    MANUAL(1),
    SEMANAL(2),
    MENSUAL(3),
    BIMENSUAL(4),
    TRIMESTRAL(5),
    SEMESTRAL(6),
    ANUAL(7);

    private Integer id;

    DefinicionRemesaUnidadPeriodicidadEnum(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static DefinicionRemesaUnidadPeriodicidadEnum fromId(Integer id) {
        for (DefinicionRemesaUnidadPeriodicidadEnum at : DefinicionRemesaUnidadPeriodicidadEnum.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}