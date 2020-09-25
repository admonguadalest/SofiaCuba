package com.company.test1.entity.enums;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum TipoPlantillaEnum implements EnumClass<String> {

    AUMENTOS("AUMENTOS"),
    AUMENTOS_IPC("AUMENTOS_IPC"),
    RBOS_PENDIENTES("RBOS_PENDIENTES"),
    GENERICA("GENERICA"),
    APROBACION_PPTO("APROBACION PRESUPUESTO"),
    APROBACION_PPTO_ENCARGADO("APROBACION PRESUPUESTO ENCARGADO"),
    APROBACION_FRA("APBROBACION FACTURA");

    private String id;

    TipoPlantillaEnum(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static TipoPlantillaEnum fromId(String id) {
        for (TipoPlantillaEnum at : TipoPlantillaEnum.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}