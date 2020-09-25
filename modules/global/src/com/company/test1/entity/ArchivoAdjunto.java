package com.company.test1.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.Listeners;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.entity.annotation.PublishEntityChangedEvents;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;

@NamePattern("%s|nombreArchivo")
@PublishEntityChangedEvents
@Table(name = "ARCHIVO_ADJUNTO")
@Entity(name = "test1_ArchivoAdjunto")
@Listeners("test1_ArchivoAdjuntoEntityListener")
public class ArchivoAdjunto extends StandardEntity {
    private static final long serialVersionUID = -3747978738368806154L;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COLECCION_ARCHIVOS_ADJUNTOS_ID")
    protected ColeccionArchivosAdjuntos coleccionArchivosAdjuntos;

    @Column(name = "EXT_ID")
    protected Integer extId;

    @Column(name = "REPRESENTACION_SERIAL")
    protected byte[] representacionSerial;

    @Column(name = "NOMBRE_ARCHIVO")
    protected String nombreArchivo;

    @Column(name = "NOMBRE_ARCHIVO_ORIGINAL")
    protected String nombreArchivoOriginal;

    @Column(name = "DESCRIPCION")
    protected String descripcion;

    @Column(name = "EXTENSION", length = 50)
    protected String extension;

    @Column(name = "MIME_TYPE", length = 100)
    protected String mimeType;

    @Column(name = "TAMANO")
    protected Integer tamano;

    public byte[] getRepresentacionSerial() {
        return representacionSerial;
    }

    public void setRepresentacionSerial(byte[] representacionSerial) {
        this.representacionSerial = representacionSerial;
    }

    public Integer getExtId() {
        return extId;
    }

    public void setExtId(Integer extId) {
        this.extId = extId;
    }

    public Integer getTamano() {
        return tamano;
    }

    public void setTamano(Integer tamano) {
        this.tamano = tamano;
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

    public ColeccionArchivosAdjuntos getColeccionArchivosAdjuntos() {
        return coleccionArchivosAdjuntos;
    }

    public void setColeccionArchivosAdjuntos(ColeccionArchivosAdjuntos coleccionArchivosAdjuntos) {
        this.coleccionArchivosAdjuntos = coleccionArchivosAdjuntos;
    }

    public String toString(){
        return this.nombreArchivo;
    }
}