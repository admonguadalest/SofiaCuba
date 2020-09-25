package com.company.test1.entity.ciclos;

import com.company.test1.entity.documentosfotograficos.CarpetaDocumentosFotograficos;
import com.company.test1.entity.enums.OrdenTrabajoTipoPrivacidadEnum;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;
import com.haulmont.cuba.security.entity.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Table(name = "ORDEN_TRABAJO")
@Entity(name = "test1_OrdenTrabajo")
public class OrdenTrabajo extends StandardEntity {
    private static final long serialVersionUID = -5567040945921599543L;

    @NotNull(message = "Las Ordenes de Trabajo deben tener asociada una entrada")
    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ENTRADA_ID")
    protected Entrada entrada;

    @Lookup(type = LookupType.DROPDOWN, actions = {})
    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.UNLINK)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CARPETA_DOCUMENTOS_FOTOGRAFICOS_ID")
    protected CarpetaDocumentosFotograficos carpetaDocumentosFotograficos;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_SOLICITUD")
    protected Date fechaSolicitud;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_PREVISTA_INTERVENCION")
    protected Date fechaPrevistaIntervencion;

    @Column(name = "COSTE_ORIENTATIVO", length = 100)
    protected String costeOrientativo;

    @NotNull(message = "Debe indicar el autor de la propuesta de la Orden de Trabajo")
    @Column(name = "PROPUESTO_POR")
    protected String propuestoPor;

    @NotNull(message = "{msg://test1_OrdenTrabajo.descripcion.validation.NotNull}")
    @Lob
    @Column(name = "DESCRIPCION")
    protected String descripcion;

    @Lob
    @Column(name = "OBSERVACION")
    protected String observacion;

    @Lob
    @Column(name = "OBSERVACION_INTERVENCION")
    protected String observacionIntervencion;

    @Column(name = "DURACION_PREVISTA_INTERVENCION", length = 100)
    protected String duracionPrevistaIntervencion;

    @Column(name = "FRANJA_HORARIA_INTERVENCION", length = 100)
    protected String franjaHorariaIntervencion;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_FINALIZACION")
    protected Date fechaFinalizacion;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_FINALIZACION_ESTIMADA")
    protected Date fechaFinalizacionEstimada;

    @Column(name = "EXCLUIR_DE_MONITORIZACION_ENCARGADO")
    protected Boolean excluirDeMonitorizacionEncargado;

    @Column(name = "TIPO_PRIVACIDAD")
    protected Integer tipoPrivacidad;

    @Lookup(type = LookupType.DROPDOWN, actions = "lookup")
    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ADMINISTRADOR_ID")
    protected User administrador;

    @Column(name = "REPARACION_REHABILITACION")
    protected Boolean reparacionRehabilitacion;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "ordenTrabajo")
    protected List<NotaIntervencion> notasIntervenciones = new ArrayList<NotaIntervencion>();

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "ordenTrabajo")
    protected List<AsignacionTarea> asignacionesTareas = new ArrayList<AsignacionTarea>();

    @Column(name = "RM2ID")
    protected Integer rm2id;

    public void setCosteOrientativo(String costeOrientativo) {
        this.costeOrientativo = costeOrientativo;
    }

    public String getCosteOrientativo() {
        return costeOrientativo;
    }

    public void setFechaSolicitud(Date fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public Date getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaPrevistaIntervencion(Date fechaPrevistaIntervencion) {
        this.fechaPrevistaIntervencion = fechaPrevistaIntervencion;
    }

    public Date getFechaPrevistaIntervencion() {
        return fechaPrevistaIntervencion;
    }

    public void setFechaFinalizacion(Date fechaFinalizacion) {
        this.fechaFinalizacion = fechaFinalizacion;
    }

    public Date getFechaFinalizacion() {
        return fechaFinalizacion;
    }

    public void setFechaFinalizacionEstimada(Date fechaFinalizacionEstimada) {
        this.fechaFinalizacionEstimada = fechaFinalizacionEstimada;
    }

    public Date getFechaFinalizacionEstimada() {
        return fechaFinalizacionEstimada;
    }

    public Integer getRm2id() {
        return rm2id;
    }

    public void setRm2id(Integer rm2id) {
        this.rm2id = rm2id;
    }

    public void setPropuestoPor(String propuestoPor) {
        this.propuestoPor = propuestoPor;
    }

    public String getPropuestoPor() {
        return propuestoPor;
    }

    public List<AsignacionTarea> getAsignacionesTareas() {
        return asignacionesTareas;
    }

    public void setAsignacionesTareas(List<AsignacionTarea> asignacionesTareas) {
        this.asignacionesTareas = asignacionesTareas;
    }

    public List<NotaIntervencion> getNotasIntervenciones() {
        return notasIntervenciones;
    }

    public void setNotasIntervenciones(List<NotaIntervencion> notasIntervenciones) {
        this.notasIntervenciones = notasIntervenciones;
    }

    public Boolean getReparacionRehabilitacion() {
        return reparacionRehabilitacion;
    }

    public void setReparacionRehabilitacion(Boolean reparacionRehabilitacion) {
        this.reparacionRehabilitacion = reparacionRehabilitacion;
    }

    public void setTipoPrivacidad(OrdenTrabajoTipoPrivacidadEnum tipoPrivacidad) {
        this.tipoPrivacidad = tipoPrivacidad == null ? null : tipoPrivacidad.getId();
    }

    public OrdenTrabajoTipoPrivacidadEnum getTipoPrivacidad() {
        return tipoPrivacidad == null ? null : OrdenTrabajoTipoPrivacidadEnum.fromId(tipoPrivacidad);
    }

    public User getAdministrador() {
        return administrador;
    }

    public void setAdministrador(User administrador) {
        this.administrador = administrador;
    }

    public Boolean getExcluirDeMonitorizacionEncargado() {
        return excluirDeMonitorizacionEncargado;
    }

    public void setExcluirDeMonitorizacionEncargado(Boolean excluirDeMonitorizacionEncargado) {
        this.excluirDeMonitorizacionEncargado = excluirDeMonitorizacionEncargado;
    }

    public String getFranjaHorariaIntervencion() {
        return franjaHorariaIntervencion;
    }

    public void setFranjaHorariaIntervencion(String franjaHorariaIntervencion) {
        this.franjaHorariaIntervencion = franjaHorariaIntervencion;
    }

    public String getDuracionPrevistaIntervencion() {
        return duracionPrevistaIntervencion;
    }

    public void setDuracionPrevistaIntervencion(String duracionPrevistaIntervencion) {
        this.duracionPrevistaIntervencion = duracionPrevistaIntervencion;
    }

    public String getObservacionIntervencion() {
        return observacionIntervencion;
    }

    public void setObservacionIntervencion(String observacionIntervencion) {
        this.observacionIntervencion = observacionIntervencion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public CarpetaDocumentosFotograficos getCarpetaDocumentosFotograficos() {
        return carpetaDocumentosFotograficos;
    }

    public void setCarpetaDocumentosFotograficos(CarpetaDocumentosFotograficos carpetaDocumentosFotograficos) {
        this.carpetaDocumentosFotograficos = carpetaDocumentosFotograficos;
    }

    public Entrada getEntrada() {
        return entrada;
    }

    public void setEntrada(Entrada entrada) {
        this.entrada = entrada;
    }
}