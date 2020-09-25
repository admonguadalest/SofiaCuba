package com.company.test1.entity.departamentos;

import com.company.test1.entity.ArchivoAdjunto;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Table(name = "CERTIFICADO_CALIFICACION_ENERGETICA")
@Entity(name = "test1_CertificadoCalificacionEnergetica")
public class CertificadoCalificacionEnergetica extends StandardEntity {
    private static final long serialVersionUID = 6440254325263918927L;

    @NotNull(message = "Aportar numero de registro")
    @Column(name = "NUMERO_REGISTRO")
    protected String numeroRegistro;

    @NotNull(message = "Aportar escaneo de Certificado")
    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ARCHIVO_ADJUNTO_ID")
    protected ArchivoAdjunto archivoAdjunto;

    @NotNull(message = "Indicar Fecha de Vencimiento")
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_VENCIMIENTO")
    protected Date fechaVencimiento;

    @Lob
    @Column(name = "OBSERVACIONES")
    protected String observaciones;

    @NotNull(message = "Aportar Departamento")
    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEPARTAMENTO_ID")
    protected Departamento departamento;

    @NotNull(message = "Indicar Calificacion de Emisiones")
    @Column(name = "CALIFICACION_EMISIONES", length = 5)
    protected String calificacionEmisiones;

    @NotNull(message = "Indicar Calificacion")
    @Column(name = "CALIFICACION_CONSUMO_ENERGETICO", length = 5)
    protected String calificacionConsumoEnergetico;

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public String getCalificacionConsumoEnergetico() {
        return calificacionConsumoEnergetico;
    }

    public void setCalificacionConsumoEnergetico(String calificacionConsumoEnergetico) {
        this.calificacionConsumoEnergetico = calificacionConsumoEnergetico;
    }

    public String getCalificacionEmisiones() {
        return calificacionEmisiones;
    }

    public void setCalificacionEmisiones(String calificacionEmisiones) {
        this.calificacionEmisiones = calificacionEmisiones;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public ArchivoAdjunto getArchivoAdjunto() {
        return archivoAdjunto;
    }

    public void setArchivoAdjunto(ArchivoAdjunto archivoAdjunto) {
        this.archivoAdjunto = archivoAdjunto;
    }

    public String getNumeroRegistro() {
        return numeroRegistro;
    }

    public void setNumeroRegistro(String numeroRegistro) {
        this.numeroRegistro = numeroRegistro;
    }
}