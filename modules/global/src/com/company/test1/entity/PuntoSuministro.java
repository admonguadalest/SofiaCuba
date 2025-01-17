package com.company.test1.entity;

import com.company.test1.entity.extroles.Proveedor;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "TEST1_PUNTO_SUMINISTRO")
@Entity(name = "test1_PuntoSuministro")
public class PuntoSuministro extends StandardEntity {
    private static final long serialVersionUID = 8071942547771966433L;

    @Column(name = "IDENTIFICADOR_PUNTO_SUMINISTRO")
    protected String identificadorPuntoSuministro;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PROVEEDOR_ID")
    private Proveedor proveedor;

    public String getIdentificadorPuntoSuministro() {
        return identificadorPuntoSuministro;
    }

    public void setIdentificadorPuntoSuministro(String identificadorPuntoSuministro) {
        this.identificadorPuntoSuministro = identificadorPuntoSuministro;
    }

    @Lob
    @Column(name = "DESCRIPCION")
    protected String descripcion;

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}