package com.company.test1.entity.contratosinquilinos;

import com.company.test1.entity.departamentos.Departamento;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Table(name = "OFERTA")
@Entity(name = "test1_Oferta")
public class Oferta extends StandardEntity {
    private static final long serialVersionUID = -8634466766543522332L;

    @NotNull
    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.DENY)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "DEPARTAMENTO_ID")
    private Departamento departamento;

    @NotNull
    @Column(name = "IMPORTE_RENTA", nullable = false)
    private Double importeRenta;

    @Lob
    @Column(name = "OBSERVACIONES")
    private String observaciones;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "oferta")
    private List<Reserva> reservas;

    public List<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Double getImporteRenta() {
        return importeRenta;
    }

    public void setImporteRenta(Double importeRenta) {
        this.importeRenta = importeRenta;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }
}