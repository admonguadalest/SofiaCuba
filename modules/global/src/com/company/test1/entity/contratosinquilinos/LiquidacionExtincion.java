package com.company.test1.entity.contratosinquilinos;

import com.company.test1.entity.ArchivoAdjunto;
import com.company.test1.entity.documentosfotograficos.CarpetaDocumentosFotograficos;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import java.util.Date;

@Table(name = "LIQUIDACION_EXTINCION")
@Entity(name = "test1_LiquidacionExtincion")
public class LiquidacionExtincion extends StandardEntity {
    private static final long serialVersionUID = 6286451616868256876L;

    @Column(name = "TOTALES_GARANTIAS")
    protected Double totalesGarantias;

    @Column(name = "TOTALES_RECIBOS_PENDIENTES")
    protected Double totalesRecibosPendientes;

    @Column(name = "TOTALES_INDEMNIZACIONES")
    protected Double totalesIndemnizaciones;

    @Column(name = "POR_LIQUIDAR")
    protected Double porLiquidar;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ESCANEO_LIQUIDACION_ID")
    protected ArchivoAdjunto escaneoLiquidacion;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONTRATO_INQUILINO_ID")
    protected ContratoInquilino contratoInquilino;

    @Lob
    @Column(name = "DETALLE")
    protected String detalle;

    @Column(name = "IMPORTE_AVAL_EJECUTADO")
    protected Double importeAvalEjecutado;

    @Column(name = "CANTIDADES_ENTREGADAS_EN_RESERVA")
    protected Double cantidadesEntregadasEnReserva;

    @Column(name = "CONFORMIDAD_ADMINISTRADOR")
    protected Boolean conformidadAdministrador;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.UNLINK)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RETENCION_CDF_ID")
    protected CarpetaDocumentosFotograficos retencionCarpetaDocumentoFotografico;

    @Column(name = "CANTIDADES_A_CUENTA_LIQUIDACION")
    protected Double cantidadesACuentaLiquidacion;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_CANTIDADES_A_CUENTA_LIQUIDACION")
    protected Date fechaCantidadesACuentaLiquidacion;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_LIQUIDACION")
    protected Date fechaLiquidacion;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "liquidacionExtincion")
    protected CarpetaDocumentosFotograficos carpetaDocumentosFotograficos;

    public void setFechaCantidadesACuentaLiquidacion(Date fechaCantidadesACuentaLiquidacion) {
        this.fechaCantidadesACuentaLiquidacion = fechaCantidadesACuentaLiquidacion;
    }

    public Date getFechaCantidadesACuentaLiquidacion() {
        return fechaCantidadesACuentaLiquidacion;
    }

    public void setFechaLiquidacion(Date fechaLiquidacion) {
        this.fechaLiquidacion = fechaLiquidacion;
    }

    public Date getFechaLiquidacion() {
        return fechaLiquidacion;
    }

    public LiquidacionExtincion(ContratoInquilino contratoInquilino) {
        this.contratoInquilino = contratoInquilino;
    }

    public LiquidacionExtincion() {
    }

    public CarpetaDocumentosFotograficos getCarpetaDocumentosFotograficos() {
        return carpetaDocumentosFotograficos;
    }

    public void setCarpetaDocumentosFotograficos(CarpetaDocumentosFotograficos carpetaDocumentosFotograficos) {
        this.carpetaDocumentosFotograficos = carpetaDocumentosFotograficos;
    }

    public Double getCantidadesACuentaLiquidacion() {
        return cantidadesACuentaLiquidacion;
    }

    public void setCantidadesACuentaLiquidacion(Double cantidadesACuentaLiquidacion) {
        this.cantidadesACuentaLiquidacion = cantidadesACuentaLiquidacion;
    }

    public CarpetaDocumentosFotograficos getRetencionCarpetaDocumentoFotografico() {
        return retencionCarpetaDocumentoFotografico;
    }

    public void setRetencionCarpetaDocumentoFotografico(CarpetaDocumentosFotograficos retencionCarpetaDocumentoFotografico) {
        this.retencionCarpetaDocumentoFotografico = retencionCarpetaDocumentoFotografico;
    }

    public Boolean getConformidadAdministrador() {
        return conformidadAdministrador;
    }

    public void setConformidadAdministrador(Boolean conformidadAdministrador) {
        this.conformidadAdministrador = conformidadAdministrador;
    }

    public Double getCantidadesEntregadasEnReserva() {
        return cantidadesEntregadasEnReserva;
    }

    public void setCantidadesEntregadasEnReserva(Double cantidadesEntregadasEnReserva) {
        this.cantidadesEntregadasEnReserva = cantidadesEntregadasEnReserva;
    }

    public Double getImporteAvalEjecutado() {
        return importeAvalEjecutado;
    }

    public void setImporteAvalEjecutado(Double importeAvalEjecutado) {
        this.importeAvalEjecutado = importeAvalEjecutado;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public ContratoInquilino getContratoInquilino() {
        return contratoInquilino;
    }

    public void setContratoInquilino(ContratoInquilino contratoInquilino) {
        this.contratoInquilino = contratoInquilino;
    }

    public ArchivoAdjunto getEscaneoLiquidacion() {
        return escaneoLiquidacion;
    }

    public void setEscaneoLiquidacion(ArchivoAdjunto escaneoLiquidacion) {
        this.escaneoLiquidacion = escaneoLiquidacion;
    }

    public Double getPorLiquidar() {
        return porLiquidar;
    }

    public void setPorLiquidar(Double porLiquidar) {
        this.porLiquidar = porLiquidar;
    }

    public Double getTotalesIndemnizaciones() {
        return totalesIndemnizaciones;
    }

    public void setTotalesIndemnizaciones(Double totalesIndemnizaciones) {
        this.totalesIndemnizaciones = totalesIndemnizaciones;
    }

    public Double getTotalesRecibosPendientes() {
        return totalesRecibosPendientes;
    }

    public void setTotalesRecibosPendientes(Double totalesRecibosPendientes) {
        this.totalesRecibosPendientes = totalesRecibosPendientes;
    }

    public Double getTotalesGarantias() {
        return totalesGarantias;
    }

    public void setTotalesGarantias(Double totalesGarantias) {
        this.totalesGarantias = totalesGarantias;
    }
}