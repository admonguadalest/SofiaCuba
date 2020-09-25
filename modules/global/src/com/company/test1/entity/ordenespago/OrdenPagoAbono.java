package com.company.test1.entity.ordenespago;

import com.company.test1.entity.documentosImputables.FacturaProveedor;
import com.company.test1.entity.extroles.Proveedor;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import java.util.List;

@DiscriminatorValue("OPA")
@Entity(name = "test1_OrdenPagoAbono")
public class OrdenPagoAbono extends OrdenPago {
    private static final long serialVersionUID = -3328192380719090956L;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "ordenPagoAbono")
    protected List<CompensacionOrdenPagoProveedor> compensaciones;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROVEEDOR_AB_ID")
    protected Proveedor proveedor;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FACTURA_PROVEEDOR_AB_ID")
    protected FacturaProveedor facturaProveedor;

    @Transient
    @MetaProperty
    public Double getImportePendienteCompensacion() {
        double importe = this.getImporte();
        double compensado = 0.0;
        for (int i = 0; i < this.getCompensaciones().size(); i++) {
            CompensacionOrdenPagoProveedor copp = this.getCompensaciones().get(i);
            compensado += copp.getImporte();
        }
        return importe - compensado;
    }

    public FacturaProveedor getFacturaProveedor() {
        return facturaProveedor;
    }

    public void setFacturaProveedor(FacturaProveedor facturaProveedor) {
        this.facturaProveedor = facturaProveedor;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public List<CompensacionOrdenPagoProveedor> getCompensaciones() {
        return compensaciones;
    }

    public void setCompensaciones(List<CompensacionOrdenPagoProveedor> compensaciones) {
        this.compensaciones = compensaciones;
    }
}