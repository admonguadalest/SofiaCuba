package com.company.test1.entity.documentosImputables;

import com.company.test1.entity.enums.DocumentoImputableTipoEnum;
import com.company.test1.validations.documentosimputables.PresupuestoBean;
import com.haulmont.cuba.core.global.validation.groups.UiCrossFieldChecks;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@DiscriminatorValue("P")
@Entity(name = "test1_Presupuesto")
@PresupuestoBean(groups = UiCrossFieldChecks.class)
public class Presupuesto extends DocumentoProveedor {
    private static final long serialVersionUID = 5354320281546867429L;

    @Column(name = "ES_PRESUPUESTO_VERBAL")
    protected Boolean esPresupuestoVerbal;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_VALIDEZ_HASTA")
    protected Date fechaValidezHasta;

    @NotNull(message = "Indicar realizador de Presupuesto")
    @Column(name = "REALIZADO_POR")
    protected String realizadoPor;

    @Column(name = "IMPORTE_DEFINITIVO")
    private Double importeDefinitivo;

    @Lob
    @Column(name = "CONSIDERACIONES_PRESUPUESTO")
    private String consideracionesPresupuesto;

    public String getConsideracionesPresupuesto() {
        return consideracionesPresupuesto;
    }

    public void setConsideracionesPresupuesto(String consideracionesPresupuesto) {
        this.consideracionesPresupuesto = consideracionesPresupuesto;
    }

    public Presupuesto() {
        this.setTipoEnum(DocumentoImputableTipoEnum.PRESUPUESTO);
    }


    public Double getImporteDefinitivo() {
        return importeDefinitivo;
    }

    public void setImporteDefinitivo(Double importeDefinitivo) {
        this.importeDefinitivo = importeDefinitivo;
    }

    public String getRealizadoPor() {
        return realizadoPor;
    }

    public void setRealizadoPor(String realizadoPor) {
        this.realizadoPor = realizadoPor;
    }

    public Date getFechaValidezHasta() {
        return fechaValidezHasta;
    }

    public void setFechaValidezHasta(Date fechaValidezHasta) {
        this.fechaValidezHasta = fechaValidezHasta;
    }

    public Boolean getEsPresupuestoVerbal() {
        return esPresupuestoVerbal;
    }

    public void setEsPresupuestoVerbal(Boolean esPresupuestoVerbal) {
        this.esPresupuestoVerbal = esPresupuestoVerbal;
    }
}