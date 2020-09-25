package com.company.test1.entity.contratosinquilinos;

import com.company.test1.entity.ArchivoAdjunto;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import java.util.Date;

@Table(name = "LIQUIDACION_SUSCRIPCION")
@Entity(name = "test1_LiquidacionSuscripcion")
public class LiquidacionSuscripcion extends StandardEntity {
    private static final long serialVersionUID = 3104671789656269949L;

    @Column(name = "ES_RENOVACION")
    protected Boolean esRenovacion = false;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ESCANEO_LIQUIDACION_ID")
    protected ArchivoAdjunto escaneoLiquidacion;


    public LiquidacionSuscripcion() {
    }

    public LiquidacionSuscripcion(ContratoInquilino contratoInquilino) {
        this.contratoInquilino = contratoInquilino;
    }

    @Column(name = "FIANZA_CONTRATO")
    protected Double fianzaContrato;

    @Column(name = "GARANTIAS_EN_EFECTIVO")
    protected Double garantiasEnEfectivo;

    @Column(name = "RECIBOS_A_CUENTA")
    protected Double recibosACuenta;

    @Column(name = "DESC_RECIBOS_A_CUENTA")
    protected String descRecibosACuenta;

    @Column(name = "DESC_DEVOLUCION_FIANZA_CONTRATO_RENUNCIADO")
    protected String descDevolucionFianzaContratoRenunciado;

    @Column(name = "DEVOLUCION_FIANZA_CONTRATO_RENUNCIADO")
    protected Double devolucionFianzaContratoRenunciado;

    @Column(name = "GASTOS_CONTRATO")
    protected Double gastosContrato;

    @Column(name = "TOTAL_LIQUIDACION")
    protected Double totalLiquidacion;

    @Lob
    @Column(name = "DETALLE")
    protected String detalle;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ESCANEO_INGRESO_LIQUIDACION_ID")
    protected ArchivoAdjunto escaneoIngresoLiquidacion;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONTRATO_INQUILINO_ID")
    protected ContratoInquilino contratoInquilino;

    @Column(name = "OTROS_CONCEPTOS")
    protected Double otrosConceptos;

    @Column(name = "CANTIDADES_ENTREGADAS_EN_RESERVA")
    protected Double cantidadesEntregadasEnReserva;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_INGRESO_ITP")
    protected Date fechaIngresoITP;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ESCANEO_RESGUARDO_ITP_ID")
    protected ArchivoAdjunto escaneoResguardoITP;

    public void setFechaIngresoITP(Date fechaIngresoITP) {
        this.fechaIngresoITP = fechaIngresoITP;
    }

    public Date getFechaIngresoITP() {
        return fechaIngresoITP;
    }

    public ArchivoAdjunto getEscaneoLiquidacion() {
        return escaneoLiquidacion;
    }

    public void setEscaneoLiquidacion(ArchivoAdjunto escaneoLiquidacion) {
        this.escaneoLiquidacion = escaneoLiquidacion;
    }

    public Double getRecibosACuenta() {
        return recibosACuenta;
    }

    public void setRecibosACuenta(Double recibosACuenta) {
        this.recibosACuenta = recibosACuenta;
    }

    public ArchivoAdjunto getEscaneoResguardoITP() {
        return escaneoResguardoITP;
    }

    public void setEscaneoResguardoITP(ArchivoAdjunto escaneoResguardoITP) {
        this.escaneoResguardoITP = escaneoResguardoITP;
    }

    public Double getCantidadesEntregadasEnReserva() {
        return cantidadesEntregadasEnReserva;
    }

    public void setCantidadesEntregadasEnReserva(Double cantidadesEntregadasEnReserva) {
        this.cantidadesEntregadasEnReserva = cantidadesEntregadasEnReserva;
    }

    public Double getOtrosConceptos() {
        return otrosConceptos;
    }

    public void setOtrosConceptos(Double otrosConceptos) {
        this.otrosConceptos = otrosConceptos;
    }

    public ContratoInquilino getContratoInquilino() {
        return contratoInquilino;
    }

    public void setContratoInquilino(ContratoInquilino contratoInquilino) {
        this.contratoInquilino = contratoInquilino;
    }

    public ArchivoAdjunto getEscaneoIngresoLiquidacion() {
        return escaneoIngresoLiquidacion;
    }

    public void setEscaneoIngresoLiquidacion(ArchivoAdjunto escaneoIngresoLiquidacion) {
        this.escaneoIngresoLiquidacion = escaneoIngresoLiquidacion;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public Double getTotalLiquidacion() {
        return totalLiquidacion;
    }

    public void setTotalLiquidacion(Double totalLiquidacion) {
        this.totalLiquidacion = totalLiquidacion;
    }

    public Double getGastosContrato() {
        return gastosContrato;
    }

    public void setGastosContrato(Double gastosContrato) {
        this.gastosContrato = gastosContrato;
    }

    public Double getDevolucionFianzaContratoRenunciado() {
        return devolucionFianzaContratoRenunciado;
    }

    public void setDevolucionFianzaContratoRenunciado(Double devolucionFianzaContratoRenunciado) {
        this.devolucionFianzaContratoRenunciado = devolucionFianzaContratoRenunciado;
    }

    public String getDescDevolucionFianzaContratoRenunciado() {
        return descDevolucionFianzaContratoRenunciado;
    }

    public void setDescDevolucionFianzaContratoRenunciado(String descDevolucionFianzaContratoRenunciado) {
        this.descDevolucionFianzaContratoRenunciado = descDevolucionFianzaContratoRenunciado;
    }

    public String getDescRecibosACuenta() {
        return descRecibosACuenta;
    }

    public void setDescRecibosACuenta(String descRecibosACuenta) {
        this.descRecibosACuenta = descRecibosACuenta;
    }

    public Double getGarantiasEnEfectivo() {
        return garantiasEnEfectivo;
    }

    public void setGarantiasEnEfectivo(Double garantiasEnEfectivo) {
        this.garantiasEnEfectivo = garantiasEnEfectivo;
    }

    public Double getFianzaContrato() {
        return fianzaContrato;
    }

    public void setFianzaContrato(Double fianzaContrato) {
        this.fianzaContrato = fianzaContrato;
    }

    public Boolean getEsRenovacion() {
        if (esRenovacion==null){
            esRenovacion = false;
        }
        return esRenovacion;
    }

    public void setEsRenovacion(Boolean esRenovacion) {
        this.esRenovacion = esRenovacion;
    }
}