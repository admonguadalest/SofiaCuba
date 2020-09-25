package com.company.test1.entity.enums.recibos;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum ConceptoReciboEstadoNotificacion implements EnumClass<Integer> {

    NO_PROCEDE(1),
    NOTIFICADO(2),
    NO_NOTIFICADO(3);

    private Integer id;

    ConceptoReciboEstadoNotificacion(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static ConceptoReciboEstadoNotificacion fromId(Integer id) {
        for (ConceptoReciboEstadoNotificacion at : ConceptoReciboEstadoNotificacion.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}