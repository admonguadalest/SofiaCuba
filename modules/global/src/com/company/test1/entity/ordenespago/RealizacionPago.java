package com.company.test1.entity.ordenespago;

import com.company.test1.entity.CuentaBancaria;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Table(name = "REALIZACION_PAGO")
@Entity(name = "test1_RealizacionPago")
public class RealizacionPago extends StandardEntity {
    private static final long serialVersionUID = 2931477070508162197L;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OneToMany(mappedBy = "realizacionPago")
    protected List<OrdenPago> ordenesPago = new ArrayList<OrdenPago>();

    @Column(name = "MARCA_PAGADO_ADMINISTRADOR")
    private Boolean marcaPagadoAdministrador;

    @OnDeleteInverse(DeletePolicy.DENY)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CUENTA_BANCARIA_ID")
    protected CuentaBancaria cuentaBancaria;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.UNLINK)
    @OneToMany(mappedBy = "realizacionPago")
    protected List<OrdenPagoProveedor> ordenesPagoProveedor = new ArrayList<OrdenPagoProveedor>();

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_VALOR")
    protected Date fechaValor;

    @Column(name = "INFO_CUENTA_EMISORA")
    protected String infoCuentaEmisora;

    @Column(name = "XSD", length = 25)
    protected String xsd = "pain.001.001.03";

    @Lob
    @Column(name = "SEPA")
    protected String sepa;

    @Column(name = "IDENTIFICADOR")
    protected String identificador;

    @Column(name = "IMPORTE_TOTAL")
    protected Double importeTotal;

    @Column(name = "RM2ID")
    protected Integer rm2id;

    public Boolean getMarcaPagadoAdministrador() {
        return marcaPagadoAdministrador;
    }

    public void setMarcaPagadoAdministrador(Boolean marcaPagadoAdministrador) {
        this.marcaPagadoAdministrador = marcaPagadoAdministrador;
    }

    public void setFechaValor(Date fechaValor) {
        this.fechaValor = fechaValor;
    }

    public Date getFechaValor() {
        return fechaValor;
    }

    public List<OrdenPagoProveedor> getOrdenesPagoProveedor() {
        return ordenesPagoProveedor;
    }

    public void setOrdenesPagoProveedor(List<OrdenPagoProveedor> ordenesPagoProveedor) {
        this.ordenesPagoProveedor = ordenesPagoProveedor;
    }

    public Double getImporteTotal() {
        return importeTotal;
    }

    public void setImporteTotal(Double importeTotal) {
        this.importeTotal = importeTotal;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getSepa() {
        return sepa;
    }

    public void setSepa(String sepa) {
        this.sepa = sepa;
    }

    public String getXsd() {
        return xsd;
    }

    public void setXsd(String xsd) {
        this.xsd = xsd;
    }

    public String getInfoCuentaEmisora() {
        return infoCuentaEmisora;
    }

    public void setInfoCuentaEmisora(String infoCuentaEmisora) {
        this.infoCuentaEmisora = infoCuentaEmisora;
    }

    public CuentaBancaria getCuentaBancaria() {
        return cuentaBancaria;
    }

    public void setCuentaBancaria(CuentaBancaria cuentaBancaria) {
        this.cuentaBancaria = cuentaBancaria;
    }

    public List<OrdenPago> getOrdenesPago() {
        return ordenesPago;
    }

    public void setOrdenesPago(List<OrdenPago> ordenesPago) {
        this.ordenesPago = ordenesPago;
    }

    public Integer getRm2id() {
        return rm2id;
    }

    public void setRm2id(Integer rm2id) {
        this.rm2id = rm2id;
    }
}