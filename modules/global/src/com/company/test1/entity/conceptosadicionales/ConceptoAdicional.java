package com.company.test1.entity.conceptosadicionales;

import com.haulmont.cuba.core.entity.StandardEntity;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Table(name = "CONCEPTO_ADICIONAL")
@Entity(name = "test1_ConceptoAdicional")
public class ConceptoAdicional extends StandardEntity {
    private static final long serialVersionUID = 7860808360599583310L;

    @Length(message = "Longitud minima de 3 caracteres para campo Nombre", min = 3)
    @NotNull
    @Column(name = "NOMBRE")
    protected String nombre;

    @Length(message = "Longitud minima de 3 caracteres para Abreviacion", min = 3)
    @NotNull
    @Column(name = "ABREVIACION", length = 50)
    protected String abreviacion;

    @Column(name = "ADICION_SUSTRACCION")
    protected Boolean adicionSustraccion;

    @Column(name = "RM2ID")
    protected Integer rm2id;

    public void setRm2id(Integer rm2id) {
        this.rm2id = rm2id;
    }

    public Integer getRm2id() {
        return rm2id;
    }

    public Boolean getAdicionSustraccion() {
        return adicionSustraccion;
    }

    public void setAdicionSustraccion(Boolean adicionSustraccion) {
        this.adicionSustraccion = adicionSustraccion;
    }

    public String getAbreviacion() {
        return abreviacion;
    }

    public void setAbreviacion(String abreviacion) {
        this.abreviacion = abreviacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}