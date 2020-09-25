package com.company.test1.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.BaseIntIdentityIdEntity;
import com.haulmont.cuba.core.global.DesignSupport;

import javax.persistence.*;

@DesignSupport("{'imported':true}")
@NamePattern("%s|fotoDocumentoFotografico")
@Table(name = "fotos_thumbnail")
@Entity(name = "test1_FotosThumbnailExt")
public class FotosThumbnailExt extends BaseIntIdentityIdEntity {
    private static final long serialVersionUID = 66428862009491988L;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "foto_documento_fotografico_id")
    protected com.company.test1.entity.FotosDocumentosFotograficosExt fotoDocumentoFotografico;
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

    public com.company.test1.entity.FotosDocumentosFotograficosExt getFotoDocumentoFotografico() {
        return fotoDocumentoFotografico;
    }

    public void setFotoDocumentoFotografico(com.company.test1.entity.FotosDocumentosFotograficosExt fotoDocumentoFotografico) {
        this.fotoDocumentoFotografico = fotoDocumentoFotografico;
    }
}