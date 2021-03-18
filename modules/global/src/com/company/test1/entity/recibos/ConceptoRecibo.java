package com.company.test1.entity.recibos;

import com.company.test1.entity.enums.recibos.ConceptoReciboEstadoNotificacion;
import com.company.test1.entity.enums.recibos.ConceptoReciboVigenciaEnum;
import com.company.test1.entity.incrementos.Incremento;
import com.company.test1.entity.recibos.accesorios.OrdenacionConceptoReciboComparator;
import com.company.test1.validations.contratos.ConceptoReciboBean;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;
import com.haulmont.cuba.core.global.validation.groups.UiCrossFieldChecks;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Table(name = "CONCEPTO_RECIBO")
@Entity(name = "test1_ConceptoRecibo")
@ConceptoReciboBean(groups= UiCrossFieldChecks.class)
public class ConceptoRecibo extends StandardEntity {
    private static final long serialVersionUID = -2414263241907384168L;

    public static OrdenacionConceptoReciboComparator ordenacionConceptoReciboComparator = new OrdenacionConceptoReciboComparator();

    @NotNull(message = "Indicar Concepto")
    @OnDeleteInverse(DeletePolicy.DENY)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONCEPTO_ID")
    protected Concepto concepto;

    @Column(name = "RM2ID")
    protected Integer rm2id;

    @NotNull(message = "Indicar Importe")
    @Column(name = "IMPORTE")
    protected Double importe;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROGRAMACION_RECIBO_ID")
    protected ProgramacionRecibo programacionRecibo;

    @NotNull(message = "Especificar Tipo de Vigencia")
    @Column(name = "VIGENCIA")
    protected Integer vigencia;

    @Column(name = "ACTIVADO_DESACTIVADO")
    protected Boolean activadoDesactivado = false;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_DESDE")
    protected Date fechaDesde;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_HASTA")
    protected Date fechaHasta;

    @Column(name = "NUM_EMISIONES")
    protected Integer numEmisiones;

    @Column(name = "ES_MODIFICACION_AGREGADA")
    protected Boolean esModificacionAgregada;

    @NotNull(message = "Indicar FechaValor")
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_VALOR")

     protected Date fechaValor;

    @Column(name = "DESCRIPCION_CAUSA")
    protected String descripcionCausa;

    @NotNull(message = "Indicar Estado de Notificacion")
    @Column(name = "ESTADO_NOTIFICACION")
    protected Integer estadoNotificacion;

    @Column(name = "ACTUALIZABLE_IPC")
    protected Boolean actualizableIPC;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "INCREMENTO_ID")
    protected Incremento incremento;

    @Column(name = "OMITIR_EN_PRORRATEO")
    protected Boolean omitirEnProrrateo = false;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "conceptoRecibo")
    protected List<ConceptoAdicionalConceptoRecibo> conceptosAdicionalesConceptoRecibo = new ArrayList<ConceptoAdicionalConceptoRecibo>();

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaValor(Date fechaValor) {
        this.fechaValor = fechaValor;
    }

    public Date getFechaValor() {
        return fechaValor;
    }

    public Integer getRm2id() {
        return rm2id;
    }

    public void setRm2id(Integer rm2id) {
        this.rm2id = rm2id;
    }

    public void setConceptosAdicionalesConceptoRecibo(List<ConceptoAdicionalConceptoRecibo> conceptosAdicionalesConceptoRecibo) {
        this.conceptosAdicionalesConceptoRecibo = conceptosAdicionalesConceptoRecibo;
    }

    public List<ConceptoAdicionalConceptoRecibo> getConceptosAdicionalesConceptoRecibo() {
        return conceptosAdicionalesConceptoRecibo;
    }

    public Boolean getOmitirEnProrrateo() {
        return omitirEnProrrateo;
    }

    public void setOmitirEnProrrateo(Boolean omitirEnProrrateo) {
        this.omitirEnProrrateo = omitirEnProrrateo;
    }

    public Incremento getIncremento() {
        return incremento;
    }

    public void setIncremento(Incremento incremento) {
        this.incremento = incremento;
    }

    public Boolean getActualizableIPC() {
        return actualizableIPC;
    }

    public void setActualizableIPC(Boolean actualizableIPC) {
        this.actualizableIPC = actualizableIPC;
    }

    public void setEstadoNotificacion(ConceptoReciboEstadoNotificacion estadoNotificacion) {
        this.estadoNotificacion = estadoNotificacion == null ? null : estadoNotificacion.getId();
    }

    public ConceptoReciboEstadoNotificacion getEstadoNotificacion() {
        return estadoNotificacion == null ? null : ConceptoReciboEstadoNotificacion.fromId(estadoNotificacion);
    }

    public String getDescripcionCausa() {
        return descripcionCausa;
    }

    public void setDescripcionCausa(String descripcionCausa) {
        this.descripcionCausa = descripcionCausa;
    }

    public Boolean getEsModificacionAgregada() {
        return esModificacionAgregada;
    }

    public void setEsModificacionAgregada(Boolean esModificacionAgregada) {
        this.esModificacionAgregada = esModificacionAgregada;
    }

    public Integer getNumEmisiones() {
        return numEmisiones;
    }

    public void setNumEmisiones(Integer numEmisiones) {
        this.numEmisiones = numEmisiones;
    }

    public Boolean getActivadoDesactivado() {
        return activadoDesactivado;
    }

    public void setActivadoDesactivado(Boolean activadoDesactivado) {
        this.activadoDesactivado = activadoDesactivado;
    }

    public ConceptoReciboVigenciaEnum getVigencia() {
        return vigencia == null ? null : ConceptoReciboVigenciaEnum.fromId(vigencia);
    }

    public void setVigencia(ConceptoReciboVigenciaEnum vigencia) {
        this.vigencia = vigencia == null ? null : vigencia.getId();
    }

    public ProgramacionRecibo getProgramacionRecibo() {
        return programacionRecibo;
    }

    public void setProgramacionRecibo(ProgramacionRecibo programacionRecibo) {
        this.programacionRecibo = programacionRecibo;
    }

    public Double getImporte() {
        return importe;
    }

    public void setImporte(Double importe) {
        this.importe = importe;
    }

    public Concepto getConcepto() {
        return concepto;
    }

    public void setConcepto(Concepto concepto) {
        this.concepto = concepto;
    }
}