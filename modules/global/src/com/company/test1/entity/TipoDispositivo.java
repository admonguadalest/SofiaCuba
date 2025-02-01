package com.company.test1.entity;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum TipoDispositivo implements EnumClass<String> {

    SIMCARD("SIMCARD"),
    PANEL_ALARMA("PANEL_ALARMA"),
    DETECTOR_PIR("DETECTOR_PIS"),
    ROUTER("ROUTER"),
    CAMARA("CAMARA");

    private String id;

    TipoDispositivo(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }



    @Nullable
    public static TipoDispositivo fromId(String id) {
        for (TipoDispositivo at : TipoDispositivo.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}