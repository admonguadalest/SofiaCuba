package com.company.test1.entity.enums;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum IncrementoGeneralObrasModoReparticion implements EnumClass<Integer> {

    MODO_REPARTO_TOTAL_REPARTIDO_COEFS_TODOS_DEPTOS(0),
    MODO_REPARTO_TOTAL_PONDERADO_DEPTOS_SELECCIONADOS(1),
    MODO_REPARTO_TODO_EN_CADA_DEPARTAMENTO(2);

    private Integer id;

    IncrementoGeneralObrasModoReparticion(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static IncrementoGeneralObrasModoReparticion fromId(Integer id) {
        for (IncrementoGeneralObrasModoReparticion at : IncrementoGeneralObrasModoReparticion.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}