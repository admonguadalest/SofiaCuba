package com.company.test1.entity.conceptosadicionales;

import com.company.test1.entity.extroles.Proveedor;
import com.company.test1.entity.recibos.ProgramacionRecibo;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;

@Table(name = "PROGRAMACION_CONCEPTO_ADICIONAL")
@Entity(name = "test1_ProgramacionConceptoAdicional")
public class ProgramacionConceptoAdicional extends StandardEntity {
    private static final long serialVersionUID = -5566463150955608810L;

    @Lookup(type = LookupType.DROPDOWN)
    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONCEPTO_ADICIONAL_ID")
    protected ConceptoAdicional conceptoAdicional;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROVEEDOR_ID")
    protected Proveedor proveedor;

    @OnDeleteInverse(DeletePolicy.DENY)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROGRAMACION_RECIBO_ID")
    protected ProgramacionRecibo programacionRecibo;

    public ProgramacionRecibo getProgramacionRecibo() {
        return programacionRecibo;
    }

    public void setProgramacionRecibo(ProgramacionRecibo programacionRecibo) {
        this.programacionRecibo = programacionRecibo;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public ConceptoAdicional getConceptoAdicional() {
        return conceptoAdicional;
    }

    public void setConceptoAdicional(ConceptoAdicional conceptoAdicional) {
        this.conceptoAdicional = conceptoAdicional;
    }
}