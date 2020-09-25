package com.company.test1.entity.ordenescobro;

import com.company.test1.entity.recibos.Recibo;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import java.util.Date;

@Table(name = "TEST1_ORDEN_COBRO")
@Entity(name = "test1_OrdenCobro")
public class OrdenCobro extends StandardEntity {
    private static final long serialVersionUID = 8690538627813790835L;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_VALOR")
    protected Date fechaValor;

    @Column(name = "IMPORTE")
    protected Double importe;

    @Column(name = "DESCRIPCION")
    protected String descripcion;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.DENY)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REALIZACION_COBRO_ID")
    protected RealizacionCobro realizacionCobro;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RECIBO_ID")
    protected Recibo recibo;

    @Column(name = "ENT_TO_ENT_ID")
    protected String entToEntId;

    public String getEntToEntId() {
        return entToEntId;
    }

    public void setEntToEntId(String entToEntId) {
        this.entToEntId = entToEntId;
    }

    public Recibo getRecibo() {
        return recibo;
    }

    public void setRecibo(Recibo recibo) {
        this.recibo = recibo;
    }

    public RealizacionCobro getRealizacionCobro() {
        return realizacionCobro;
    }

    public void setRealizacionCobro(RealizacionCobro realizacionCobro) {
        this.realizacionCobro = realizacionCobro;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getImporte() {
        return importe;
    }

    public void setImporte(Double importe) {
        this.importe = importe;
    }

    public Date getFechaValor() {
        return fechaValor;
    }

    public void setFechaValor(Date fechaValor) {
        this.fechaValor = fechaValor;
    }
}