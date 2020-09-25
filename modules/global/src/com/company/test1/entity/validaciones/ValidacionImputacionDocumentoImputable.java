package com.company.test1.entity.validaciones;

import com.company.test1.entity.ciclos.ImputacionDocumentoImputable;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

@DiscriminatorValue("VIDI")
@Entity(name = "test1_ValidacionImputacionDocumentoImputable")
public class ValidacionImputacionDocumentoImputable extends Validacion {
    private static final long serialVersionUID = -3777252162120977075L;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "validacionImputacion")
    protected ImputacionDocumentoImputable imputacionDocumentoImputable;

    public ImputacionDocumentoImputable getImputacionDocumentoImputable() {
        return imputacionDocumentoImputable;
    }

    public void setImputacionDocumentoImputable(ImputacionDocumentoImputable imputacionDocumentoImputable) {
        this.imputacionDocumentoImputable = imputacionDocumentoImputable;
    }
}