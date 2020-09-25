package com.company.test1.entity;

import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@DiscriminatorValue("PJ")
@Entity(name = "test1_PersonaJuridica")
public class PersonaJuridica extends Persona {
    private static final long serialVersionUID = 7250862918263454548L;

    @Length(message = "Aportar descripcion de actividad (minimo 3 caracteres)", min = 3)
    @NotNull(message = "Aportar descripcion de actividad")
    @Column(name = "DESCRIPCION_ACTIVIDAD")
    protected String descripcionActividad;

    @Length(message = "Aportar Razon Social (minimo 5 caracteres)", min = 5)
    @NotNull
    @Column(name = "RAZON_SOCIAL")
    protected String razonSocial;

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getDescripcionActividad() {
        return descripcionActividad;
    }

    public void setDescripcionActividad(String descripcionActividad) {
        this.descripcionActividad = descripcionActividad;
    }
}