package com.company.test1.entity.ciclos;

import com.company.test1.validations.ciclos.EntradaBean;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;
import com.haulmont.cuba.core.global.validation.groups.UiCrossFieldChecks;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Table(name = "ENTRADA")
@Entity(name = "test1_Entrada")
@EntradaBean(groups= UiCrossFieldChecks.class)
public class Entrada extends StandardEntity {
    private static final long serialVersionUID = 5786499215915209355L;

    @NotNull(message = "Aportar Contenido de Entrada")
    @Lob
    @Column(name = "CONTENIDO_ENTRADA")
    protected String contenidoEntrada;

    @Column(name = "RM2ID")
    protected Integer rm2id;

    @NotNull(message = "Indicar Fecha de Entrada")
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_ENTRADA")
    protected Date fechaEntrada;

    @NotNull(message = "Las Entradas han de tener asociado un ciclo")
    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CICLO_ID")
    protected Ciclo ciclo;

    @Lookup(type = LookupType.DROPDOWN, actions = "lookup")
    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EVENTO_ID")
    protected Evento evento;

    @Column(name = "ENTERO_RECORDATORIO")
    protected Integer enteroRecordatorio;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "entrada")
    protected OrdenTrabajo ordenTrabajo;

    public void setFechaEntrada(Date fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public Date getFechaEntrada() {
        return fechaEntrada;
    }

    public Integer getRm2id() {
        return rm2id;
    }

    public void setRm2id(Integer rm2id) {
        this.rm2id = rm2id;
    }

    public OrdenTrabajo getOrdenTrabajo() {
        return ordenTrabajo;
    }

    public void setOrdenTrabajo(OrdenTrabajo ordenTrabajo) {
        this.ordenTrabajo = ordenTrabajo;
    }

    public Integer getEnteroRecordatorio() {
        return enteroRecordatorio;
    }

    public void setEnteroRecordatorio(Integer enteroRecordatorio) {
        this.enteroRecordatorio = enteroRecordatorio;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public Ciclo getCiclo() {
        return ciclo;
    }

    public void setCiclo(Ciclo ciclo) {
        this.ciclo = ciclo;
    }

    public String getContenidoEntrada() {
        return contenidoEntrada;
    }

    public void setContenidoEntrada(String contenidoEntrada) {
        this.contenidoEntrada = contenidoEntrada;
    }
}