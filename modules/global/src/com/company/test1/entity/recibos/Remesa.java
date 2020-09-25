package com.company.test1.entity.recibos;

import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Table(name = "REMESA")
@Entity(name = "test1_Remesa")
public class Remesa extends StandardEntity {
    private static final long serialVersionUID = -5841751709380954080L;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_ADEUDO")
    protected Date fechaAdeudo;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_REALIZACION")
    protected Date fechaRealizacion;

    @Column(name = "IDENTIFICADOR_REMESA")
    protected String identificadorRemesa;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEFINICION_REMESA_ID")
    protected DefinicionRemesa definicionRemesa;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_VALOR")
    protected Date fechaValor;

    @Column(name = "RM2ID")
    protected Integer rm2id;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "remesa")
    protected List<OrdenanteRemesa> ordenantesRemesa = new ArrayList<OrdenanteRemesa>();

    @Column(name = "TOTAL_REMESA")
    protected Double totalRemesa;

    public Double getTotalRemesa() {
        return totalRemesa;
    }

    public void setTotalRemesa(Double totalRemesa) {
        this.totalRemesa = totalRemesa;
    }

    public List<OrdenanteRemesa> getOrdenantesRemesa() {
        return ordenantesRemesa;
    }

    public void setOrdenantesRemesa(List<OrdenanteRemesa> ordenantesRemesa) {
        this.ordenantesRemesa = ordenantesRemesa;
    }

    public Integer getRm2id() {
        return rm2id;
    }

    public void setRm2id(Integer rm2id) {
        this.rm2id = rm2id;
    }

    public Date getFechaValor() {
        return fechaValor;
    }

    public void setFechaValor(Date fechaValor) {
        this.fechaValor = fechaValor;
    }

    public DefinicionRemesa getDefinicionRemesa() {
        return definicionRemesa;
    }

    public void setDefinicionRemesa(DefinicionRemesa definicionRemesa) {
        this.definicionRemesa = definicionRemesa;
    }

    public String getIdentificadorRemesa() {
        return identificadorRemesa;
    }

    public void setIdentificadorRemesa(String identificadorRemesa) {
        this.identificadorRemesa = identificadorRemesa;
    }

    public Date getFechaRealizacion() {
        return fechaRealizacion;
    }

    public void setFechaRealizacion(Date fechaRealizacion) {
        this.fechaRealizacion = fechaRealizacion;
    }

    public Date getFechaAdeudo() {
        return fechaAdeudo;
    }

    public void setFechaAdeudo(Date fechaAdeudo) {
        this.fechaAdeudo = fechaAdeudo;
    }
}