package com.company.test1.entity.enums;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum ModoPagoProveedor implements EnumClass<String> {

    TALON("TALON"),
    PAGO_TELEMATICO("PAGO TELEMATICO"),
    PAGO_DOMICILIADO("PAGO DOMICILIADO");

    private String id;

    ModoPagoProveedor(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static ModoPagoProveedor fromId(String id) {
        for (ModoPagoProveedor at : ModoPagoProveedor.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}