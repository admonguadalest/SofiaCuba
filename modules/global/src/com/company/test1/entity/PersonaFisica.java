package com.company.test1.entity;

import com.company.test1.entity.enums.EstadoCivilEnum;
import com.haulmont.cuba.core.entity.annotation.CaseConversion;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@DiscriminatorValue("PF")
@Entity(name = "test1_PersonaFisica")
public class PersonaFisica extends Persona {
    private static final long serialVersionUID = 7025683925460035707L;

    @Length(message = "Longitud minima de 2 caracteres", min = 2)
    @NotNull
    @CaseConversion
    @Column(name = "APELLIDO1")
    protected String apellido1;

    @Column(name = "APELLIDO2")
    protected String apellido2;

    @Column(name = "ESTADO_CIVIL")
    protected String estadoCivil;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_NACIMIENTO")
    protected Date fechaNacimiento;

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setEstadoCivil(EstadoCivilEnum estadoCivil) {
        this.estadoCivil = estadoCivil == null ? null : estadoCivil.getId();
    }

    public EstadoCivilEnum getEstadoCivil() {
        return estadoCivil == null ? null : EstadoCivilEnum.fromId(estadoCivil);
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }
}