package com.company.test1.entity;

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "TEST1_MAILING_LIST")
@Entity(name = "test1_MailingList")
public class MailingList extends StandardEntity {
    private static final long serialVersionUID = 8350475493334224380L;

    @Column(name = "NOMBRE")
    private String nombre;

    @Lob
    @Column(name = "DESCRIPCION")
    private String descripcion;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "COLECCION_ARCHIVOS_ADJUNTOS_ID")
    private ColeccionArchivosAdjuntos coleccionArchivosAdjuntos;

    @Lob
    @Column(name = "COMMA_SEPARATED_MAILING_LIST")
    private String commaSeparatedMailingList;

    public String getCommaSeparatedMailingList() {
        return commaSeparatedMailingList;
    }

    public void setCommaSeparatedMailingList(String commaSeparatedMailingList) {
        this.commaSeparatedMailingList = commaSeparatedMailingList;
    }

    public ColeccionArchivosAdjuntos getColeccionArchivosAdjuntos() {
        return coleccionArchivosAdjuntos;
    }

    public void setColeccionArchivosAdjuntos(ColeccionArchivosAdjuntos coleccionArchivosAdjuntos) {
        this.coleccionArchivosAdjuntos = coleccionArchivosAdjuntos;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}