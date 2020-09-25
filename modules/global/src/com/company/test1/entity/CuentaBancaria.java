package com.company.test1.entity;

import com.company.test1.entity.extroles.Proveedor;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "CUENTA_BANCARIA")
@Entity(name = "test1_CuentaBancaria")
public class CuentaBancaria extends StandardEntity {
    private static final long serialVersionUID = 9102866434063904804L;

    @Transient
    @MetaProperty
    protected String textoCuentaBancariaCompleta;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PERSONA_ID")
    protected Persona persona;

    @Length(message = "El valor entidad se compone de 4 digitos", min = 4, max = 4)
    @NotNull(message = "Ingresar Entidad")
    @Column(name = "ENTIDAD", length = 5)
    protected String entidad;

    @Length(message = "El valor Oficina se compone de 4 digitos", min = 4, max = 4)
    @NotNull(message = "Ingresar valor de oficina")
    @Column(name = "OFICINA", length = 5)
    protected String oficina;

    @Length(message = "El valor digitos de control se compone de 2 digitos", min = 2, max = 2)
    @NotNull(message = "Ingresar digitos de control de c/c")
    @Column(name = "DIGITOS_CONTROL", length = 5)
    protected String digitosControl;

    @Length(message = "El valor numero de cuenta se compone de 10 digitos", min = 10, max = 10)
    @NotNull(message = "Ingresar numero de cuenta")
    @Column(name = "NUMERO_CUENTA", length = 15)
    protected String numeroCuenta;

    @Column(name = "INFO_CONTACTO_OFICINA")
    protected String infoContactoOficina;

    @Column(name = "DOMICILIO_ENTIDAD_BANCARIA")
    protected String domicilioEntidadBancaria;

    @NotNull(message = "Aportar nombre de entidad bancaria")
    @Column(name = "NOMBRE_ENTIDAD_BANCARIA")
    protected String nombreEntidadBancaria;

    @NotNull(message = "Ingresar valor de pais")
    @Column(name = "PAIS", length = 50)
    protected String pais;

    @Column(name = "CODIGO_BIC", length = 11)
    protected String codigoBIC;

    @NotNull(message = "Ingresar valor para digitos de control IBAN")
    @Column(name = "DIGIGOS_CONTROL_IBAN", length = 5)
    protected String digigosControlIBAN;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "cuentaBancaria")
    protected Proveedor proveedor;

    @Column(name = "RM2ID")
    protected Integer rm2id;

    @Transient
    @MetaProperty
    public String getVersionIBAN() {
        String s = this.pais + this.digigosControlIBAN + this.oficina + this.entidad + this.digitosControl + this.numeroCuenta;
        return s;
    }

    @Transient
    @MetaProperty
    public String getTextoCuentaBancariaPropietario() {
        if (this.getPersona()==null){
            return "Cuenta sin Propietario Asociado " + this.getTextoCuentaBancariaCompleta();
        }
        if (this.getPersona().getPropietario()!=null){
            return this.getPersona().getNombreCompleto() + this.getTextoCuentaBancariaCompleta();
        }
        return "Cuenta sin Propietario Asociado " + this.getTextoCuentaBancariaCompleta();
    }

    public Integer getRm2id() {
        return rm2id;
    }

    public void setRm2id(Integer rm2id) {
        this.rm2id = rm2id;
    }

    public String getTextoCuentaBancariaCompleta() {
        return this.codigoBIC + this.digigosControlIBAN + this.entidad + this.oficina + this.digitosControl + this.numeroCuenta;
    }

    public String getDigigosControlIBAN() {
        return digigosControlIBAN;
    }

    public void setDigigosControlIBAN(String digigosControlIBAN) {
        this.digigosControlIBAN = digigosControlIBAN;
    }

    public String getCodigoBIC() {
        return codigoBIC;
    }

    public void setCodigoBIC(String codigoBIC) {
        this.codigoBIC = codigoBIC;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getNombreEntidadBancaria() {
        return nombreEntidadBancaria;
    }

    public void setNombreEntidadBancaria(String nombreEntidadBancaria) {
        this.nombreEntidadBancaria = nombreEntidadBancaria;
    }

    public String getDomicilioEntidadBancaria() {
        return domicilioEntidadBancaria;
    }

    public void setDomicilioEntidadBancaria(String domicilioEntidadBancaria) {
        this.domicilioEntidadBancaria = domicilioEntidadBancaria;
    }

    public String getInfoContactoOficina() {
        return infoContactoOficina;
    }

    public void setInfoContactoOficina(String infoContactoOficina) {
        this.infoContactoOficina = infoContactoOficina;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public String getOficina() {
        return oficina;
    }

    public void setOficina(String oficina) {
        this.oficina = oficina;
    }

    public String getEntidad() {
        return entidad;
    }

    public void setEntidad(String entidad) {
        this.entidad = entidad;
    }

    public String getDigitosControl() {
        return digitosControl;
    }

    public void setDigitosControl(String digitosControl) {
        this.digitosControl = digitosControl;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }
}