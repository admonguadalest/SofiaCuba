package com.company.test1.entity.ordenespago;

import com.company.test1.entity.CuentaBancaria;
import com.company.test1.entity.contratosinquilinos.ContratoInquilino;
import com.company.test1.entity.documentosImputables.FacturaProveedor;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Table(name = "ORDEN_PAGO")
@Entity(name = "test1_OrdenPago")
public class OrdenPago extends StandardEntity {
    private static final long serialVersionUID = 7846178937176600804L;

    @NotNull(message = "Aportar fecha de valor")
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_VALOR")
    protected Date fechaValor;

    @NotNull(message = "Indicar importe")
    @Column(name = "IMPORTE")
    protected Double importe;

    @Column(name = "IMPORTE_EFECTIVO")
    protected Double importeEfectivo;

    @Column(name = "DESCRIPCION")
    protected String descripcion;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REALIZACION_PAGO_ID")
    protected RealizacionPago realizacionPago;

    @Column(name = "RM2ID")
    protected Integer rm2id;



    @Transient
    @MetaProperty
    public String getNombreTipo() {
        if (this instanceof OrdenPagoProveedor) return "OPP";
        if (this instanceof OrdenPagoAbono) return "OPA";
        if (this instanceof OrdenPagoFacturaProveedor) return "OPFP";
        if (this instanceof OrdenPagoContratoInquilino) return "OPCI";
        return "Undefined";
    }

    public void setFechaValor(Date fechaValor) {
        this.fechaValor = fechaValor;
    }

    public Date getFechaValor() {
        return fechaValor;
    }

    public Integer getRm2id() {
        return rm2id;
    }

    public void setRm2id(Integer rm2id) {
        this.rm2id = rm2id;
    }

    public RealizacionPago getRealizacionPago() {
        return realizacionPago;
    }

    public void setRealizacionPago(RealizacionPago realizacionPago) {
        this.realizacionPago = realizacionPago;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getImporteEfectivo() {
        return importeEfectivo;
    }

    public void setImporteEfectivo(Double importeEfectivo) {
        this.importeEfectivo = importeEfectivo;
    }

    public Double getImporte() {
        return importe;
    }

    public void setImporte(Double importe) {
        this.importe = importe;
    }

    @Transient
    @MetaProperty
    public String getDato() {
        if (this instanceof OrdenPagoFacturaProveedor){
            OrdenPagoFacturaProveedor opfp = (OrdenPagoFacturaProveedor) this;
            return opfp.getFacturaProveedor().getProveedor().getPersona().getNombreCompleto() + " " + opfp.getFacturaProveedor().getNumDocumento();
        }
        if (this instanceof OrdenPagoProveedor){
            OrdenPagoProveedor opp = (OrdenPagoProveedor) this;
            return opp.getProveedor().getPersona().getNombreCompleto();
        }
        if (this instanceof OrdenPagoContratoInquilino){
            OrdenPagoContratoInquilino opci = (OrdenPagoContratoInquilino) this;
            opci.getContratoInquilino().getNumeroContrato();
        }
        return "N/D";
    }

    @Transient
    @MetaProperty
    public CuentaBancaria getCuentaBancariaOrdenPago() {
        if (this instanceof OrdenPagoFacturaProveedor){
            OrdenPagoFacturaProveedor opfp = (OrdenPagoFacturaProveedor) this;
            return opfp.getFacturaProveedor().getProveedor().getCuentaBancaria();
        }
        if (this instanceof OrdenPagoProveedor){
            OrdenPagoProveedor opp = (OrdenPagoProveedor) this;
            return opp.getProveedor().getCuentaBancaria();
        }
        if (this instanceof OrdenPagoContratoInquilino){
            OrdenPagoContratoInquilino opci = (OrdenPagoContratoInquilino) this;
            return opci.getContratoInquilino().getProgramacionRecibo().getCuentaBancariaInquilino();
        }
        return null;
    }

}