package com.company.test1.entity.departamentos;

import com.company.test1.entity.ArchivoAdjunto;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Table(name = "CEDULA_HABITABILIDAD")
@Entity(name = "test1_CedulaHabitabilidad")
public class CedulaHabitabilidad extends StandardEntity {
    private static final long serialVersionUID = -2775153434899957134L;

    @NotNull(message = "Establecer Fecha de Emision")
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_EMISION")
    protected Date fechaEmision;

    @NotNull(message = "Establecer Fecha de Vencimiento")
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_VENCIMIENTO")
    protected Date fechaVencimiento;

    @NotNull(message = "Aportar numero de cedula")
    @Column(name = "NUMERO_CEDULA", length = 100)
    protected String numeroCedula;

    @Lob
    @Column(name = "OBSERVACIONES")
    protected String observaciones;

    @NotNull(message = "Las Cedulas de Habitabilidad dependen de un Departamento")
    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEPARTAMENTO_ID")
    protected Departamento departamento;

    @NotNull(message = "Aportar Escaneo de Cédula")
    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ESCANEO_CEDULA_ID")
    protected ArchivoAdjunto escaneoCedula;

    public ArchivoAdjunto getEscaneoCedula() {
        return escaneoCedula;
    }

    public void setEscaneoCedula(ArchivoAdjunto escaneoCedula) {
        this.escaneoCedula = escaneoCedula;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getNumeroCedula() {
        return numeroCedula;
    }

    public void setNumeroCedula(String numeroCedula) {
        this.numeroCedula = numeroCedula;
    }

}