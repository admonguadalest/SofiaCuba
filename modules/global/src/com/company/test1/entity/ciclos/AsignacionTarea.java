package com.company.test1.entity.ciclos;

import com.company.test1.entity.extroles.Proveedor;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Table(name = "ASIGNACION_TAREA")
@Entity(name = "test1_AsignacionTarea")
public class AsignacionTarea extends StandardEntity {
    private static final long serialVersionUID = 7826019844932347192L;

    @NotNull(message = "Indicar Proveedor")
    @Lookup(type = LookupType.DROPDOWN, actions = "lookup")
    @OnDeleteInverse(DeletePolicy.DENY)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROVEEDOR_ID")
    protected Proveedor proveedor;

    @NotNull(message = "Toda Nota de Intervencion debe tener asignada una Orden de Trabajo")
    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDEN_TRABAJO_ID")
    protected OrdenTrabajo ordenTrabajo;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_PREVISTA")
    protected Date fechaPrevista; // la uso temporalmente hasta arreglar como fechaPrevistaFinalizacion

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_FINALIZACION")
    protected Date fechaFinalizacion;

    @NotNull(message = "Aportar Descripcion de la Asignacion")
    @Lob
    @Column(name = "DESCRIPCION")
    protected String descripcion;

    @Column(name = "CANCELADO")
    protected Boolean cancelado;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_PREVISTO_INICIO")
    protected Date fechaPrevistoInicio;

    @Column(name = "ACORDADO_CON_INDUSTRIAL")
    protected Boolean acordadoConIndustrial;

    @Lob
    @Column(name = "COMENTARIOS_INDUSTRIAL")
    protected String comentariosIndustrial;

    public void setFechaPrevista(Date fechaPrevista) {
        this.fechaPrevista = fechaPrevista;
    }

    public Date getFechaPrevista() {
        return fechaPrevista;
    }

    public void setFechaFinalizacion(Date fechaFinalizacion) {
        this.fechaFinalizacion = fechaFinalizacion;
    }

    public Date getFechaFinalizacion() {
        return fechaFinalizacion;
    }

    public void setFechaPrevistoInicio(Date fechaPrevistoInicio) {
        this.fechaPrevistoInicio = fechaPrevistoInicio;
    }

    public Date getFechaPrevistoInicio() {
        return fechaPrevistoInicio;
    }

    public String getComentariosIndustrial() {
        return comentariosIndustrial;
    }

    public void setComentariosIndustrial(String comentariosIndustrial) {
        this.comentariosIndustrial = comentariosIndustrial;
    }

    public Boolean getAcordadoConIndustrial() {
        return acordadoConIndustrial;
    }

    public void setAcordadoConIndustrial(Boolean acordadoConIndustrial) {
        this.acordadoConIndustrial = acordadoConIndustrial;
    }

    public Boolean getCancelado() {
        return cancelado;
    }

    public void setCancelado(Boolean cancelado) {
        this.cancelado = cancelado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public OrdenTrabajo getOrdenTrabajo() {
        return ordenTrabajo;
    }

    public void setOrdenTrabajo(OrdenTrabajo ordenTrabajo) {
        this.ordenTrabajo = ordenTrabajo;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }
}