package com.company.test1.entity.contratosinquilinos;

import com.company.test1.entity.extroles.ComercialOfertas;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Table(name = "TEST1_RESERVA")
@Entity(name = "test1_Reserva")
public class Reserva extends StandardEntity {
    private static final long serialVersionUID = 3418012462790511596L;

    @Temporal(TemporalType.DATE)
    @NotNull
    @Column(name = "FECHA_RESERVA", nullable = false)
    private Date fechaReserva;

    @NotNull
    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.DENY)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "COMERCIAL_ID")
    private ComercialOfertas comercial;

    @Lob
    @Column(name = "CONDICIONES_RESERVA")
    private String condicionesReserva;

    @Column(name = "ESTADO_RESERVA")
    private String estadoReserva;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OFERTA_ID")
    private Oferta oferta;

    public Oferta getOferta() {
        return oferta;
    }

    public void setOferta(Oferta oferta) {
        this.oferta = oferta;
    }

    public String getEstadoReserva() {
        return estadoReserva;
    }

    public void setEstadoReserva(String estadoReserva) {
        this.estadoReserva = estadoReserva;
    }

    public String getCondicionesReserva() {
        return condicionesReserva;
    }

    public void setCondicionesReserva(String condicionesReserva) {
        this.condicionesReserva = condicionesReserva;
    }

    public ComercialOfertas getComercial() {
        return comercial;
    }

    public void setComercial(ComercialOfertas comercial) {
        this.comercial = comercial;
    }

    public Date getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(Date fechaReserva) {
        this.fechaReserva = fechaReserva;
    }
}