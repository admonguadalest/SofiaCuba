package com.company.test1.entity.documentosImputables;

import com.company.test1.entity.enums.DocumentoImputableTipoEnum;
import com.company.test1.validations.documentosimputables.FacturaProveedorBean;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.chile.core.annotations.NumberFormat;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;
import com.haulmont.cuba.core.global.validation.groups.UiCrossFieldChecks;

import javax.persistence.*;
import java.util.Date;

@DiscriminatorValue("FP")
@Entity(name = "test1_FacturaProveedor")
@NamePattern("facturaproveedor")
@FacturaProveedorBean(groups = UiCrossFieldChecks.class)
public class FacturaProveedor extends DocumentoProveedor {

    public FacturaProveedor(){
        this.setTipoEnum(DocumentoImputableTipoEnum.FACTURA);
    }

    private static final long serialVersionUID = -6938134052389769079L;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_DEVENGO")
    protected Date fechaDevengo;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_PAGO")
    protected Date fechaPago;

    @NumberFormat(pattern = "##0.00", decimalSeparator = ",", groupingSeparator = ".")
    @Column(name = "IMPORTE_FACTURA_PAGADO")
    protected Double importeFacturaPagado;

    @NumberFormat(pattern = "##0.00%", decimalSeparator = ",", groupingSeparator = ".")
    @Column(name = "PORCENTAJE_FACTURA_PAGADO")
    protected Double porcentajeFacturaPagado;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.UNLINK)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRESUPUESTO_ID")
    protected Presupuesto presupuesto;

    @Lob
    @Column(name = "CONSIDERACIONES_DOCUMENTO_IMPUTABLE")
    protected String consideracionesDocumentoImputable;

    @Column(name = "PAGO_POR_CAJA")
    protected Boolean pagoPorCaja;

    public void setFechaDevengo(Date fechaDevengo) {
        this.fechaDevengo = fechaDevengo;
    }

    public Date getFechaDevengo() {
        return fechaDevengo;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }

    public Date getFechaPago() {
        return fechaPago;
    }

    public Boolean getPagoPorCaja() {
        return pagoPorCaja;
    }

    public void setPagoPorCaja(Boolean pagoPorCaja) {
        this.pagoPorCaja = pagoPorCaja;
    }

    public String getConsideracionesDocumentoImputable() {
        return consideracionesDocumentoImputable;
    }

    public void setConsideracionesDocumentoImputable(String consideracionesDocumentoImputable) {
        this.consideracionesDocumentoImputable = consideracionesDocumentoImputable;
    }

    public void setPresupuesto(Presupuesto presupuesto) {
        this.presupuesto = presupuesto;
    }

    public Presupuesto getPresupuesto() {
        return presupuesto;
    }

    public Double getPorcentajeFacturaPagado() {
        return porcentajeFacturaPagado;
    }

    public void setPorcentajeFacturaPagado(Double porcentajeFacturaPagado) {
        this.porcentajeFacturaPagado = porcentajeFacturaPagado;
    }

    public Double getImporteFacturaPagado() {
        return importeFacturaPagado;
    }

    public void setImporteFacturaPagado(Double importeFacturaPagado) {
        this.importeFacturaPagado = importeFacturaPagado;
    }

}