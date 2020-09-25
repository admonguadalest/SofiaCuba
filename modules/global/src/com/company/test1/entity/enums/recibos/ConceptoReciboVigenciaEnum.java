package com.company.test1.entity.enums.recibos;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum ConceptoReciboVigenciaEnum implements EnumClass<Integer> {

    PERMANENTE(1),
    ENTRE_FECHAS(2),
    ACTIVACION(3),
    NUMERO_EMISIONES(4);

    private Integer id;

    ConceptoReciboVigenciaEnum(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static ConceptoReciboVigenciaEnum fromId(Integer id) {
        for (ConceptoReciboVigenciaEnum at : ConceptoReciboVigenciaEnum.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}