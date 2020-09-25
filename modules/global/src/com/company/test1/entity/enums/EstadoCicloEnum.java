package com.company.test1.entity.enums;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum EstadoCicloEnum implements EnumClass<Integer> {

    ACTIVO(1),
    FINALIZADO(3);

    private Integer id;

    EstadoCicloEnum(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static EstadoCicloEnum fromId(Integer id) {
        for (EstadoCicloEnum at : EstadoCicloEnum.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}