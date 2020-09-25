package com.company.test1.entity.enums;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum DocumentoImputableTipoEnum implements EnumClass<String> {

    FACTURA("FP"),
    PRESUPUESTO("P");

    private String id;

    DocumentoImputableTipoEnum(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static DocumentoImputableTipoEnum fromId(String id) {
        for (DocumentoImputableTipoEnum at : DocumentoImputableTipoEnum.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}