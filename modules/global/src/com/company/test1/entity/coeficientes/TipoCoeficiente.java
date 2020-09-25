package com.company.test1.entity.coeficientes;

import com.haulmont.cuba.core.entity.StandardEntity;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Table(name = "TEST1_TIPO_COEFICIENTE")
@Entity(name = "test1_TipoCoeficiente")
public class TipoCoeficiente extends StandardEntity {
    private static final long serialVersionUID = -8858649929359325562L;

    @Length(message = "Longitud minima de 1 caracter para campo Nombre", min = 1)
    @NotNull
    @Column(name = "NOMBRE")
    protected String nombre;

    @NotNull(message = "Aportar descripcion para Tipo de Coeficiente")
    @Column(name = "DESCRIPCION")
    protected String descripcion;

    @NotNull
    @Column(name = "VALOR_MIN")
    protected Double valorMin;

    @NotNull
    @Column(name = "VALOR_MAX")
    protected Double valorMax;

    @Column(name = "RM2ID")
    protected Integer rm2id;

    public Integer getRm2id() {
        return rm2id;
    }

    public void setRm2id(Integer rm2id) {
        this.rm2id = rm2id;
    }

    public Double getValorMax() {
        return valorMax;
    }

    public void setValorMax(Double valorMax) {
        this.valorMax = valorMax;
    }

    public Double getValorMin() {
        return valorMin;
    }

    public void setValorMin(Double valorMin) {
        this.valorMin = valorMin;
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