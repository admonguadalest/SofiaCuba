package com.company.test1.entity.documentosImputables;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.BaseUuidEntity;

import java.util.Date;

@MetaClass(name = "test1_DocumentoProveedorReport")
public class DocumentoProveedorReport extends BaseUuidEntity {
    private static final long serialVersionUID = -4943119108834130124L;

    @MetaProperty
    private String titularNombreCompleto;

    @MetaProperty
    private String proveedorNombreCompleto;

    @MetaProperty
    private Date fechaEmision;

    @MetaProperty
    private String numDocumento;

    @MetaProperty
    private Double base;

    @MetaProperty
    private Double irpf;

    @MetaProperty
    private Double iva;

    @MetaProperty
    private Double total;

    @MetaProperty
    private String textoImputaciones;

    @MetaProperty
    private Date fechaRegistro;

    @MetaProperty
    private String porcentajesImputaciones;

    @MetaProperty
    private String documentoProveedorId;

    public String getDocumentoProveedorId() {
        return documentoProveedorId;
    }

    public void setDocumentoProveedorId(String documentoProveedorId) {
        this.documentoProveedorId = documentoProveedorId;
    }

    public String getPorcentajesImputaciones() {
        return porcentajesImputaciones;
    }

    public void setPorcentajesImputaciones(String porcentajesImputaciones) {
        this.porcentajesImputaciones = porcentajesImputaciones;
    }

    public void setBase(Double base) {
        this.base = base;
    }

    public Double getBase() {
        return base;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getTextoImputaciones() {
        return textoImputaciones;
    }

    public void setTextoImputaciones(String textoImputaciones) {
        this.textoImputaciones = textoImputaciones;
    }

    public Double getIrpf() {
        return irpf;
    }

    public void setIrpf(Double irpf) {
        this.irpf = irpf;
    }

    public Double getIva() {
        return iva;
    }

    public void setIva(Double iva) {
        this.iva = iva;
    }

    public String getNumDocumento() {
        return numDocumento;
    }

    public void setNumDocumento(String numDocumento) {
        this.numDocumento = numDocumento;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getProveedorNombreCompleto() {
        return proveedorNombreCompleto;
    }

    public void setProveedorNombreCompleto(String proveedorNombreCompleto) {
        this.proveedorNombreCompleto = proveedorNombreCompleto;
    }

    public String getTitularNombreCompleto() {
        return titularNombreCompleto;
    }

    public void setTitularNombreCompleto(String titularNombreCompleto) {
        this.titularNombreCompleto = titularNombreCompleto;
    }
}