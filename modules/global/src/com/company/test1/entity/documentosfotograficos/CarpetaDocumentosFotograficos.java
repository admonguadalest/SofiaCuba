package com.company.test1.entity.documentosfotograficos;

import com.company.test1.entity.ciclos.Ciclo;
import com.company.test1.entity.ciclos.Evento;
import com.company.test1.entity.contratosinquilinos.LiquidacionExtincion;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "CTA_DOCTOS_FOTOGRAFICOS")
@Entity(name = "test1_CarpetaDocumentosFotograficos")
public class CarpetaDocumentosFotograficos extends StandardEntity {
    private static final long serialVersionUID = 4079557281941786745L;

    @Column(name = "NOMBRE_CARPETA")
    protected String nombreCarpeta;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CICLO_ID")
    protected Ciclo ciclo;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EVENTO_ID")
    protected Evento evento;

    @Column(name = "APORTADAS_POR")
    protected String aportadasPor;

    @Column(name = "DESCRIPCION")
    protected String descripcion;

    @Column(name = "NUMERO_DE_FOTOGRAFIAS")
    protected Integer numeroDeFotografias;

    @Column(name = "ACCESIBLE_PARA_COMERCIALES")
    protected Boolean accesibleParaComerciales;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LIQUIDACION_EXTINCION_ID")
    protected LiquidacionExtincion liquidacionExtincion;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "carpeta")
    protected List<FotoDocumentoFotografico> fotos = new ArrayList<FotoDocumentoFotografico>();

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "carpetaDocumentoFotografico")
    protected List<FotoThumbnail> fotosThumbnail = new ArrayList<FotoThumbnail>();

    @Column(name = "RM2ID")
    protected Integer rm2id;

    public List<FotoThumbnail> getFotosThumbnail() {
        return fotosThumbnail;
    }

    public void setFotosThumbnail(List<FotoThumbnail> fotosThumbnail) {
        this.fotosThumbnail = fotosThumbnail;
    }

    public List<FotoDocumentoFotografico> getFotos() {
        return fotos;
    }

    public void setFotos(List<FotoDocumentoFotografico> fotos) {
        this.fotos = fotos;
    }

    public void setCiclo(Ciclo ciclo) {
        this.ciclo = ciclo;
    }

    public Ciclo getCiclo() {
        return ciclo;
    }

    public Integer getRm2id() {
        return rm2id;
    }

    public void setRm2id(Integer rm2id) {
        this.rm2id = rm2id;
    }

    public LiquidacionExtincion getLiquidacionExtincion() {
        return liquidacionExtincion;
    }

    public void setLiquidacionExtincion(LiquidacionExtincion liquidacionExtincion) {
        this.liquidacionExtincion = liquidacionExtincion;
    }

    public Boolean getAccesibleParaComerciales() {
        return accesibleParaComerciales;
    }

    public void setAccesibleParaComerciales(Boolean accesibleParaComerciales) {
        this.accesibleParaComerciales = accesibleParaComerciales;
    }

    public Integer getNumeroDeFotografias() {
        return numeroDeFotografias;
    }

    public void setNumeroDeFotografias(Integer numeroDeFotografias) {
        this.numeroDeFotografias = numeroDeFotografias;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getAportadasPor() {
        return aportadasPor;
    }

    public void setAportadasPor(String aportadasPor) {
        this.aportadasPor = aportadasPor;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public String getNombreCarpeta() {
        return nombreCarpeta;
    }

    public void setNombreCarpeta(String nombreCarpeta) {
        this.nombreCarpeta = nombreCarpeta;
    }
}