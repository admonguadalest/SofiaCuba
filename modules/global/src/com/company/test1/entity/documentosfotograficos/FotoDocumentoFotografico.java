package com.company.test1.entity.documentosfotograficos;

import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.Listeners;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.entity.annotation.PublishEntityChangedEvents;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;

@PublishEntityChangedEvents
@Table(name = "FOTO_DOCUMENTO_FOTOGRAFICO")
@Entity(name = "test1_FotoDocumentoFotografico")
@Listeners("test1_FotoDocumentoFotograficoListener")
public class FotoDocumentoFotografico extends StandardEntity {
    private static final long serialVersionUID = -6862563716137040209L;

    @Column(name = "DESCRIPCION")
    protected String descripcion;

    @Column(name = "EXTENSION", length = 10)
    protected String extension;

    @Column(name = "MIME_TYPE", length = 100)
    protected String mimeType;

    @Column(name = "NOMBRE_ARCHIVO")
    protected String nombreArchivo;

    @Column(name = "NOMBRE_ARCHIVO_ORIGINAL")
    protected String nombreArchivoOriginal;

    @Column(name = "TAMANO")
    protected Integer tamano;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CARPETA_ID")
    protected CarpetaDocumentosFotograficos carpeta;

    @Column(name = "REPRESENTACION_SERIAL")
    protected byte[] representacionSerial;

    @Column(name = "EXT_ID")
    protected Integer extId;

    @JoinColumn(name = "FOTO_THUMBNAIL_ID")
    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToOne(fetch = FetchType.LAZY)
    protected FotoThumbnail fotoThumbnail;

    public FotoThumbnail getFotoThumbnail() {
        return fotoThumbnail;
    }

    public void setFotoThumbnail(FotoThumbnail fotoThumbnail) {
        this.fotoThumbnail = fotoThumbnail;
    }

    public Integer getExtId() {
        return extId;
    }

    public void setExtId(Integer extId) {
        this.extId = extId;
    }

    public byte[] getRepresentacionSerial() {
        return representacionSerial;
    }

    public void setRepresentacionSerial(byte[] representacionSerial) {
        this.representacionSerial = representacionSerial;
    }

    public CarpetaDocumentosFotograficos getCarpeta() {
        return carpeta;
    }

    public void setCarpeta(CarpetaDocumentosFotograficos carpeta) {
        this.carpeta = carpeta;
    }

    public Integer getTamano() {
        return tamano;
    }

    public void setTamano(Integer tamano) {
        this.tamano = tamano;
    }

    public String getNombreArchivoOriginal() {
        return nombreArchivoOriginal;
    }

    public void setNombreArchivoOriginal(String nombreArchivoOriginal) {
        this.nombreArchivoOriginal = nombreArchivoOriginal;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}