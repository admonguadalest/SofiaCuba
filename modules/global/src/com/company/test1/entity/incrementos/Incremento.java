package com.company.test1.entity.incrementos;

import com.company.test1.entity.recibos.ConceptoRecibo;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import java.util.Date;

@Table(name = "INCREMENTO")
@Entity(name = "test1_Incremento")
public class Incremento extends StandardEntity {
    private static final long serialVersionUID = 3881422789695960497L;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "incremento")
    protected ConceptoRecibo conceptoRecibo;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONCEPTO_RECIBO_ATRASOS_ID")
    protected ConceptoRecibo conceptoReciboAtrasos;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_INCREMENTO")
    protected Date fechaIncremento;

    @Column(name = "IMPORTE")
    protected Double importe;

    @Column(name = "MESES_ATRASOS")
    protected Integer mesesAtrasos;

    @Column(name = "IMPORTE_ATRASOS")
    protected Double importeAtrasos;

    @Column(name = "MESES_REPERCUSION_ATRASOS")
    protected Integer mesesRepercusionAtrasos;

    @Column(name = "RM2ID")
    protected Integer rm2id;

    public Integer getRm2id() {
        return rm2id;
    }

    public void setRm2id(Integer rm2id) {
        this.rm2id = rm2id;
    }

    public ConceptoRecibo getConceptoReciboAtrasos() {
        return conceptoReciboAtrasos;
    }

    public void setConceptoReciboAtrasos(ConceptoRecibo conceptoReciboAtrasos) {
        this.conceptoReciboAtrasos = conceptoReciboAtrasos;
    }

    public Integer getMesesRepercusionAtrasos() {
        return mesesRepercusionAtrasos;
    }

    public void setMesesRepercusionAtrasos(Integer mesesRepercusionAtrasos) {
        this.mesesRepercusionAtrasos = mesesRepercusionAtrasos;
    }

    public Double getImporteAtrasos() {
        return importeAtrasos;
    }

    public void setImporteAtrasos(Double importeAtrasos) {
        this.importeAtrasos = importeAtrasos;
    }

    public Integer getMesesAtrasos() {
        return mesesAtrasos;
    }

    public void setMesesAtrasos(Integer mesesAtrasos) {
        this.mesesAtrasos = mesesAtrasos;
    }

    public Double getImporte() {
        return importe;
    }

    public void setImporte(Double importe) {
        this.importe = importe;
    }

    public Date getFechaIncremento() {
        return fechaIncremento;
    }

    public void setFechaIncremento(Date fechaIncremento) {
        this.fechaIncremento = fechaIncremento;
    }

    public ConceptoRecibo getConceptoRecibo() {
        return conceptoRecibo;
    }

    public void setConceptoRecibo(ConceptoRecibo conceptoRecibo) {
        this.conceptoRecibo = conceptoRecibo;
    }
}