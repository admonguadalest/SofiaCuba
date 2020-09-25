package com.company.test1.entity.recibos;

import com.company.test1.entity.Persona;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Table(name = "TEST1_REGISTRO_INDICE_REFERENCIA")
@Entity(name = "test1_RegistroIndiceReferencia")
public class RegistroIndiceReferencia extends StandardEntity {
    private static final long serialVersionUID = -8267554037493886628L;

    @Max(message = "Introducir valores entre 1 y 12", value = 12)
    @Min(message = "Introducir valores entre 1 y 12", value = 1)
    @NotNull
    @Column(name = "MES")
    protected Integer mes;

    @Digits(message = "Incorporar anyo con 4 digitos", integer = 4, fraction = 0)
    @NotNull
    @Column(name = "ANNO")
    protected Integer anno;

    @NotNull(message = "Incorporar valor numerico en campo Valor")
    @Column(name = "VALOR")
    protected Double valor;

    @Length(message = "Aportar nombre de indice al menos con 3 letras (IPC)", min = 3)
    @NotNull
    @Column(name = "NOMBRE_TIPO", length = 50)
    protected String nombreTipo;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REALIZADO_POR_PERSONA_ID")
    protected Persona realizadoPor;

    public Persona getRealizadoPor() {
        return realizadoPor;
    }

    public void setRealizadoPor(Persona realizadoPor) {
        this.realizadoPor = realizadoPor;
    }

    public String getNombreTipo() {
        return nombreTipo;
    }

    public void setNombreTipo(String nombreTipo) {
        this.nombreTipo = nombreTipo;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Integer getAnno() {
        return anno;
    }

    public void setAnno(Integer anno) {
        this.anno = anno;
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }
}