package com.company.test1.entity;

import com.company.test1.entity.departamentos.Departamento;
import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.BaseUuidEntity;

@MetaClass(name = "test1_ItemNoEmitidoOAnomalo")
public class ItemNoEmitidoOAnomalo extends BaseUuidEntity {
    private static final long serialVersionUID = -2453267199508482082L;

    @MetaProperty
    private Departamento departamento;

    @MetaProperty
    private String informacionReportada;

    public String getInformacionReportada() {
        return informacionReportada;
    }

    public void setInformacionReportada(String informacionReportada) {
        this.informacionReportada = informacionReportada;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }
}