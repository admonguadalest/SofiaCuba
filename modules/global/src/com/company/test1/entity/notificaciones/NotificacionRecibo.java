package com.company.test1.entity.notificaciones;

import com.company.test1.entity.conceptosadicionales.ProgramacionConceptoAdicional;
import com.company.test1.entity.recibos.ConceptoRecibo;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import java.util.Date;

@Table(name = "NOTIFICACION_RECIBO")
@Entity(name = "test1_NotificacionRecibo")
public class NotificacionRecibo extends StandardEntity {
    private static final long serialVersionUID = 2951019620667448099L;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_DESDE")
    protected Date fechaDesde;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_HASTA")
    protected Date fechaHasta;

    @Lob
    @Column(name = "CONTENIDO")
    protected String contenido;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROGRAMACION_RECIBO_ID")
    protected ProgramacionConceptoAdicional programacionRecibo;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONCEPTO_RECIBO_ID")
    protected ConceptoRecibo conceptoRecibo;

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

    public ConceptoRecibo getConceptoRecibo() {
        return conceptoRecibo;
    }

    public void setConceptoRecibo(ConceptoRecibo conceptoRecibo) {
        this.conceptoRecibo = conceptoRecibo;
    }

    public ProgramacionConceptoAdicional getProgramacionRecibo() {
        return programacionRecibo;
    }

    public void setProgramacionRecibo(ProgramacionConceptoAdicional programacionRecibo) {
        this.programacionRecibo = programacionRecibo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

}