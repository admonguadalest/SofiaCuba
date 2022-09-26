package com.company.test1.entity;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum TipoPuntoEntradaDocumentosEnum implements EnumClass<String> {

    MAIL("MAIL"),
    STORAGE("STORAGE"),
    CLIENTSTORAGE("CLIENTSTORAGE"),
    ROSSUM("ROSSUM");

    private String id;

    TipoPuntoEntradaDocumentosEnum(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static TipoPuntoEntradaDocumentosEnum fromId(String id) {
        for (TipoPuntoEntradaDocumentosEnum at : TipoPuntoEntradaDocumentosEnum.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}