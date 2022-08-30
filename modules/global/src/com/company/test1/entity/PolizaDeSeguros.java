package com.company.test1.entity;

import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;
import java.util.Date;

@Table(name = "TEST1_POLIZA_DE_SEGUROS")
@Entity(name = "test1_PolizaDeSeguros")
public class PolizaDeSeguros extends StandardEntity {
    private static final long serialVersionUID = 4010060840003333771L;

    @Column(name = "IDENTIFICADOR_POLIZA")
    private String identificadorPoliza;

    @Column(name = "DESCRIPCION_ABREVIADA_RIESGO")
    private String descripcionAbreviadaRiesgo;

    @Lob
    @Column(name = "DESCRIPCION_AMPLIADA_RIESGO")
    private String descripcionAmpliadaRiesgo;

    @Temporal(TemporalType.DATE)
    @Column(name = "PRIMERA_FECHA_INICIAL")
    private Date primeraFechaInicial;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_RESCISION")
    private Date fechaRescision;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ESCANEO_POLIZA_ID")
    private ArchivoAdjunto escaneoPoliza;

    @Lob
    @Column(name = "DATOS_DE_CONTACTO_BROKER_Y_CIA")
    private String datosDeContactoBrokerYCia;

    @Column(name = "COMPANIA_ASEGURADORA")
    private String companiaAseguradora;

    @Column(name = "BROKER")
    private String broker;

    @MetaProperty
    public String getNombreDescriptivoCompleto() {
        return this.companiaAseguradora + " (" + this.broker + ") " + this.identificadorPoliza;
    }

    public String getDatosDeContactoBrokerYCia() {
        return datosDeContactoBrokerYCia;
    }

    public void setDatosDeContactoBrokerYCia(String datosDeContactoBrokerYCia) {
        this.datosDeContactoBrokerYCia = datosDeContactoBrokerYCia;
    }

    public String getBroker() {
        return broker;
    }

    public void setBroker(String broker) {
        this.broker = broker;
    }

    public String getCompaniaAseguradora() {
        return companiaAseguradora;
    }

    public void setCompaniaAseguradora(String companiaAseguradora) {
        this.companiaAseguradora = companiaAseguradora;
    }

    public ArchivoAdjunto getEscaneoPoliza() {
        return escaneoPoliza;
    }

    public void setEscaneoPoliza(ArchivoAdjunto escaneoPoliza) {
        this.escaneoPoliza = escaneoPoliza;
    }

    public Date getFechaRescision() {
        return fechaRescision;
    }

    public void setFechaRescision(Date fechaRescision) {
        this.fechaRescision = fechaRescision;
    }

    public Date getPrimeraFechaInicial() {
        return primeraFechaInicial;
    }

    public void setPrimeraFechaInicial(Date primeraFechaInicial) {
        this.primeraFechaInicial = primeraFechaInicial;
    }

    public String getDescripcionAmpliadaRiesgo() {
        return descripcionAmpliadaRiesgo;
    }

    public void setDescripcionAmpliadaRiesgo(String descripcionAmpliadaRiesgo) {
        this.descripcionAmpliadaRiesgo = descripcionAmpliadaRiesgo;
    }

    public String getDescripcionAbreviadaRiesgo() {
        return descripcionAbreviadaRiesgo;
    }

    public void setDescripcionAbreviadaRiesgo(String descripcionAbreviadaRiesgo) {
        this.descripcionAbreviadaRiesgo = descripcionAbreviadaRiesgo;
    }

    public String getIdentificadorPoliza() {
        return identificadorPoliza;
    }

    public void setIdentificadorPoliza(String identificadorPoliza) {
        this.identificadorPoliza = identificadorPoliza;
    }
}