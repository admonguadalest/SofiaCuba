package com.company.test1.entity.enums.conceptosadicionales;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum ConceptosAdicionalesIVA implements EnumClass<Integer> {

    IVA4(4),
    IVA10(10),
    IVA21(21),
    IVA33(33);

    private Integer id;

    ConceptosAdicionalesIVA(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static ConceptosAdicionalesIVA fromId(Integer id) {
        for (ConceptosAdicionalesIVA at : ConceptosAdicionalesIVA.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}