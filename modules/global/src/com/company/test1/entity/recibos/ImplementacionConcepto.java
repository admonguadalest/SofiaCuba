package com.company.test1.entity.recibos;

import com.company.test1.entity.conceptosadicionales.RegistroAplicacionConceptoAdicional;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Table(name = "IMPLEMENTACION_CONCEPTO")
@Entity(name = "test1_ImplementacionConcepto")
public class ImplementacionConcepto extends StandardEntity {
    private static final long serialVersionUID = -3942871086533059264L;

    @NotNull(message = "Indicar Importe")
    @Column(name = "IMPORTE")
    protected Double importe;

    @Column(name = "IMPORTE_POST_CCAA")
    protected Double importePostCCAA;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONCEPTO_RECIBO_ID")
    protected ConceptoRecibo conceptoRecibo;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RECIBO_ID")
    protected Recibo recibo;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OVERRIDE_CONCEPTO_ID")
    protected Concepto overrideConcepto;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "implementacionConcepto")
    protected List<RegistroAplicacionConceptoAdicional> registroAplicacionesConceptosAdicionales = new ArrayList<RegistroAplicacionConceptoAdicional>();

    @Column(name = "RM2ID")
    protected Integer rm2id;

    @Transient
    @MetaProperty
    public String getTitulo() {
        if (this.overrideConcepto!=null){
            return this.overrideConcepto.getTituloConcepto();
        }else{
            return this.conceptoRecibo.getConcepto().getTituloConcepto();
        }

    }

    @Transient
    @MetaProperty
    public Concepto getConcepto() {
        if (this.getConceptoRecibo()!=null){
            return this.getConceptoRecibo().getConcepto();
        }else{
            return this.getOverrideConcepto();
        }
    }

    public List<RegistroAplicacionConceptoAdicional> getRegistroAplicacionesConceptosAdicionales() {
        return registroAplicacionesConceptosAdicionales;
    }

    public void setRegistroAplicacionesConceptosAdicionales(List<RegistroAplicacionConceptoAdicional> registroAplicacionesConceptosAdicionales) {
        this.registroAplicacionesConceptosAdicionales = registroAplicacionesConceptosAdicionales;
    }

    public void setRm2id(Integer rm2id) {
        this.rm2id = rm2id;
    }

    public Integer getRm2id() {
        return rm2id;
    }

    public Concepto getOverrideConcepto() {
        return overrideConcepto;
    }

    public void setOverrideConcepto(Concepto overrideConcepto) {
        this.overrideConcepto = overrideConcepto;
    }

    public Recibo getRecibo() {
        return recibo;
    }

    public void setRecibo(Recibo recibo) {
        this.recibo = recibo;
    }

    public ConceptoRecibo getConceptoRecibo() {
        return conceptoRecibo;
    }

    public void setConceptoRecibo(ConceptoRecibo conceptoRecibo) {
        this.conceptoRecibo = conceptoRecibo;
    }

    public Double getImportePostCCAA() {
        return importePostCCAA;
    }

    public void setImportePostCCAA(Double importePostCCAA) {
        this.importePostCCAA = importePostCCAA;
    }

    public Double getImporte() {
        return importe;
    }

    public void setImporte(Double importe) {
        this.importe = importe;
    }
}