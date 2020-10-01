package com.company.test1.entity.conceptosadicionales;

import com.company.test1.entity.documentosImputables.DocumentoImputable;
import com.company.test1.entity.recibos.ImplementacionConcepto;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Table(name = "RTRO_APP_CONCEPTO_ADICIONAL")
@Entity(name = "test1_RegistroAplicacionConceptoAdicional")
public class RegistroAplicacionConceptoAdicional extends StandardEntity {
    private static final long serialVersionUID = 7085195863135938262L;

    @NotNull(message = "Especificar Concepto Adicional")
    @OnDeleteInverse(DeletePolicy.DENY)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONCEPTO_ADICIONAL_ID")
    protected ConceptoAdicional conceptoAdicional;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DOCUMENTO_IMPUTABLE_ID")
    protected DocumentoImputable documentoImputable;

    @NotNull(message = "Especificar Nif/Dni")
    @Column(name = "NIF_DNI", length = 50)
    protected String nifDni;

    @NotNull(message = "Introducir Fecha de Valor")
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_VALOR")
    protected Date fechaValor;

    @NotNull(message = "Inserir Número de Documento")
    @Column(name = "NUM_DOCUMENTO", length = 100)
    protected String numDocumento;

    @NotNull(message = "Especificar Base")
    @Column(name = "BASE_")
    protected Double base;

    @NotNull(message = "Especificar Porcentaje")
    @Column(name = "PORCENTAJE")
    protected Double porcentaje;

    @NotNull(message = "Indicar Importe Aplicado")
    @Column(name = "IMPORTE_APLICADO")
    protected Double importeAplicado;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IMPLEMENTACION_CONCEPTO_ID")
    protected ImplementacionConcepto implementacionConcepto;

    public void setFechaValor(Date fechaValor) {
        this.fechaValor = fechaValor;
    }

    public Date getFechaValor() {
        return fechaValor;
    }

    public ImplementacionConcepto getImplementacionConcepto() {
        return implementacionConcepto;
    }

    public void setImplementacionConcepto(ImplementacionConcepto implementacionConcepto) {
        this.implementacionConcepto = implementacionConcepto;
    }

    public Double getImporteAplicado() {
        return importeAplicado;
    }

    public void setImporteAplicado(Double importeAplicado) {
        this.importeAplicado = importeAplicado;
    }

    public Double getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(Double porcentaje) {
        this.porcentaje = porcentaje;
    }

    public Double getBase() {
        return base;
    }

    public void setBase(Double base) {
        this.base = base;
    }

    public String getNumDocumento() {
        return numDocumento;
    }

    public void setNumDocumento(String numDocumento) {
        this.numDocumento = numDocumento;
    }

    public String getNifDni() {
        return nifDni;
    }

    public void setNifDni(String nifDni) {
        this.nifDni = nifDni;
    }

    public DocumentoImputable getDocumentoImputable() {
        return documentoImputable;
    }

    public void setDocumentoImputable(DocumentoImputable documentoImputable) {
        this.documentoImputable = documentoImputable;
    }

    public ConceptoAdicional getConceptoAdicional() {
        return conceptoAdicional;
    }

    public void setConceptoAdicional(ConceptoAdicional conceptoAdicional) {
        this.conceptoAdicional = conceptoAdicional;
    }
}