package com.company.test1.entity;

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;
import java.util.Date;

@Table(name = "TEST1_SINIESTRO")
@Entity(name = "test1_Siniestro")
public class Siniestro extends StandardEntity {
    private static final long serialVersionUID = -1504769541878543421L;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_SINIESTRO")
    private Date fechaSiniestro;

    @Lob
    @Column(name = "DESCRIPCION_SINIESTRO")
    private String descripcionSiniestro;

    @Lob
    @Column(name = "DATOS_DE_CONTACTO_PARTES")
    private String datosDeContactoPartes;

    @Column(name = "SINIESTRO_CERRADO")
    private Boolean siniestroCerrado;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COLECCION_ARCHIVOS_ADJUNTOS_ID")
    private ColeccionArchivosAdjuntos coleccionArchivosAdjuntos = new ColeccionArchivosAdjuntos();

    @Lob
    @Column(name = "DIARIO_DE_SINIESTRO")
    private String diarioDeSiniestro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POLIZA_DE_SEGUROS_ID")
    private PolizaDeSeguros polizaDeSeguros;

    public PolizaDeSeguros getPolizaDeSeguros() {
        return polizaDeSeguros;
    }

    public void setPolizaDeSeguros(PolizaDeSeguros polizaDeSeguros) {
        this.polizaDeSeguros = polizaDeSeguros;
    }

    public String getDiarioDeSiniestro() {
        return diarioDeSiniestro;
    }

    public void setDiarioDeSiniestro(String diarioDeSiniestro) {
        this.diarioDeSiniestro = diarioDeSiniestro;
    }

    public ColeccionArchivosAdjuntos getColeccionArchivosAdjuntos() {
        return coleccionArchivosAdjuntos;
    }

    public void setColeccionArchivosAdjuntos(ColeccionArchivosAdjuntos coleccionArchivosAdjuntos) {
        this.coleccionArchivosAdjuntos = coleccionArchivosAdjuntos;
    }

    public Boolean getSiniestroCerrado() {
        return siniestroCerrado;
    }

    public void setSiniestroCerrado(Boolean siniestroCerrado) {
        this.siniestroCerrado = siniestroCerrado;
    }

    public String getDatosDeContactoPartes() {
        return datosDeContactoPartes;
    }

    public void setDatosDeContactoPartes(String datosDeContactoPartes) {
        this.datosDeContactoPartes = datosDeContactoPartes;
    }

    public String getDescripcionSiniestro() {
        return descripcionSiniestro;
    }

    public void setDescripcionSiniestro(String descripcionSiniestro) {
        this.descripcionSiniestro = descripcionSiniestro;
    }

    public Date getFechaSiniestro() {
        return fechaSiniestro;
    }

    public void setFechaSiniestro(Date fechaSiniestro) {
        this.fechaSiniestro = fechaSiniestro;
    }
}