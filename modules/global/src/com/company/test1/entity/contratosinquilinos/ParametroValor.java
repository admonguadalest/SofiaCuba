package com.company.test1.entity.contratosinquilinos;

import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;

@Table(name = "PARAMETRO_VALOR")
@Entity(name = "test1_ParametroValor")
public class ParametroValor extends StandardEntity {
    private static final long serialVersionUID = -8917114976573301354L;

    @Column(name = "NOMBRE_PARAMETRO")
    protected String nombreParametro;

    @Lob
    @Column(name = "VALOR")
    protected String valor;

    @Column(name = "DESCRIPCION_PARAMETRO")
    protected String descripcionParametro;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IMPLEMENTACION_MODELO_ID")
    protected ImplementacionModelo implementacionModelo;

    public ImplementacionModelo getImplementacionModelo() {
        return implementacionModelo;
    }

    public void setImplementacionModelo(ImplementacionModelo implementacionModelo) {
        this.implementacionModelo = implementacionModelo;
    }

    public String getDescripcionParametro() {
        return descripcionParametro;
    }

    public void setDescripcionParametro(String descripcionParametro) {
        this.descripcionParametro = descripcionParametro;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getNombreParametro() {
        return nombreParametro;
    }

    public void setNombreParametro(String nombreParametro) {
        this.nombreParametro = nombreParametro;
    }
}