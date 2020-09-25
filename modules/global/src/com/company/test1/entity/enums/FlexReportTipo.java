package com.company.test1.entity.enums;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum FlexReportTipo implements EnumClass<String> {

    GRAFICO("GRAFICO"),
    TABULAR("TABULAR"),
    PLANTILLA("PLANTILLA");

    private String id;

    FlexReportTipo(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static FlexReportTipo fromId(String id) {
        for (FlexReportTipo at : FlexReportTipo.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}