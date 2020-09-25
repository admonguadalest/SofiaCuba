package com.company.test1.entity.enums;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum PersonaEnum implements EnumClass<Integer> {


    PERSONA_FISICA(1),
    PERSONA_JURIDICA(2);

    private Integer id;

    PersonaEnum(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static PersonaEnum fromId(Integer id) {
        for (PersonaEnum at : PersonaEnum.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}