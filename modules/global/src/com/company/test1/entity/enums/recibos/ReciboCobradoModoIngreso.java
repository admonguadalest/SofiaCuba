package com.company.test1.entity.enums.recibos;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum ReciboCobradoModoIngreso implements EnumClass<Integer> {

    BANCARIO(1),
    ADMINISTRACION(2),
    DEVUELTO(3),
    PENDIENTE(4),
    INGRESO_TALON(5),
    COMPENSACION_ABONO_RECIBO(6);

    private Integer id;

    ReciboCobradoModoIngreso(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static ReciboCobradoModoIngreso fromId(Integer id) {
        for (ReciboCobradoModoIngreso at : ReciboCobradoModoIngreso.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}