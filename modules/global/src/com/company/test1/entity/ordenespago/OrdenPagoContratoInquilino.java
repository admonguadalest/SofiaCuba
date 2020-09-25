package com.company.test1.entity.ordenespago;

import com.company.test1.entity.contratosinquilinos.ContratoInquilino;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;

@DiscriminatorValue("OPCI")
@Entity(name = "test1_OrdenPagoContratoInquilino")
public class OrdenPagoContratoInquilino extends OrdenPago {
    private static final long serialVersionUID = 2477419840409942354L;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONTRATO_INQUILINO_ID")
    protected ContratoInquilino contratoInquilino;

    public ContratoInquilino getContratoInquilino() {
        return contratoInquilino;
    }

    public void setContratoInquilino(ContratoInquilino contratoInquilino) {
        this.contratoInquilino = contratoInquilino;
    }
}