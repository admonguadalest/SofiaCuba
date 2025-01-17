package com.company.test1.entity.documentosImputables;

import com.company.test1.entity.ColeccionArchivosAdjuntos;
import com.company.test1.entity.ciclos.ImputacionDocumentoImputable;
import com.company.test1.entity.conceptosadicionales.RegistroAplicacionConceptoAdicional;
import com.company.test1.entity.enums.DocumentoImputableTipoEnum;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.chile.core.annotations.NumberFormat;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Table(name = "DOCUMENTO_IMPUTABLE")
@Entity(name = "test1_DocumentoImputable")
public class DocumentoImputable extends StandardEntity {
    private static final long serialVersionUID = -1305475386597244645L;

    @NotNull(message = "Aportar Descripcion")
    @Lob
    @Column(name = "DESCRIPCION_DOCUMENTO")
    protected String descripcionDocumento;

    @Column(name = "MARCAJES_PROVISIONAL")
    protected String marcajesProvisional;

    @Transient
    @MetaProperty
    protected String textoDescriptivoDocumento;

    @NotNull(message = "Aportar Fecha de Emision de Documento")
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_EMISION")
    protected Date fechaEmision;

    @NotNull(message = "Especificar Importe")
    @NumberFormat(pattern = "#,##0.00", decimalSeparator = ",", groupingSeparator = ".")
    @Column(name = "IMPORTE_TOTAL_BASE")
    protected Double importeTotalBase;

    @NotNull(message = "Especificar Importe PostCCAA")
    @NumberFormat(pattern = "#,##0.00", decimalSeparator = ",", groupingSeparator = ".")
    @Column(name = "IMPORTE_POST_CCAA")
    protected Double importePostCCAA;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "documentoImputable")
    protected List<ImputacionDocumentoImputable> imputacionesDocumentoImputable = new ArrayList<ImputacionDocumentoImputable>();

    @NotNull(message = "Aportar Numero de Documento")
    @Column(name = "NUM_DOCUMENTO", length = 100)
    protected String numDocumento;

    @Lob
    @Column(name = "OBSERVACIONES")
    protected String observaciones;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COLECCION_ARCHIVOS_ADJUNTOS_ID")
    protected ColeccionArchivosAdjuntos coleccionArchivosAdjuntos = null;

    @NotNull
    @Column(name = "ARCHIVADO", nullable = false)
    protected Boolean archivado = false;

    @NotNull
    @Column(name = "OMITIR_CONTROL_DE_ORDEN_PAGO", nullable = false)
    protected Boolean omitirControlDeOrdenPago = false;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "documentoImputable")
    protected List<RegistroAplicacionConceptoAdicional> registroAplicacionConceptosAdicionales = new ArrayList<RegistroAplicacionConceptoAdicional>();

    @Column(name = "RM2ID")
    protected Integer rm2id;

    @NotNull(message = "Especificar Tipo de Documento Imputable")
    @Column(name = "TIPO_ENUM")
    protected String tipoEnum;

    public String getMarcajesProvisional() {
        return marcajesProvisional;
    }

    public void setMarcajesProvisional(String marcajesProvisional) {
        this.marcajesProvisional = marcajesProvisional;
    }

    //campos relativos a suministros e imputaciones
    @Temporal(TemporalType.DATE)
    @Column(name = "SUMINISTRO_PERIODO_DESDE")
    protected Date suministroPeriodoDesde;

    @Temporal(TemporalType.DATE)
    @Column(name = "SUMINISTRO_PERIODO_HASTA")
    protected Date suministroPeriodoHasta;
    @Column(name = "SUMINISTRO_INFO_PUNTO_SUMINISTRO")
    protected String suministroInfoPuntoSuministro;



    //fin campos relativos a suministros e imputaciones

    public String getSuministroInfoPuntoSuministro() {
        return suministroInfoPuntoSuministro;
    }

    public void setSuministroInfoPuntoSuministro(String suministroInfoPuntoSuministro) {
        this.suministroInfoPuntoSuministro = suministroInfoPuntoSuministro;
    }

    public Date getSuministroPeriodoDesde() {
        return suministroPeriodoDesde;
    }

    public void setSuministroPeriodoDesde(Date suministroPeriodoDesde) {
        this.suministroPeriodoDesde = suministroPeriodoDesde;
    }

    public Date getSuministroPeriodoHasta() {
        return suministroPeriodoHasta;
    }

    public void setSuministroPeriodoHasta(Date suministroPeriodoHasta) {
        this.suministroPeriodoHasta = suministroPeriodoHasta;
    }

    @Transient
    @MetaProperty
    public String getNombreProveedor() {
        if (this instanceof DocumentoProveedor){
            DocumentoProveedor dp = (DocumentoProveedor) this;

            return dp.getProveedor().getPersona().getNombreCompleto();
        }
        return "N/D";
    }

    public DocumentoImputableTipoEnum getTipoEnum() {
        return tipoEnum == null ? null : DocumentoImputableTipoEnum.fromId(tipoEnum);
    }

    public void setTipoEnum(DocumentoImputableTipoEnum tipoEnum) {
        this.tipoEnum = tipoEnum == null ? null : tipoEnum.getId();
    }

    @Transient
    @MetaProperty
    public String getTextoImputaciones() {
        String texto = "";
        for (int i = 0; i < this.imputacionesDocumentoImputable.size(); i++) {
            if (i > 0){
                texto += " / ";
            }
            ImputacionDocumentoImputable idi = imputacionesDocumentoImputable.get(i);
            String evento = "";
            try{
                evento = idi.getEvento().getNombre();
            }catch(Exception exc){

            }

            String depto = idi.getCiclo().getDepartamento().getNombreDescriptivoCompleto();
            texto += depto + " " + evento;
        }
        return texto;
    }

    @Transient
    @MetaProperty
    public String getTipo() {
        if (this instanceof FacturaProveedor){
            return "FP";
        }
        if (this instanceof Presupuesto){
            return "P";
        }
        return null;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public Integer getRm2id() {
        return rm2id;
    }

    public void setRm2id(Integer rm2id) {
        this.rm2id = rm2id;
    }

    public List<ImputacionDocumentoImputable> getImputacionesDocumentoImputable() {
        return imputacionesDocumentoImputable;
    }

    public void setImputacionesDocumentoImputable(List<ImputacionDocumentoImputable> imputacionesDocumentoImputable) {
        this.imputacionesDocumentoImputable = imputacionesDocumentoImputable;
    }

    public List<RegistroAplicacionConceptoAdicional> getRegistroAplicacionConceptosAdicionales() {
        return registroAplicacionConceptosAdicionales;
    }

    public void setRegistroAplicacionConceptosAdicionales(List<RegistroAplicacionConceptoAdicional> registroAplicacionConceptosAdicionales) {
        this.registroAplicacionConceptosAdicionales = registroAplicacionConceptosAdicionales;
    }

    public String getTextoDescriptivoDocumento() {
        SimpleDateFormat sdf = new SimpleDateFormat("#,##0.00");
        try {
            if (this instanceof FacturaProveedor) {
                FacturaProveedor fp = (FacturaProveedor) this;
                return "Factura " + fp.getProveedor().getNombreComercial() + " " + this.getNumDocumento() + " " + sdf.format(this.getFechaEmision());
            }
            if (this instanceof Presupuesto) {
                Presupuesto fp = (Presupuesto) this;
                return "Ppto " + fp.getProveedor().getNombreComercial() + " " + this.getNumDocumento() + " " + sdf.format(this.getFechaEmision().toString());
            }
        }catch(Exception ex){
            return "N/D";
        }
        return null;
    }

    public void setTextoDescriptivoDocumento(String textoDescriptivoDocumento) {
        this.textoDescriptivoDocumento = textoDescriptivoDocumento;
    }

    public Boolean getOmitirControlDeOrdenPago() {
        return omitirControlDeOrdenPago;
    }

    public void setOmitirControlDeOrdenPago(Boolean omitirControlDeOrdenPago) {
        this.omitirControlDeOrdenPago = omitirControlDeOrdenPago;
    }

    public Boolean getArchivado() {
        return archivado;
    }

    public void setArchivado(Boolean archivado) {
        this.archivado = archivado;
    }

    public ColeccionArchivosAdjuntos getColeccionArchivosAdjuntos() {
        return coleccionArchivosAdjuntos;
    }

    public void setColeccionArchivosAdjuntos(ColeccionArchivosAdjuntos coleccionArchivosAdjuntos) {
        this.coleccionArchivosAdjuntos = coleccionArchivosAdjuntos;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getNumDocumento() {
        return numDocumento;
    }

    public void setNumDocumento(String numDocumento) {
        this.numDocumento = numDocumento;
    }

    public Double getImportePostCCAA() {
        return importePostCCAA;
    }

    public void setImportePostCCAA(Double importePostCCAA) {
        this.importePostCCAA = importePostCCAA;
    }

    public Double getImporteTotalBase() {
        return importeTotalBase;
    }

    public void setImporteTotalBase(Double importeTotalBase) {
        this.importeTotalBase = importeTotalBase;
    }

    public String getDescripcionDocumento() {
        return descripcionDocumento;
    }

    public void setDescripcionDocumento(String descripcionDocumento) {
        this.descripcionDocumento = descripcionDocumento;
    }
}