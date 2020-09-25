package com.company.test1.entity.enums;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum EstadoContratoInquilinoEnum implements EnumClass<Integer> {

    REDACCION(0),
    EVALUACION(1),
    AUTORIZADO(2),
    VIGENTE(3),
    CANCELADO(4),
    RENUNCIADO(5),
    DESHAUCIADO(6),
    LIQUIDADO(7);

    private Integer id;

    EstadoContratoInquilinoEnum(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static EstadoContratoInquilinoEnum fromId(Integer id) {
        for (EstadoContratoInquilinoEnum at : EstadoContratoInquilinoEnum.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }

    public static int compare(EstadoContratoInquilinoEnum e1, EstadoContratoInquilinoEnum e2){
        return e1.id.compareTo(e2.id);
    }
}