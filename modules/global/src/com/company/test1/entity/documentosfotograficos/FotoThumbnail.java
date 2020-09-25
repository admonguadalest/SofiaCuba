package com.company.test1.entity.documentosfotograficos;

import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.Listeners;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.entity.annotation.PublishEntityChangedEvents;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.Max;

@PublishEntityChangedEvents
@Table(name = "TEST1_FOTO_THUMBNAIL")
@Entity(name = "test1_FotoThumbnail")
@Listeners("test1_FotoThumbnailEntityListener")
public class FotoThumbnail extends StandardEntity {
    private static final long serialVersionUID = 2763898102976508650L;

    @Column(name = "TAMANO")
    protected Integer tamano;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CARPETA_DOCUMENTO_FOTOGRAFICO_ID")
    protected CarpetaDocumentosFotograficos carpetaDocumentoFotografico;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "fotoThumbnail")
    protected FotoDocumentoFotografico fotoDocumentoFotografico;

    @Column(name = "REPRESENTACION_SERIAL")
    protected byte[] representacionSerial;

    @Column(name = "EXT_ID")
    protected Integer extId;

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

    public FotoDocumentoFotografico getFotoDocumentoFotografico() {
        return fotoDocumentoFotografico;
    }

    public void setFotoDocumentoFotografico(FotoDocumentoFotografico fotoDocumentoFotografico) {
        this.fotoDocumentoFotografico = fotoDocumentoFotografico;
    }

    public CarpetaDocumentosFotograficos getCarpetaDocumentoFotografico() {
        return carpetaDocumentoFotografico;
    }

    public void setCarpetaDocumentoFotografico(CarpetaDocumentosFotograficos carpetaDocumentoFotografico) {
        this.carpetaDocumentoFotografico = carpetaDocumentoFotografico;
    }

    public Integer getTamano() {
        return tamano;
    }

    public void setTamano(Integer tamano) {
        this.tamano = tamano;
    }
}