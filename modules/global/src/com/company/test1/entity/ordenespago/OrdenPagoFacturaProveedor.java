package com.company.test1.entity.ordenespago;

import com.company.test1.entity.documentosImputables.FacturaProveedor;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@DiscriminatorValue("OPFP")
@Entity(name = "test1_OrdenPagoFacturaProveedor")
public class OrdenPagoFacturaProveedor extends OrdenPago {
    private static final long serialVersionUID = 5283328444420856825L;

    @Column(name = "ES_ABONO")
    protected Boolean esAbono;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FACTURA_PROVEEDOR_ID")
    protected FacturaProveedor facturaProveedor;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "ordenPagoFacturaProveedor")
    protected List<CompensacionOrdenPagoProveedor> compensaciones = new ArrayList<CompensacionOrdenPagoProveedor>();

    public List<CompensacionOrdenPagoProveedor> getCompensaciones() {
        return compensaciones;
    }

    public void setCompensaciones(List<CompensacionOrdenPagoProveedor> compensaciones) {
        this.compensaciones = compensaciones;
    }

    public FacturaProveedor getFacturaProveedor() {
        return facturaProveedor;
    }

    public void setFacturaProveedor(FacturaProveedor facturaProveedor) {
        this.facturaProveedor = facturaProveedor;
    }

    public Boolean getEsAbono() {
        return esAbono;
    }

    public void setEsAbono(Boolean esAbono) {
        this.esAbono = esAbono;
    }
}