package com.company.test1.entity.notificaciones;

import com.company.test1.entity.contratosinquilinos.ContratoInquilino;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;

@DiscriminatorValue("NCI")
@Entity(name = "test1_NotificacionContratoInquilino")
public class NotificacionContratoInquilino extends Notificacion {
    private static final long serialVersionUID = 1556482362392762292L;

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