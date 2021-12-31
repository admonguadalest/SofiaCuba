package com.company.test1.entity;

import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

@Table(name = "PUNTO_ENTRADA_DOCUMENTOS")
@Entity(name = "test1_PuntoEntradaDocumentos")
public class PuntoEntradaDocumentos extends StandardEntity {
    private static final long serialVersionUID = 3966568833825898326L;

    @Column(name = "TITULO")
    private String titulo;

    @Column(name = "TIPO")
    private String tipo;

    @Lob
    @Column(name = "DESCRIPCION")
    private String descripcion;

    @Lob
    @Column(name = "PROPIEDADES_JSON")
    private String propiedadesJson;

    @MetaProperty
    public String getDescriptivo() {
        return titulo + "<"+getTipo().toString()+">";
    }

    public void setTipo(TipoPuntoEntradaDocumentosEnum tipo) {
        this.tipo = tipo == null ? null : tipo.getId();
    }

    public TipoPuntoEntradaDocumentosEnum getTipo() {
        return tipo == null ? null : TipoPuntoEntradaDocumentosEnum.fromId(tipo);
    }

    public String getPropiedadesJson() {
        return propiedadesJson;
    }

    public void setPropiedadesJson(String propiedadesJson) {
        this.propiedadesJson = propiedadesJson;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}