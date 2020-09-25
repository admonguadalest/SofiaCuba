package com.company.test1.entity.enums;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum OrdenTrabajoTipoPrivacidadEnum implements EnumClass<Integer> {

    PUBLICO(1),
    SOLO_ADMINISTRADORES(2),
    SOLO_PROPIO_CREADOR(3);

    private Integer id;

    OrdenTrabajoTipoPrivacidadEnum(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static OrdenTrabajoTipoPrivacidadEnum fromId(Integer id) {
        for (OrdenTrabajoTipoPrivacidadEnum at : OrdenTrabajoTipoPrivacidadEnum.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}