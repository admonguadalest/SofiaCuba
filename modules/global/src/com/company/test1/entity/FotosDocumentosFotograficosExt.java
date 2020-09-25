package com.company.test1.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.BaseIntIdentityIdEntity;
import com.haulmont.cuba.core.global.DesignSupport;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@DesignSupport("{'imported':true}")
@NamePattern("%s|nombreArchivo")
@Table(name = "fotos_documentos_fotograficos")
@Entity(name = "test1_FotosDocumentosFotograficosExt")
public class FotosDocumentosFotograficosExt extends BaseIntIdentityIdEntity {
    private static final long serialVersionUID = -4689267144586004562L;
    @Column(name = "descripcion")
    protected String descripcion;
    @Column(name = "extension")
    protected String extension;
    @Column(name = "`mimeType`")
    protected String mimeType;
    @Column(name = "`nombreArchivo`")
    protected String nombreArchivo;
    @Column(name = "`nombreArchivoOriginal`")
    protected String nombreArchivoOriginal;
    @Column(name = "`representacionSerial`")
    protected byte[] representacionSerial;
    @Column(name = "tamano", nullable = false)
    protected Integer tamano;

    public Integer getTamano() {
        return tamano;
    }

    public void setTamano(Integer tamano) {
        this.tamano = tamano;
    }

    public byte[] getRepresentacionSerial() {
        return representacionSerial;
    }

    public void setRepresentacionSerial(byte[] representacionSerial) {
        this.representacionSerial = representacionSerial;
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