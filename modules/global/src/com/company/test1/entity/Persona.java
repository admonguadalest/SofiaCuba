package com.company.test1.entity;

import com.company.test1.entity.extroles.Propietario;
import com.company.test1.entity.extroles.Proveedor;
import com.company.test1.validations.personas.PersonaBean;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;
import com.haulmont.cuba.core.global.validation.groups.UiCrossFieldChecks;
import com.haulmont.cuba.security.entity.User;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING)
@Table(name = "PERSONA")
@Entity(name = "test1_Persona")
@PersonaBean(groups = UiCrossFieldChecks.class)
public class Persona extends StandardEntity implements Comparable<Persona> {
    private static final long serialVersionUID = -1704163782743701194L;

    @Column(name = "RM2ID")
    protected Integer rm2id;

    @NotNull(message = "Aportar un usuario")
    @Lookup(type = LookupType.SCREEN)
    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.UNLINK)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USUARIO_ID")
    protected User usuario;

    @Size(message = "Aportar al menos una direccion", min = 1)
    @NotNull
    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "persona")
    @Valid
    protected List<Direccion> direcciones = new ArrayList<Direccion>();

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "persona")
    @Valid
    protected List<CuentaBancaria> cuentasBancarias = new ArrayList<CuentaBancaria>();

    @NotNull
    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "persona")
    @Valid
    protected List<DatoDeContacto> datosDeContacto = new ArrayList<DatoDeContacto>();

    @Length(message = "Longitud minima de 2 caracteres", min = 2)
    @NotNull
    @Column(name = "NOMBRE")
    protected String nombre;

    @Length(message = "Longitud minima de 6 caracteres", min = 6)
    @NotNull
    @Column(name = "NIF_DNI", length = 50)
    protected String nifDni;

    @Column(name = "NOMBRE_COMPLETO")
    protected String nombreCompleto;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "persona")
    protected Proveedor proveedor;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "persona")
    protected Propietario propietario;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "persona")
    protected List<RepresentacionLegal> representacionesLegales;

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }

    public List<RepresentacionLegal> getRepresentacionesLegales() {
        return representacionesLegales;
    }

    public void setRepresentacionesLegales(List<RepresentacionLegal> representacionesLegales) {
        this.representacionesLegales = representacionesLegales;
    }

    public Integer getRm2id() {
        return rm2id;
    }

    public void setRm2id(Integer rm2id) {
        this.rm2id = rm2id;
    }

    public Propietario getPropietario() {
        return propietario;
    }

    public void setPropietario(Propietario propietario) {
        this.propietario = propietario;
    }

    public List<DatoDeContacto> getDatosDeContacto() {
        return datosDeContacto;
    }

    public void setDatosDeContacto(List<DatoDeContacto> datosDeContacto) {
        this.datosDeContacto = datosDeContacto;
    }

    public List<CuentaBancaria> getCuentasBancarias() {
        return cuentasBancarias;
    }

    public void setCuentasBancarias(List<CuentaBancaria> cuentasBancarias) {
        this.cuentasBancarias = cuentasBancarias;
    }

    public List<Direccion> getDirecciones() {
        return direcciones;
    }

    public void setDirecciones(List<Direccion> direcciones) {
        this.direcciones = direcciones;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getNifDni() {
        return nifDni;
    }

    public void setNifDni(String nifDni) {
        this.nifDni = nifDni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public int compareTo(Persona o){
        Persona p = o;
        return this.getNombreCompleto().compareTo(p.getNombreCompleto());
    }

    public Direccion direccionDesdeNombre(String nombre){
        for (int i = 0; i < direcciones.size(); i++) {
            Direccion d = direcciones.get(i);
            if (d.getNombre().trim().toUpperCase().compareTo(nombre.trim().toUpperCase())==0){
                return d;
            }
        }
        return null;
    }
}