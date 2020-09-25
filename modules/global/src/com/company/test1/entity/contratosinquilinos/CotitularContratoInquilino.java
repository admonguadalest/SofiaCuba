package com.company.test1.entity.contratosinquilinos;

import com.company.test1.entity.Persona;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;

@Table(name = "COTITULAR_CONTRATO_INQUILINO")
@Entity(name = "test1_CotitularContratoInquilino")
public class CotitularContratoInquilino extends StandardEntity {
    private static final long serialVersionUID = -8434775731505269299L;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONTRATO_INQUILINO_ID")
    protected ContratoInquilino contratoInquilino;

    @OnDeleteInverse(DeletePolicy.DENY)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COTITULAR_ID")
    protected Persona cotitular;

    public Persona getCotitular() {
        return cotitular;
    }

    public void setCotitular(Persona cotitular) {
        this.cotitular = cotitular;
    }

    public ContratoInquilino getContratoInquilino() {
        return contratoInquilino;
    }

    public void setContratoInquilino(ContratoInquilino contratoInquilino) {
        this.contratoInquilino = contratoInquilino;
    }
}