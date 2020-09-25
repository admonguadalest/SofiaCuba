package com.company.test1.entity.ciclos;

import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Table(name = "NOTA_INTERVENCION")
@Entity(name = "test1_NotaIntervencion")
public class NotaIntervencion extends StandardEntity {
    private static final long serialVersionUID = 4770255532585244877L;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_PREVISTA_INTERVENCION")
    protected Date fechaPrevistaIntervencion;

    @NotNull(message = "Aportar contenido de nota de intervencion")
    @Lob
    @Column(name = "CONTENIDO")
    protected String contenido;

    @Column(name = "FRANJA_HORARIA", length = 100)
    protected String franjaHoraria;

    @NotNull(message = "Indicar Fecha de Nota de Intervencion")
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_INTERVENCION")
    protected Date fechaIntervencion;

    @Column(name = "GRADO_EXITO_INTERVENCION")
    protected Integer gradoExitoIntervencion;

    @NotNull(message = "Las Notas de Intervencion deben tener asociada una Orden de Trabajo")
    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDEN_TRABAJO_ID")
    protected OrdenTrabajo ordenTrabajo;

    @Column(name = "ENVIO_NOTIFICACION_INQUILINO")
    protected Boolean envioNotificacionInquilino;

    @Column(name = "ENVIO_NOTIFICACION_PROPIETARIO")
    protected Boolean envioNotificacionPropietario;

    @Column(name = "AMPLIACION_NOTIFICACION_INQUILINO")
    protected String ampliacionNotificacionInquilino;

    @Column(name = "AMPLIACION_NOTIFICACION_PROPIETARIO")
    protected String ampliacionNotificacionPropietario;

    @Column(name = "ENVIADO_A_INQUILINO")
    protected Boolean enviadoAInquilino;

    @Column(name = "ENVIADO_A_PROPIETARIO")
    protected Boolean enviadoAPropietario;

    @Column(name = "COORDINAR_INTERVENCION")
    protected Boolean coordinarIntervencion;

    public void setFechaPrevistaIntervencion(Date fechaPrevistaIntervencion) {
        this.fechaPrevistaIntervencion = fechaPrevistaIntervencion;
    }

    public Date getFechaPrevistaIntervencion() {
        return fechaPrevistaIntervencion;
    }

    public void setFechaIntervencion(Date fechaIntervencion) {
        this.fechaIntervencion = fechaIntervencion;
    }

    public Date getFechaIntervencion() {
        return fechaIntervencion;
    }

    public String getAmpliacionNotificacionPropietario() {
        return ampliacionNotificacionPropietario;
    }

    public void setAmpliacionNotificacionPropietario(String ampliacionNotificacionPropietario) {
        this.ampliacionNotificacionPropietario = ampliacionNotificacionPropietario;
    }

    public Boolean getCoordinarIntervencion() {
        return coordinarIntervencion;
    }

    public void setCoordinarIntervencion(Boolean coordinarIntervencion) {
        this.coordinarIntervencion = coordinarIntervencion;
    }

    public Boolean getEnviadoAPropietario() {
        return enviadoAPropietario;
    }

    public void setEnviadoAPropietario(Boolean enviadoAPropietario) {
        this.enviadoAPropietario = enviadoAPropietario;
    }

    public Boolean getEnviadoAInquilino() {
        return enviadoAInquilino;
    }

    public void setEnviadoAInquilino(Boolean enviadoAInquilino) {
        this.enviadoAInquilino = enviadoAInquilino;
    }

    public String getAmpliacionNotificacionInquilino() {
        return ampliacionNotificacionInquilino;
    }

    public void setAmpliacionNotificacionInquilino(String ampliacionNotificacionInquilino) {
        this.ampliacionNotificacionInquilino = ampliacionNotificacionInquilino;
    }

    public Boolean getEnvioNotificacionPropietario() {
        return envioNotificacionPropietario;
    }

    public void setEnvioNotificacionPropietario(Boolean envioNotificacionPropietario) {
        this.envioNotificacionPropietario = envioNotificacionPropietario;
    }

    public Boolean getEnvioNotificacionInquilino() {
        return envioNotificacionInquilino;
    }

    public void setEnvioNotificacionInquilino(Boolean envioNotificacionInquilino) {
        this.envioNotificacionInquilino = envioNotificacionInquilino;
    }

    public OrdenTrabajo getOrdenTrabajo() {
        return ordenTrabajo;
    }

    public void setOrdenTrabajo(OrdenTrabajo ordenTrabajo) {
        this.ordenTrabajo = ordenTrabajo;
    }

    public Integer getGradoExitoIntervencion() {
        return gradoExitoIntervencion;
    }

    public void setGradoExitoIntervencion(Integer gradoExitoIntervencion) {
        this.gradoExitoIntervencion = gradoExitoIntervencion;
    }

    public String getFranjaHoraria() {
        return franjaHoraria;
    }

    public void setFranjaHoraria(String franjaHoraria) {
        this.franjaHoraria = franjaHoraria;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

}