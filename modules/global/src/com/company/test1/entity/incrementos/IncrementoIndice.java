package com.company.test1.entity.incrementos;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@DiscriminatorValue("IIND")
@Entity(name = "test1_IncrementoIndice")
public class IncrementoIndice extends Incremento {
    private static final long serialVersionUID = 4440709945692654728L;

    @Column(name = "NOMBRE_TIPO")
    protected String nombreTipo;

    @Column(name = "MES_ANNO_INDICE")
    protected String mesAnnoIndice;

    @Column(name = "VALOR_INDICE")
    protected Double valorIndice;

    @Column(name = "INDICE_ANTERIOR")
    protected Double indiceAnterior;

    @Column(name = "VALOR_BASE")
    protected Double valorBase;

    @Column(name = "VALOR_INDICE_PORCENTUAL")
    protected Double valorIndicePorcentual;

    public Double getValorIndicePorcentual() {
        return valorIndicePorcentual;
    }

    public void setValorIndicePorcentual(Double valorIndicePorcentual) {
        this.valorIndicePorcentual = valorIndicePorcentual;
    }

    public Double getValorBase() {
        return valorBase;
    }

    public void setValorBase(Double valorBase) {
        this.valorBase = valorBase;
    }

    public Double getIndiceAnterior() {
        return indiceAnterior;
    }

    public void setIndiceAnterior(Double indiceAnterior) {
        this.indiceAnterior = indiceAnterior;
    }

    public Double getValorIndice() {
        return valorIndice;
    }

    public void setValorIndice(Double valorIndice) {
        this.valorIndice = valorIndice;
    }

    public String getMesAnnoIndice() {
        return mesAnnoIndice;
    }

    public void setMesAnnoIndice(String mesAnnoIndice) {
        this.mesAnnoIndice = mesAnnoIndice;
    }

    public String getNombreTipo() {
        return nombreTipo;
    }

    public void setNombreTipo(String nombreTipo) {
        this.nombreTipo = nombreTipo;
    }
}