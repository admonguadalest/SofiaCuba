package com.company.test1.entity.cuentadegasto;

import com.company.test1.entity.Persona;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;

@Table(name = "TEST1_CUENTA_DE_GASTO")
@Entity(name = "test1_CuentaDeGasto")
public class CuentaDeGasto extends StandardEntity {
    private static final long serialVersionUID = 416107239853941352L;

    @Column(name = "NOMBRE")
    private String nombre;

    @Column(name = "TARJETA_DE_CREDITO_BANCARIA")
    private Boolean tarjetaDeCreditoBancaria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TITULAR_ID")
    private Persona titular;

    @Lob
    @Column(name = "AMPLIACION")
    private String ampliacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PERSONA_ID")
    private Persona persona;

    public Boolean getTarjetaDeCreditoBancaria() {
        return tarjetaDeCreditoBancaria;
    }

    public void setTarjetaDeCreditoBancaria(Boolean tarjetaDeCreditoBancaria) {
        this.tarjetaDeCreditoBancaria = tarjetaDeCreditoBancaria;
    }

    public Persona getTitular() {
        return titular;
    }

    public void setTitular(Persona titular) {
        this.titular = titular;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public String getAmpliacion() {
        return ampliacion;
    }

    public void setAmpliacion(String ampliacion) {
        this.ampliacion = ampliacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}