package com.company.test1.entity.cuentadegasto;

import com.company.test1.entity.ColeccionArchivosAdjuntos;
import com.company.test1.entity.documentosImputables.FacturaProveedor;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;
import java.util.Date;

@Table(name = "TEST1_MOVIMIENTO_CUENTA_DE_GASTO")
@Entity(name = "test1_MovimientoCuentaDeGasto")
public class MovimientoCuentaDeGasto extends StandardEntity {
    private static final long serialVersionUID = 5006681643887256910L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CUENTA_DE_GASTO_ID")
    private CuentaDeGasto cuentaDeGasto;

    @Column(name = "TIPO_DE_GASTO")
    private String tipoDeGasto;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA")
    private Date fecha;

    @Column(name = "IMPORTE_BASE")
    private Double importeBase;

    @Column(name = "IMPORTE_POST_CCAA")
    private Double importePostCCAA;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COLECCION_ARCHIVOS_ADJUNTOS_ID")
    private ColeccionArchivosAdjuntos coleccionArchivosAdjuntos;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FACTURA_PROVEEDOR_ASOCIADO_ID")
    private FacturaProveedor facturaProveedorAsociado;

    public FacturaProveedor getFacturaProveedorAsociado() {
        return facturaProveedorAsociado;
    }

    public void setFacturaProveedorAsociado(FacturaProveedor facturaProveedorAsociado) {
        this.facturaProveedorAsociado = facturaProveedorAsociado;
    }

    public ColeccionArchivosAdjuntos getColeccionArchivosAdjuntos() {
        return coleccionArchivosAdjuntos;
    }

    public void setColeccionArchivosAdjuntos(ColeccionArchivosAdjuntos coleccionArchivosAdjuntos) {
        this.coleccionArchivosAdjuntos = coleccionArchivosAdjuntos;
    }

    public Double getImportePostCCAA() {
        return importePostCCAA;
    }

    public void setImportePostCCAA(Double importePostCCAA) {
        this.importePostCCAA = importePostCCAA;
    }

    public Double getImporteBase() {
        return importeBase;
    }

    public void setImporteBase(Double importeBase) {
        this.importeBase = importeBase;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getTipoDeGasto() {
        return tipoDeGasto;
    }

    public void setTipoDeGasto(String tipoDeGasto) {
        this.tipoDeGasto = tipoDeGasto;
    }

    public CuentaDeGasto getCuentaDeGasto() {
        return cuentaDeGasto;
    }

    public void setCuentaDeGasto(CuentaDeGasto cuentaDeGasto) {
        this.cuentaDeGasto = cuentaDeGasto;
    }

}