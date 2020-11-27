package com.company.test1.entity.ordenespago;

import com.company.test1.entity.extroles.Proveedor;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@DiscriminatorValue("OPP")
@Entity(name = "test1_OrdenPagoProveedor")
public class OrdenPagoProveedor extends OrdenPago {
    private static final long serialVersionUID = 8214727093318651098L;

    @OnDeleteInverse(DeletePolicy.DENY)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROVEEDOR_ID")
    protected Proveedor proveedor;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "ordenPagoProveedor")
    protected List<CompensacionOrdenPagoProveedor> compensacionesOrdenesPagoProveedor;

    @Column(name = "ES_COMPENSACION_ABONO")
    protected Boolean esCompensacionAbono;

    @Transient
    @MetaProperty
    public Double getImportePendienteCompensacion() {
        double importe = this.getImporte();
        double compensado = 0.0;
        for (int i = 0; i < this.getCompensacionesOrdenesPagoProveedor().size(); i++) {
            CompensacionOrdenPagoProveedor copp = this.getCompensacionesOrdenesPagoProveedor().get(i);
            compensado += copp.getImporte();
        }
        return importe - compensado;
    }

    public Boolean getEsCompensacionAbono() {
        return esCompensacionAbono;
    }

    public void setEsCompensacionAbono(Boolean esCompensacionAbono) {
        this.esCompensacionAbono = esCompensacionAbono;
    }

    public List<CompensacionOrdenPagoProveedor> getCompensacionesOrdenesPagoProveedor() {
        if (compensacionesOrdenesPagoProveedor == null){
            compensacionesOrdenesPagoProveedor = new ArrayList<CompensacionOrdenPagoProveedor>();
        }

        return compensacionesOrdenesPagoProveedor;
    }

    public void setCompensacionesOrdenesPagoProveedor(List<CompensacionOrdenPagoProveedor> compensacionesOrdenesPagoProveedor) {
        this.compensacionesOrdenesPagoProveedor = compensacionesOrdenesPagoProveedor;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }
}