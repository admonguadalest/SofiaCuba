package com.company.test1.entity.ordenescobro;

import com.company.test1.entity.CuentaBancaria;
import com.company.test1.entity.recibos.Recibo;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Table(name = "TEST1_REALIZACION_COBRO")
@Entity(name = "test1_RealizacionCobro")
public class RealizacionCobro extends StandardEntity {
    private static final long serialVersionUID = -2174892868827845941L;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_VALOR")
    protected Date fechaValor;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CUENTA_BANCARIA_ID")
    protected CuentaBancaria cuentaBancaria;

    @Lob
    @Column(name = "XSD")
    protected String xsd;

    @Lob
    @Column(name = "SEPA")
    protected String sepa;

    @Column(name = "IDENTIFICADOR")
    protected String identificador;

    @Column(name = "IMPORTE_TOTAL")
    protected Double importeTotal;

    @OnDeleteInverse(DeletePolicy.DENY)
    @OnDelete(DeletePolicy.UNLINK)
    @OneToMany(mappedBy = "realizacionCobro")
    protected List<OrdenCobro> ordenesCobro = new ArrayList<OrdenCobro>();

    @MetaProperty
    public String getInfoInquilino(){
        if (ordenesCobro.size()!=1){
            return "REMESA MULTIPLE";
        }else{
            OrdenCobro oc = ordenesCobro.get(0);
            try {
                String s = oc.getDeudor().getNombreCompleto();
                return s;
            }catch(Exception exc){
                Recibo r = oc.getRecibo();
                String s = r.getUtilitarioContratoInquilino().getInquilino().getNombreCompleto();
                return s;
            }
        }
    }

    @Column(name = "RM2ID")
    protected Integer rm2id;

    public CuentaBancaria getCuentaBancaria() {
        return cuentaBancaria;
    }

    public void setCuentaBancaria(CuentaBancaria cuentaBancaria) {
        this.cuentaBancaria = cuentaBancaria;
    }

    public Integer getRm2id() {
        return rm2id;
    }

    public void setRm2id(Integer rm2id) {
        this.rm2id = rm2id;
    }

    public List<OrdenCobro> getOrdenesCobro() {
        return ordenesCobro;
    }

    public void setOrdenesCobro(List<OrdenCobro> ordenesCobro) {
        this.ordenesCobro = ordenesCobro;
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

    public Date getFechaValor() {
        return fechaValor;
    }

    public void setFechaValor(Date fechaValor) {
        this.fechaValor = fechaValor;
    }
}