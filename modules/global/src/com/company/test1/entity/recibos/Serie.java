package com.company.test1.entity.recibos;

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;

@Table(name = "SERIE")
@Entity(name = "test1_Serie")
public class Serie extends StandardEntity {
    private static final long serialVersionUID = -813657445256302483L;

    @Column(name = "NOMBRE_SERIE", length = 100)
    protected String nombreSerie;

    @Column(name = "RM2ID")
    protected Integer rm2id;

    @Column(name = "DESCRIPCION")
    protected String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MASTER_SERIE_ID")
    protected Serie masterSerie;

    public Serie getMasterSerie() {
        return masterSerie;
    }

    public void setMasterSerie(Serie masterSerie) {
        this.masterSerie = masterSerie;
    }

    public Integer getRm2id() {
        return rm2id;
    }

    public void setRm2id(Integer rm2id) {
        this.rm2id = rm2id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombreSerie() {
        return nombreSerie;
    }

    public void setNombreSerie(String nombreSerie) {
        this.nombreSerie = nombreSerie;
    }
}