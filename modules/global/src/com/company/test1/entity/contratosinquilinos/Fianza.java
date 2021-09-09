package com.company.test1.entity.contratosinquilinos;

import com.company.test1.entity.ArchivoAdjunto;
import com.company.test1.entity.enums.EstadoFianzaEnum;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Table(name = "FIANZA")
@Entity(name = "test1_Fianza")
public class Fianza extends StandardEntity {
    private static final long serialVersionUID = 2096904747475574521L;

    public Fianza() {
    }

    public Fianza(ContratoInquilino contratoInquilino) {
        this.contratoInquilino = contratoInquilino;

    }

    @NotNull(message = "Asociar Contrato")
    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONTRATO_INQUILINO_ID")
    protected ContratoInquilino contratoInquilino;

    @NotNull(message = "Especificar si la Fianza es o no un Aval Bancario")
    @Column(name = "ES_AVAL_BANCARIO")
    protected Boolean esAvalBancario = false;

    @NotNull(message = "Aportar fecha de Ingreso de la Fianza")
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_ABONO_FIANZA")
    protected Date fechaAbonoFianza;

    @NotNull(message = "Indicar importe de la Fianza Complementaria")
    @Column(name = "FIANZA_COMPLEMENTARIA")
    protected Double fianzaComplementaria;

    @NotNull(message = "Indicar importe de Fianza Legal")
    @Column(name = "FIANZA_LEGAL")
    protected Double fianzaLegal;

    @Column(name = "IDENTIFICADOR_AVAL")
    protected String identificadorAval;

    @Lob
    @Column(name = "OBSERVACIONES")
    protected String observaciones;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ESCANEO_ARCHIVO_ADJUNTO_ID")
    protected ArchivoAdjunto escaneoArchivoAdjunto;

    @Column(name = "ESTADO_FIANZA")
    protected Integer estadoFianza;

    @Column(name = "TIENE_POLIZA_ALQUILER")
    protected Boolean tienePolizaAlquiler = false;

    @Column(name = "NUMERO_POLIZA")
    protected String numeroPoliza;

    @Column(name = "INFORMACION_DE_CONTACTO_POLIZA")
    protected String informacionDeContactoPoliza;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ESCANEO_SEGURO_ARCHIVO_ADJUNTO_ID")
    protected ArchivoAdjunto escaneoSeguroArchivoAdjunto;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_INGRESO_FIANZA_EN_CAMARA")
    protected Date fechaIngresoFianzaEnCamara;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_RESCATE_FIANZA_DE_CAMARA")
    protected Date fechaRescateFianzaDeCamara;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ESCANEO_FIANZA_ID")
    protected ArchivoAdjunto escaneoFianza;

    @Transient
    @MetaProperty
    public String getTipoGarantiaNombre() {
        if (this.esAvalBancario){
            return "Aval Bancario";
        }
        if (this.tienePolizaAlquiler){
            return "Poliza de Seguro de Alquiler";
        }
        return "Efectivo";
    }

    @Transient
    @MetaProperty
    public String getEstadoFianzaNombre() {
        if (this.getEstadoFianza()==null){
            return "No Establecido (NULL)";
        }
        return this.getEstadoFianza().name();
    }

    public void setFechaAbonoFianza(Date fechaAbonoFianza) {
        this.fechaAbonoFianza = fechaAbonoFianza;
    }

    public Date getFechaAbonoFianza() {
        return fechaAbonoFianza;
    }

    public void setFechaIngresoFianzaEnCamara(Date fechaIngresoFianzaEnCamara) {
        this.fechaIngresoFianzaEnCamara = fechaIngresoFianzaEnCamara;
    }

    public Date getFechaIngresoFianzaEnCamara() {
        return fechaIngresoFianzaEnCamara;
    }

    public void setFechaRescateFianzaDeCamara(Date fechaRescateFianzaDeCamara) {
        this.fechaRescateFianzaDeCamara = fechaRescateFianzaDeCamara;
    }

    public Date getFechaRescateFianzaDeCamara() {
        return fechaRescateFianzaDeCamara;
    }

    public ArchivoAdjunto getEscaneoFianza() {
        return escaneoFianza;
    }

    public void setEscaneoFianza(ArchivoAdjunto escaneoFianza) {
        this.escaneoFianza = escaneoFianza;
    }

    public ArchivoAdjunto getEscaneoSeguroArchivoAdjunto() {
        return escaneoSeguroArchivoAdjunto;
    }

    public void setEscaneoSeguroArchivoAdjunto(ArchivoAdjunto escaneoSeguroArchivoAdjunto) {
        this.escaneoSeguroArchivoAdjunto = escaneoSeguroArchivoAdjunto;
    }

    public String getInformacionDeContactoPoliza() {
        return informacionDeContactoPoliza;
    }

    public void setInformacionDeContactoPoliza(String informacionDeContactoPoliza) {
        this.informacionDeContactoPoliza = informacionDeContactoPoliza;
    }

    public String getNumeroPoliza() {
        return numeroPoliza;
    }

    public void setNumeroPoliza(String numeroPoliza) {
        this.numeroPoliza = numeroPoliza;
    }

    public Boolean getTienePolizaAlquiler() {
        return tienePolizaAlquiler;
    }

    public void setTienePolizaAlquiler(Boolean tienePolizaAlquiler) {
        this.tienePolizaAlquiler = tienePolizaAlquiler;
    }

    public EstadoFianzaEnum getEstadoFianza() {
        return estadoFianza == null ? null : EstadoFianzaEnum.fromId(estadoFianza);
    }

    public void setEstadoFianza(EstadoFianzaEnum estadoFianza) {
        this.estadoFianza = estadoFianza == null ? null : estadoFianza.getId();
    }

    public ArchivoAdjunto getEscaneoArchivoAdjunto() {
        return escaneoArchivoAdjunto;
    }

    public void setEscaneoArchivoAdjunto(ArchivoAdjunto escaneoArchivoAdjunto) {
        this.escaneoArchivoAdjunto = escaneoArchivoAdjunto;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getIdentificadorAval() {
        return identificadorAval;
    }

    public void setIdentificadorAval(String identificadorAval) {
        this.identificadorAval = identificadorAval;
    }

    public Double getFianzaLegal() {
        return fianzaLegal;
    }

    public void setFianzaLegal(Double fianzaLegal) {
        this.fianzaLegal = fianzaLegal;
    }

    public Double getFianzaComplementaria() {
        return fianzaComplementaria;
    }

    public void setFianzaComplementaria(Double fianzaComplementaria) {
        this.fianzaComplementaria = fianzaComplementaria;
    }

    public Boolean getEsAvalBancario() {
        return esAvalBancario;
    }

    public void setEsAvalBancario(Boolean esAvalBancario) {
        this.esAvalBancario = esAvalBancario;
    }

    public ContratoInquilino getContratoInquilino() {
        return contratoInquilino;
    }

    public void setContratoInquilino(ContratoInquilino contratoInquilino) {
        this.contratoInquilino = contratoInquilino;
    }
}