package com.company.test1.entity.ciclos;

import com.company.test1.entity.documentosImputables.DocumentoImputable;
import com.company.test1.entity.validaciones.ValidacionImputacionDocumentoImputable;
import com.haulmont.chile.core.annotations.NumberFormat;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "IMPUTACION_DOCUMENTO_IMPUTABLE")
@Entity(name = "test1_ImputacionDocumentoImputable")
public class ImputacionDocumentoImputable extends StandardEntity {
    private static final long serialVersionUID = 8185037046280398454L;

    @NotNull(message = "Aportar descripcion de Imputacion")
    @Column(name = "DESCRIPCION_IMPUTACION")
    protected String descripcionImputacion;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VALIDACION_IMPUTACION_ID")
    protected ValidacionImputacionDocumentoImputable validacionImputacion;

    @NotNull(message = "Establecer importe de Imputacion")
    @NumberFormat(pattern = "#,##0.00", decimalSeparator = ",", groupingSeparator = ".")
    @Column(name = "IMPORTE_IMPUTACION")
    protected Double importeImputacion;

    @Column(name = "IMPUTACION_INDEFINIDOS")
    protected Boolean imputacionIndefinidos;

    @Column(name = "INFORMACION_ADICIONAL")
    protected String informacionAdicional;

    @NotNull(message = "Establecer Porcentaje de Imputacion")
    @NumberFormat(pattern = "#0.00%", decimalSeparator = ",", groupingSeparator = ".")
    @Column(name = "PORCENTAJE_IMPUTACION")
    protected Double porcentajeImputacion;

    @NotNull(message = "Asignar Documento Imputable")
    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)//eliminar cascade
    @JoinColumn(name = "DOCUMENTO_IMPUTABLE_ID")
    protected DocumentoImputable documentoImputable;

    @NotNull(message = "Asignar Ciclo")
    @Lookup(type = LookupType.DROPDOWN, actions = "lookup")
    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CICLO_ID")
    protected Ciclo ciclo;

    @NotNull(message = "Asignar Evento")
    @Lookup(type = LookupType.DROPDOWN, actions = "lookup")
    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EVENTO_ID")
    protected Evento evento;

    @Column(name = "RM2ID")
    protected Integer rm2id;

    public ValidacionImputacionDocumentoImputable getValidacionImputacion() {
        return validacionImputacion;
    }

    public void setValidacionImputacion(ValidacionImputacionDocumentoImputable validacionImputacion) {
        this.validacionImputacion = validacionImputacion;
    }

    public Integer getRm2id() {
        return rm2id;
    }

    public void setRm2id(Integer rm2id) {
        this.rm2id = rm2id;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public Ciclo getCiclo() {
        return ciclo;
    }

    public void setCiclo(Ciclo ciclo) {
        this.ciclo = ciclo;
    }

    public DocumentoImputable getDocumentoImputable() {
        return documentoImputable;
    }

    public void setDocumentoImputable(DocumentoImputable documentoImputable) {
        this.documentoImputable = documentoImputable;
    }

    public Double getPorcentajeImputacion() {
        return porcentajeImputacion;
    }

    public void setPorcentajeImputacion(Double porcentajeImputacion) {
        this.porcentajeImputacion = porcentajeImputacion;
    }

    public String getInformacionAdicional() {
        return informacionAdicional;
    }

    public void setInformacionAdicional(String informacionAdicional) {
        this.informacionAdicional = informacionAdicional;
    }

    public Boolean getImputacionIndefinidos() {
        return imputacionIndefinidos;
    }

    public void setImputacionIndefinidos(Boolean imputacionIndefinidos) {
        this.imputacionIndefinidos = imputacionIndefinidos;
    }

    public Double getImporteImputacion() {
        return importeImputacion;
    }

    public void setImporteImputacion(Double importeImputacion) {
        this.importeImputacion = importeImputacion;
    }

    public String getDescripcionImputacion() {
        return descripcionImputacion;
    }

    public void setDescripcionImputacion(String descripcionImputacion) {
        this.descripcionImputacion = descripcionImputacion;
    }
}