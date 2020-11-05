package com.company.test1.entity.recibos;

import com.company.test1.entity.Persona;
import com.company.test1.entity.contratosinquilinos.ContratoInquilino;
import com.company.test1.entity.enums.recibos.ReciboCobradoModoIngreso;
import com.company.test1.entity.enums.recibos.ReciboGradoImpago;
import com.company.test1.entity.ordenescobro.OrdenCobro;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.Listeners;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.entity.annotation.PublishEntityChangedEvents;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@PublishEntityChangedEvents
@Table(name = "RECIBO")
@Entity(name = "test1_Recibo")
//@Listeners("test1_ReciboEntityListener")
public class Recibo extends StandardEntity {
    private static final long serialVersionUID = -8484099048421269202L;

    @Column(name = "AMPLIACION")
    protected String ampliacion;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_EMISION")
    protected Date fechaEmision;

    @Column(name = "TOTAL_RECIBO")
    protected Double totalRecibo;

    @Column(name = "TOTAL_RECIBO_POST_CCAA")
    protected Double totalReciboPostCCAA;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROGRAMACION_RECIBO_ID")
    protected ProgramacionRecibo programacionRecibo;

    @Column(name = "NUM_RECIBO", length = 100)
    protected String numRecibo;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_VALOR")
    protected Date fechaValor;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDENANTE_REMESA_ID")
    protected OrdenanteRemesa ordenanteRemesa;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SERIE_ID")
    protected Serie serie;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UTILITARIO_CONTRATO_INQUILINO_ID")
    protected ContratoInquilino utilitarioContratoInquilino;

    @Column(name = "GRADO_ESTADO_IMPAGO")
    protected Integer gradoEstadoImpago;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_ESTADO_PENDIENTE")
    protected Date fechaEstadoPendiente;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_ESTADO_INCOBRABLE")
    protected Date fechaEstadoIncobrable;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UTILITARIO_INQUILINO_ID")
    protected Persona utilitarioInquilino;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "recibo")
    protected List<ImplementacionConcepto> implementacionesConceptos = new ArrayList<ImplementacionConcepto>();

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "recibo")
    protected List<OrdenCobro> ordenesCobro = new ArrayList<OrdenCobro>();

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "recibo")
    protected List<ReciboCobrado> recibosCobrados = new ArrayList<ReciboCobrado>();

    @Column(name = "RM2ID")
    protected Integer rm2id;

    @Transient
    @MetaProperty
    public Double getTotalCobrado() {
        Double d = 0.0;
        List<ReciboCobrado> rrcc = this.getRecibosCobrados();
        for (int i = 0; i < rrcc.size(); i++) {
            ReciboCobrado rc = recibosCobrados.get(i);
            if ((rc.getModoIngreso()== ReciboCobradoModoIngreso.ADMINISTRACION)||(rc.getModoIngreso()==ReciboCobradoModoIngreso.BANCARIO)||(rc.getModoIngreso()==ReciboCobradoModoIngreso.INGRESO_TALON)){
                d += rc.getTotalIngreso();
            }else if(rc.getModoIngreso()==ReciboCobradoModoIngreso.DEVUELTO){
                d -= rc.getTotalIngreso();
            }else if(rc.getModoIngreso()==ReciboCobradoModoIngreso.COMPENSACION_ABONO_RECIBO){
                d += rc.getTotalIngreso();
            }
        }
        return d;
    }

    public List<OrdenCobro> getOrdenesCobro() {
        return ordenesCobro;
    }

    public void setOrdenesCobro(List<OrdenCobro> ordenesCobro) {
        this.ordenesCobro = ordenesCobro;
    }

    public List<ReciboCobrado> getRecibosCobrados() {
        return recibosCobrados;
    }

    public void setRecibosCobrados(List<ReciboCobrado> recibosCobrados) {
        this.recibosCobrados = recibosCobrados;
    }

    public List<ImplementacionConcepto> getImplementacionesConceptos() {
        return implementacionesConceptos;
    }

    public void setImplementacionesConceptos(List<ImplementacionConcepto> implementacionesConceptos) {
        this.implementacionesConceptos = implementacionesConceptos;
    }

    public void setRm2id(Integer rm2id) {
        this.rm2id = rm2id;
    }

    public Integer getRm2id() {
        return rm2id;
    }

    public void setUtilitarioInquilino(Persona utilitarioInquilino) {
        this.utilitarioInquilino = utilitarioInquilino;
    }

    public Persona getUtilitarioInquilino() {
        return utilitarioInquilino;
    }

    public Date getFechaEstadoIncobrable() {
        return fechaEstadoIncobrable;
    }

    public void setFechaEstadoIncobrable(Date fechaEstadoIncobrable) {
        this.fechaEstadoIncobrable = fechaEstadoIncobrable;
    }

    public Date getFechaEstadoPendiente() {
        return fechaEstadoPendiente;
    }

    public void setFechaEstadoPendiente(Date fechaEstadoPendiente) {
        this.fechaEstadoPendiente = fechaEstadoPendiente;
    }

    public ReciboGradoImpago getGradoEstadoImpago() {
        return gradoEstadoImpago == null ? null : ReciboGradoImpago.fromId(gradoEstadoImpago);
    }

    public void setGradoEstadoImpago(ReciboGradoImpago gradoEstadoImpago) {
        this.gradoEstadoImpago = gradoEstadoImpago == null ? null : gradoEstadoImpago.getId();
    }

    public ContratoInquilino getUtilitarioContratoInquilino() {
        return utilitarioContratoInquilino;
    }

    public void setUtilitarioContratoInquilino(ContratoInquilino utilitarioContratoInquilino) {
        this.utilitarioContratoInquilino = utilitarioContratoInquilino;
    }

    public Serie getSerie() {
        return serie;
    }

    public void setSerie(Serie serie) {
        this.serie = serie;
    }

    public OrdenanteRemesa getOrdenanteRemesa() {
        return ordenanteRemesa;
    }

    public void setOrdenanteRemesa(OrdenanteRemesa ordenanteRemesa) {
        this.ordenanteRemesa = ordenanteRemesa;
    }

    public Date getFechaValor() {
        return fechaValor;
    }

    public void setFechaValor(Date fechaValor) {
        this.fechaValor = fechaValor;
    }

    public String getNumRecibo() {
        return numRecibo;
    }

    public void setNumRecibo(String numRecibo) {
        this.numRecibo = numRecibo;
    }

    public ProgramacionRecibo getProgramacionRecibo() {
        return programacionRecibo;
    }

    public void setProgramacionRecibo(ProgramacionRecibo programacionRecibo) {
        this.programacionRecibo = programacionRecibo;
    }

    public Double getTotalReciboPostCCAA() {
        return totalReciboPostCCAA;
    }

    public void setTotalReciboPostCCAA(Double totalReciboPostCCAA) {
        this.totalReciboPostCCAA = totalReciboPostCCAA;
    }

    public Double getTotalRecibo() {
        return totalRecibo;
    }

    public void setTotalRecibo(Double totalRecibo) {
        this.totalRecibo = totalRecibo;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getAmpliacion() {
        return ampliacion;
    }

    public void setAmpliacion(String ampliacion) {
        this.ampliacion = ampliacion;
    }


    public double getTotalPendiente(){
        double pendiente = this.totalReciboPostCCAA + this.getTotalCobranzas() - this.getTotalCobrado() - this.getTotalCompensado();
        return pendiente;
    }

    public double getTotalCompensado(){
        double d = 0;
        for (int i = 0; i < recibosCobrados.size(); i++) {
            ReciboCobrado rc = recibosCobrados.get(i);
            if (rc.getModoIngreso()==ReciboCobradoModoIngreso.COMPENSACION_ABONO_RECIBO){
                d += rc.getTotalIngreso();
            }
        }
        return d;
    }

    public double getTotalCobranzas(){
        double cobranzas = 0;
        for (int i = 0; i < recibosCobrados.size(); i++) {
            ReciboCobrado rc = recibosCobrados.get(i);
            if ((rc.getModoIngreso()==ReciboCobradoModoIngreso.ADMINISTRACION)||(rc.getModoIngreso()==ReciboCobradoModoIngreso.BANCARIO)){
                //no afecta
            }else if(rc.getModoIngreso()==ReciboCobradoModoIngreso.DEVUELTO){
                if (rc.getCobranzas()==null){
                    rc.setCobranzas(0.0);
                }
                cobranzas += rc.getCobranzas();
            }
        }
        return cobranzas;
    }

//    public double getTotalCobrad_o(){
//        double d = 0;
//        for (int i = 0; i < recibosCobrados.size(); i++) {
//            ReciboCobrado rc = recibosCobrados.get(i);
//            if ((rc.getModoIngreso()==ReciboCobradoModoIngreso.ADMINISTRACION)||(rc.getModoIngreso()==ReciboCobradoModoIngreso.BANCARIO)||(rc.getModoIngreso()==ReciboCobradoModoIngreso.INGRESO_TALON)){
//                d += rc.getTotalIngreso();
//            }else if(rc.getModoIngreso()==ReciboCobradoModoIngreso.DEVUELTO){
//                d -= rc.getTotalIngreso();
//            }
//        }
//        return d;
//    }
}