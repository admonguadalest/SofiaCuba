package com.company.test1.entity.enums.conceptosadicionales;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum ConceptosAdicionalesIRPF implements EnumClass<Integer> {

    IRPF1(1),
    IRPF2(2),
    IRPF15(15);

    private Integer id;

    ConceptosAdicionalesIRPF(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static ConceptosAdicionalesIRPF fromId(Integer id) {
        for (ConceptosAdicionalesIRPF at : ConceptosAdicionalesIRPF.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}