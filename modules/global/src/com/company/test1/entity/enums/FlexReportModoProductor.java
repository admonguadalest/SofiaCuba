package com.company.test1.entity.enums;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum FlexReportModoProductor implements EnumClass<String> {

    ENCAPSULADO("ENCAPSULADO"),
    PRIMITIVAS("PRIMITIVAS");

    private String id;

    FlexReportModoProductor(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static FlexReportModoProductor fromId(String id) {
        for (FlexReportModoProductor at : FlexReportModoProductor.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}