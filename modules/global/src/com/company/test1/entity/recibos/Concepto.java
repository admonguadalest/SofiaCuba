package com.company.test1.entity.recibos;

import com.company.test1.entity.recibos.accesorios.ConceptoOrdenacionComparator;
import com.company.test1.validations.recibos.ConceptoBean;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.global.validation.groups.UiCrossFieldChecks;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@NamePattern("%s|tituloConcepto")
@Table(name = "CONCEPTO")
@Entity(name = "test1_Concepto")
@ConceptoBean(groups = UiCrossFieldChecks.class)
public class Concepto extends StandardEntity {
    private static final long serialVersionUID = 3332985679772844542L;

    public static ConceptoOrdenacionComparator ordenacionComparator = new ConceptoOrdenacionComparator();

    @Length(message = "Cadena de entre 3 y 100 caracteres", min = 3, max = 100)
    @NotNull(message = "Aportar titulo a Concepto")
    @Column(name = "TITULO_CONCEPTO", length = 100)
    protected String tituloConcepto;

    @Column(name = "RM2ID")
    protected Integer rm2id;

    @Length(message = "Entre 3 y 4 caracteres", min = 3, max = 4)
    @NotNull
    @Column(name = "ABREVIACION", length = 25)
    protected String abreviacion;

    @Column(name = "DESCRIPCION")
    protected String descripcion;

    @Column(name = "ADICION_SUSTRACCION")
    protected Boolean adicionSustraccion;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_CREACION")
    protected Date fechaCreacion;

    @Column(name = "AGREGAR_CONCEPTO_EN_RECIBO")
    protected Boolean agregarConceptoEnRecibo;

    @Column(name = "AJUSTABLE_AGREGADAMENTE")
    protected Boolean ajustableAgregadamente;

    @NotNull(message = "Aportar valor de ordenacion")
    @Column(name = "ORDENACION", nullable = false)
    protected Integer ordenacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MASTER_CONCEPTO_ID")
    protected Concepto masterConcepto;

    public Concepto getMasterConcepto() {
        return masterConcepto;
    }

    public void setMasterConcepto(Concepto masterConcepto) {
        this.masterConcepto = masterConcepto;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public Integer getRm2id() {
        return rm2id;
    }

    public void setRm2id(Integer rm2id) {
        this.rm2id = rm2id;
    }

    public Integer getOrdenacion() {
        return ordenacion;
    }

    public void setOrdenacion(Integer ordenacion) {
        this.ordenacion = ordenacion;
    }

    public Boolean getAjustableAgregadamente() {
        return ajustableAgregadamente;
    }

    public void setAjustableAgregadamente(Boolean ajustableAgregadamente) {
        this.ajustableAgregadamente = ajustableAgregadamente;
    }

    public Boolean getAgregarConceptoEnRecibo() {
        return agregarConceptoEnRecibo;
    }

    public void setAgregarConceptoEnRecibo(Boolean agregarConceptoEnRecibo) {
        this.agregarConceptoEnRecibo = agregarConceptoEnRecibo;
    }

    public Boolean getAdicionSustraccion() {
        return adicionSustraccion;
    }

    public void setAdicionSustraccion(Boolean adicionSustraccion) {
        this.adicionSustraccion = adicionSustraccion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getAbreviacion() {
        return abreviacion;
    }

    public void setAbreviacion(String abreviacion) {
        this.abreviacion = abreviacion;
    }

    public String getTituloConcepto() {
        return tituloConcepto;
    }

    public void setTituloConcepto(String tituloConcepto) {
        this.tituloConcepto = tituloConcepto;
    }
}