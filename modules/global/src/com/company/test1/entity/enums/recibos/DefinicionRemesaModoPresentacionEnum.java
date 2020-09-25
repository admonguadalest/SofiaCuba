package com.company.test1.entity.enums.recibos;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum DefinicionRemesaModoPresentacionEnum implements EnumClass<Integer> {

    PROPIETARIO(1),
    DELEGADO(2);

    private Integer id;

    DefinicionRemesaModoPresentacionEnum(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static DefinicionRemesaModoPresentacionEnum fromId(Integer id) {
        for (DefinicionRemesaModoPresentacionEnum at : DefinicionRemesaModoPresentacionEnum.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}