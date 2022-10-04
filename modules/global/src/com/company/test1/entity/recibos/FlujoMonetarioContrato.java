package com.company.test1.entity.recibos;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.BaseUuidEntity;

import java.util.Date;

@MetaClass(name = "test1_FlujoMonetarioContrato")
public class FlujoMonetarioContrato extends BaseUuidEntity {
    private static final long serialVersionUID = 2370935465606225206L;

    @MetaProperty
    private Date fechaMovimiento = null;

    @MetaProperty
    private String informacionRecibo;

    @MetaProperty
    private Double saldoAcumulado;

    @MetaProperty
    private Double nominalRecibo = 0.0;

    @MetaProperty
    private Double importeHaber = 0.0;

    @MetaProperty
    private Double importeDebe = 0.0;

    @MetaProperty
    private String concepto = "";

    @MetaProperty
    private String informacionRemesa = "";

    public String getInformacionRemesa(){return informacionRemesa;}

    public void setInformacionRemesa(String informacionRemesa) {this.informacionRemesa = informacionRemesa;}

    public Double getNominalRecibo() {
        return nominalRecibo;
    }

    public void setNominalRecibo(Double nominalRecibo) {
        this.nominalRecibo = nominalRecibo;
    }

    public Double getSaldoAcumulado() {
        return saldoAcumulado;
    }

    public void setSaldoAcumulado(Double saldoAcumulado) {
        this.saldoAcumulado = saldoAcumulado;
    }

    public String getInformacionRecibo() {
        return informacionRecibo;
    }

    public void setInformacionRecibo(String informacionRecibo) {
        this.informacionRecibo = informacionRecibo;
    }


    public void setFechaMovimiento(Date fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }

    public Date getFechaMovimiento() {
        return fechaMovimiento;
    }

    public void setImporteHaber(Double importeHaber) {
        this.importeHaber = importeHaber;
    }

    public Double getImporteHaber() {
        return importeHaber;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public Double getImporteDebe() {
        return importeDebe;
    }

    public void setImporteDebe(Double importeDebe) {
        this.importeDebe = importeDebe;
    }

}