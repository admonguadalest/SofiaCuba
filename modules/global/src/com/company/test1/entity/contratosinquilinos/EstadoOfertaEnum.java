package com.company.test1.entity.contratosinquilinos;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum EstadoOfertaEnum implements EnumClass<String> {

    COMERCIALIZACION("Comercialización"),
    RESERVADA("Reservada"),
    CONFIRMADA("Confirmada"),
    ALQUILADA("Alquilada"),
    CANCELADA("Cancelada");

    private String id;

    EstadoOfertaEnum(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static EstadoOfertaEnum fromId(String id) {
        for (EstadoOfertaEnum at : EstadoOfertaEnum.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}