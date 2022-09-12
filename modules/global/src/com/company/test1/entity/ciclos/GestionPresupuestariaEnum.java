package com.company.test1.entity.ciclos;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum GestionPresupuestariaEnum implements EnumClass<String> {

    SIN_PRESUPUESTO("EJECUCION SIN PRESUPUESTO"),
    PRESUPUESTO_NO_ENCARGADO("PRESUPUESTO NO ENCARGADO"),
    PRESUPUESTO_PENDIENTE_RECEPCION("PRESUPUESTO PENDIENTE RECEPCION"),
    PRESUPUESTO_PENDIENTE_ACEPTACION("PRESUPUESTO PENDIENTE ACEPTACION"),
    PRESUPUESTO_APROBADO("PRESUPUESTO APROBADO");

    private String id;

    GestionPresupuestariaEnum(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static GestionPresupuestariaEnum fromId(String id) {
        for (GestionPresupuestariaEnum at : GestionPresupuestariaEnum.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}