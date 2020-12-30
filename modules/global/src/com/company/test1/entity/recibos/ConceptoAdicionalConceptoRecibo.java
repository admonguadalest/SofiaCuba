package com.company.test1.entity.recibos;

import com.company.test1.entity.conceptosadicionales.ConceptoAdicional;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "CONCEPTO_ADICIONAL_CONCEPTO_RECIBO")
@Entity(name = "test1_ConceptoAdicionalConceptoRecibo")
public class ConceptoAdicionalConceptoRecibo extends StandardEntity {
    private static final long serialVersionUID = 2356029786747929885L;

    @NotNull(message = "Especificar Concepto Adicional")
    @OnDeleteInverse(DeletePolicy.DENY)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CA_ID")
    protected ConceptoAdicional conceptoAdicional;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONCEPTO_RECIBO_ID")
    protected ConceptoRecibo conceptoRecibo;

    @NotNull(message = "Indicar porcentaje")
    @Column(name = "PORCENTAJE")
    protected Double porcentaje;

    public Double getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(Double porcentaje) {
        this.porcentaje = porcentaje;
    }

    public ConceptoRecibo getConceptoRecibo() {
        return conceptoRecibo;
    }

    public void setConceptoRecibo(ConceptoRecibo conceptoRecibo) {
        this.conceptoRecibo = conceptoRecibo;
    }

    public ConceptoAdicional getConceptoAdicional() {
        return conceptoAdicional;
    }

    public void setConceptoAdicional(ConceptoAdicional conceptoAdicional) {
        this.conceptoAdicional = conceptoAdicional;
    }
}