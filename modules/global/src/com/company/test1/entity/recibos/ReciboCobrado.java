package com.company.test1.entity.recibos;

import com.company.test1.entity.enums.recibos.ReciboCobradoModoIngreso;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Table(name = "RECIBO_COBRADO")
@Entity(name = "test1_ReciboCobrado")
public class ReciboCobrado extends StandardEntity {
    private static final long serialVersionUID = 2580194530056194028L;

    @Column(name = "AMPLIACION_INGRESO")
    protected String ampliacionIngreso;

    @Column(name = "DESCRIPCION")
    protected String descripcion;

    @NotNull(message = "Aportar Fecha de Cobro")
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_COBRO")
    protected Date fechaCobro;

    @Column(name = "MODO_INGRESO")
    protected Integer modoIngreso;

    @Column(name = "TOTAL_INGRESO")
    protected Double totalIngreso;

    @Column(name = "COBRANZAS")
    protected Double cobranzas;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RECIBO_ID")
    protected Recibo recibo;

    public Recibo getRecibo() {
        return recibo;
    }

    public void setRecibo(Recibo recibo) {
        this.recibo = recibo;
    }

    public Double getCobranzas() {
        return cobranzas;
    }

    public void setCobranzas(Double cobranzas) {
        this.cobranzas = cobranzas;
    }

    public Double getTotalIngreso() {
        return totalIngreso;
    }

    public void setTotalIngreso(Double totalIngreso) {
        this.totalIngreso = totalIngreso;
    }

    public void setModoIngreso(ReciboCobradoModoIngreso modoIngreso) {
        this.modoIngreso = modoIngreso == null ? null : modoIngreso.getId();
    }

    public ReciboCobradoModoIngreso getModoIngreso() {
        return modoIngreso == null ? null : ReciboCobradoModoIngreso.fromId(modoIngreso);
    }

    public Date getFechaCobro() {
        return fechaCobro;
    }

    public void setFechaCobro(Date fechaCobro) {
        this.fechaCobro = fechaCobro;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getAmpliacionIngreso() {
        return ampliacionIngreso;
    }

    public void setAmpliacionIngreso(String ampliacionIngreso) {
        this.ampliacionIngreso = ampliacionIngreso;
    }
}