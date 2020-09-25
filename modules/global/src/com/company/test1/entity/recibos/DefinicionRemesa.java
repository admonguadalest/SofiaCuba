package com.company.test1.entity.recibos;

import com.company.test1.entity.CuentaBancaria;
import com.company.test1.entity.Persona;
import com.company.test1.entity.enums.recibos.DefinicionRemesaModoPresentacionEnum;
import com.company.test1.entity.enums.recibos.DefinicionRemesaTipoGiroEnum;
import com.company.test1.entity.enums.recibos.DefinicionRemesaUnidadPeriodicidadEnum;
import com.company.test1.entity.extroles.Propietario;
import com.company.test1.validations.definicionremesa.DefinicionRemesaDelegado;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.Listeners;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;
import com.haulmont.cuba.core.global.validation.groups.UiCrossFieldChecks;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

//@Listeners("test1_DefinicionRemesaListener")
@NamePattern("%s|nombreRemesa")
@Table(name = "DEFINICION_REMESA")
@Entity(name = "test1_DefinicionRemesa")
@DefinicionRemesaDelegado(groups = UiCrossFieldChecks.class)
public class DefinicionRemesa extends StandardEntity {
    private static final long serialVersionUID = -633564614880888657L;

    @Column(name = "NOMBRE_REMESA")
    @NotNull
    @Length(message = "Asignar un nombre con una longitud minima de 3 caracteres", min = 3)
    protected String nombreRemesa;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROPIETARIO_ID")
    @NotNull(message = "Asignar un Propietario a la Definicion de Remesa")
    protected Propietario propietario;

    @Column(name = "DESCRIPCION")
    protected String descripcion;

    @Column(name = "UNIDAD_PERIODICIDAD")
    @NotNull
    protected Integer unidadPeriodicidad;

    @Positive
    @Max(12)
    @Min(1)
    @Column(name = "CANTIDAD_PERIODICIDAD")
    @NotNull
    protected Integer cantidadPeriodicidad;

    @Column(name = "TIPO_GIRO")
    @NotNull
    protected Integer tipoGiro;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CUENTA_BANCARIA_ID")
    protected CuentaBancaria cuentaBancaria;

    @Column(name = "MODO_PRESENTACION")
    @NotNull
    protected Integer modoPresentacion;

    @OnDeleteInverse(DeletePolicy.DENY)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DELEGADO_ID")
    protected Persona delegado;

    @Column(name = "RM2ID")
    protected Integer rm2id;

    public void setRm2id(Integer rm2id) {
        this.rm2id = rm2id;
    }

    public Integer getRm2id() {
        return rm2id;
    }

    public Propietario getPropietario() {
        return propietario;
    }

    public void setPropietario(Propietario propietario) {
        this.propietario = propietario;
    }

    public Persona getDelegado() {
        return delegado;
    }

    public void setDelegado(Persona delegado) {
        this.delegado = delegado;
    }

    public DefinicionRemesaModoPresentacionEnum getModoPresentacion() {
        return modoPresentacion == null ? null : DefinicionRemesaModoPresentacionEnum.fromId(modoPresentacion);
    }

    public void setModoPresentacion(DefinicionRemesaModoPresentacionEnum modoPresentacion) {
        this.modoPresentacion = modoPresentacion == null ? null : modoPresentacion.getId();
    }

    public CuentaBancaria getCuentaBancaria() {
        return cuentaBancaria;
    }

    public void setCuentaBancaria(CuentaBancaria cuentaBancaria) {
        this.cuentaBancaria = cuentaBancaria;
    }

    public DefinicionRemesaTipoGiroEnum getTipoGiro() {
        return tipoGiro == null ? null : DefinicionRemesaTipoGiroEnum.fromId(tipoGiro);
    }

    public void setTipoGiro(DefinicionRemesaTipoGiroEnum tipoGiro) {
        this.tipoGiro = tipoGiro == null ? null : tipoGiro.getId();
    }

    public Integer getCantidadPeriodicidad() {
        return cantidadPeriodicidad;
    }

    public void setCantidadPeriodicidad(Integer cantidadPeriodicidad) {
        this.cantidadPeriodicidad = cantidadPeriodicidad;
    }

    public void setUnidadPeriodicidad(DefinicionRemesaUnidadPeriodicidadEnum unidadPeriodicidad) {
        this.unidadPeriodicidad = unidadPeriodicidad == null ? null : unidadPeriodicidad.getId();
    }

    public DefinicionRemesaUnidadPeriodicidadEnum getUnidadPeriodicidad() {
        return unidadPeriodicidad == null ? null : DefinicionRemesaUnidadPeriodicidadEnum.fromId(unidadPeriodicidad);
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombreRemesa() {
        return nombreRemesa;
    }

    public void setNombreRemesa(String nombreRemesa) {
        this.nombreRemesa = nombreRemesa;
    }
}