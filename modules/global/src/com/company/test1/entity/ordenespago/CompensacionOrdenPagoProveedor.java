package com.company.test1.entity.ordenespago;

import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;

@Table(name = "COMP_OP_PROVEEDOR")
@Entity(name = "test1_CompensacionOrdenPagoProveedor")
public class CompensacionOrdenPagoProveedor extends StandardEntity {
    private static final long serialVersionUID = 7305605315919428211L;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OP_PROVEEDOR_ID")
    protected OrdenPagoProveedor ordenPagoProveedor;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDEN_PAGO_ABONO_ID")
    protected OrdenPagoAbono ordenPagoAbono;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDEN_PAGO_FACTURA_PROVEEDOR_ID")
    protected OrdenPagoFacturaProveedor ordenPagoFacturaProveedor;

    @Column(name = "IMPORTE")
    protected Double importe;


    public void setOrdenPagoAbono(OrdenPagoAbono ordenPagoAbono) {
        this.ordenPagoAbono = ordenPagoAbono;
    }

    public OrdenPagoAbono getOrdenPagoAbono() {
        return ordenPagoAbono;
    }

    public OrdenPagoFacturaProveedor getOrdenPagoFacturaProveedor() {
        return ordenPagoFacturaProveedor;
    }

    public void setOrdenPagoFacturaProveedor(OrdenPagoFacturaProveedor ordenPagoFacturaProveedor) {
        this.ordenPagoFacturaProveedor = ordenPagoFacturaProveedor;
    }

    public OrdenPagoProveedor getOrdenPagoProveedor() {
        return ordenPagoProveedor;
    }

    public void setOrdenPagoProveedor(OrdenPagoProveedor ordenPagoProveedor) {
        this.ordenPagoProveedor = ordenPagoProveedor;
    }

    public Double getImporte() {
        return importe;
    }

    public void setImporte(Double importe) {
        this.importe = importe;
    }
}