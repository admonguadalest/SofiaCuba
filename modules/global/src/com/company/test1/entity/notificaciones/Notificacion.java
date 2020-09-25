package com.company.test1.entity.notificaciones;

import com.company.test1.entity.reportsyplantillas.Plantilla;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import java.util.Date;

@Table(name = "NOTIFICACION")
@Entity(name = "test1_Notificacion")
public class Notificacion extends StandardEntity {
    private static final long serialVersionUID = -6530628713619233423L;

    @Lob
    @Column(name = "CONTENIDO_IMPLEMENTADO")
    protected String contenidoImplementado;

    @Column(name = "ENVIADO")
    protected Boolean enviado;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_REALIZACION")
    protected Date fechaRealizacion;

    @Column(name = "IMPLEMENTADO")
    protected Boolean implementado;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PLANTILLA_ID")
    protected Plantilla plantilla;

    @Column(name = "VERSION_PDF")
    protected byte[] versionPdf;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_PROGRAMADA_ENVIO")
    protected Date fechaProgramadaEnvio;

    @Column(name = "TITULO")
    protected String titulo;

    public void setFechaRealizacion(Date fechaRealizacion) {
        this.fechaRealizacion = fechaRealizacion;
    }

    public Date getFechaRealizacion() {
        return fechaRealizacion;
    }

    public void setFechaProgramadaEnvio(Date fechaProgramadaEnvio) {
        this.fechaProgramadaEnvio = fechaProgramadaEnvio;
    }

    public Date getFechaProgramadaEnvio() {
        return fechaProgramadaEnvio;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public byte[] getVersionPdf() {
        return versionPdf;
    }

    public void setVersionPdf(byte[] versionPdf) {
        this.versionPdf = versionPdf;
    }

    public Plantilla getPlantilla() {
        return plantilla;
    }

    public void setPlantilla(Plantilla plantilla) {
        this.plantilla = plantilla;
    }

    public Boolean getImplementado() {
        return implementado;
    }

    public void setImplementado(Boolean implementado) {
        this.implementado = implementado;
    }

    public Boolean getEnviado() {
        return enviado;
    }

    public void setEnviado(Boolean enviado) {
        this.enviado = enviado;
    }

    public String getContenidoImplementado() {
        return contenidoImplementado;
    }

    public void setContenidoImplementado(String contenidoImplementado) {
        this.contenidoImplementado = contenidoImplementado;
    }
}