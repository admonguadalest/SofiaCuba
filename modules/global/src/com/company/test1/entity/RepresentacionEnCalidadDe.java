package com.company.test1.entity;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum RepresentacionEnCalidadDe implements EnumClass<String> {

    ADMINISTRADOR("Administrador"),
    INTERVENTOR("Interventor"),
    APODERADO("Apoderado"),
    REPRESENTANTE("Representante"),
    ADMINISTRADOR_SOLIDARIO("Administrador Solidario");

    private String id;

    RepresentacionEnCalidadDe(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static RepresentacionEnCalidadDe fromId(String id) {
        for (RepresentacionEnCalidadDe at : RepresentacionEnCalidadDe.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}