package com.company.test1.entity.contratosinquilinos;

import com.company.test1.entity.Persona;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import java.util.Date;

@Table(name = "SUBROGADOR")
@Entity(name = "test1_Subrogador")
public class Subrogador extends StandardEntity {
    private static final long serialVersionUID = 9108444005407950261L;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_DESDE")
    protected Date fechaDesde;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_HASTA")
    protected Date fechaHasta;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONTRATO_INQUILINO_ID")
    protected ContratoInquilino contratoInquilino;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SUBROGADOR_ID")
    protected Persona subrogador;

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public Persona getSubrogador() {
        return subrogador;
    }

    public void setSubrogador(Persona subrogador) {
        this.subrogador = subrogador;
    }

    public ContratoInquilino getContratoInquilino() {
        return contratoInquilino;
    }

    public void setContratoInquilino(ContratoInquilino contratoInquilino) {
        this.contratoInquilino = contratoInquilino;
    }

}