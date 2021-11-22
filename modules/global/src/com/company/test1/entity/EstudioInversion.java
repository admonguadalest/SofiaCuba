package com.company.test1.entity;

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Table(name = "TEST1_INVERSION")
@Entity(name = "test1_Inversion")
public class EstudioInversion extends StandardEntity {
    private static final long serialVersionUID = -8447567232059589404L;

    @Column(name = "TITULO")
    private String titulo;

    @Column(name = "TIPO")
    private String tipo;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_ENTRADA", nullable = false)
    @NotNull
    private Date fechaEntrada;

    @Column(name = "DIRECCION1")
    private String direccion1;

    @Column(name = "DIRECCION2")
    private String direccion2;

    @Column(name = "LINK_MAPS")
    private String linkMaps;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "MEDIADOR_ID")
    @NotNull
    private Persona mediador;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COLECCION_ARCHIVOS_ADJUNTOS_ID")
    private ColeccionArchivosAdjuntos coleccionArchivosAdjuntos;

    @Lob
    @Column(name = "EXPOSICION")
    private String exposicion;

    public Date getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(Date fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setMediador(Persona mediador) {
        this.mediador = mediador;
    }

    public Persona getMediador() {
        return mediador;
    }

    public String getExposicion() {
        return exposicion;
    }

    public void setExposicion(String exposicion) {
        this.exposicion = exposicion;
    }

    public ColeccionArchivosAdjuntos getColeccionArchivosAdjuntos() {
        return coleccionArchivosAdjuntos;
    }

    public void setColeccionArchivosAdjuntos(ColeccionArchivosAdjuntos coleccionArchivosAdjuntos) {
        this.coleccionArchivosAdjuntos = coleccionArchivosAdjuntos;
    }

    public String getLinkMaps() {
        return linkMaps;
    }

    public void setLinkMaps(String linkMaps) {
        this.linkMaps = linkMaps;
    }

    public String getDireccion2() {
        return direccion2;
    }

    public void setDireccion2(String direccion2) {
        this.direccion2 = direccion2;
    }

    public String getDireccion1() {
        return direccion1;
    }

    public void setDireccion1(String direccion1) {
        this.direccion1 = direccion1;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}