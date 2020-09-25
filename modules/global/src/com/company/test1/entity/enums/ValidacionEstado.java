package com.company.test1.entity.enums;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum ValidacionEstado implements EnumClass<Integer> {

    PENDIENTE(1),
    PARCIALMENTE_VALIDADO(2),
    VALIDADO(3),
    RECHAZADO(4);

    private Integer id;

    ValidacionEstado(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static ValidacionEstado fromId(Integer id) {
        for (ValidacionEstado at : ValidacionEstado.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}