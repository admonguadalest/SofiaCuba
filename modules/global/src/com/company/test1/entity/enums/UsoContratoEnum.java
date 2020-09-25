package com.company.test1.entity.enums;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum UsoContratoEnum implements EnumClass<Integer> {

    VIVIENDA(5),
    LOCAL(1),
    ESTUDIO(2);

    private Integer id;

    UsoContratoEnum(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static UsoContratoEnum fromId(Integer id) {
        for (UsoContratoEnum at : UsoContratoEnum.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}