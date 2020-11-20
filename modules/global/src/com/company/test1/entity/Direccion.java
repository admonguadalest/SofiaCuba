package com.company.test1.entity;

import com.company.test1.entity.enums.NombreTipoDireccion;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.CaseConversion;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Table(name = "DIRECCION")
@Entity(name = "test1_Direccion")

public class Direccion extends StandardEntity {
    private static final long serialVersionUID = -2220600454605324077L;

    /*public static final String NOMBRE_DIRECCION_PROPIETARIO_CONTRATO_N19 = "Domicilio Contractual";

    public static final String NOMBRE_DIRECCION_INQUILINO = "DOMICILIO ";

    public static final String NOMBRE_DIRECCION_PROVEEDOR = "Domicilio";

    public static final String NOMBRE_DIRECCION_UBICACION = "Domicilio Ubicación";

    public static final String NOMBRE_DIRECCION_PERSONALIZADA = "Personalizada";*/

    @NotNull(message = "Aportar Codigo Postal")
    @Column(name = "CODIGO_POSTAL", length = 25)
    protected String codigoPostal;

    @Column(name = "DIRECCION_COMPLETA")
    protected String direccionCompleta;

    @Column(name = "ESCALERA", length = 50)
    protected String escalera;

    @NotNull(message = "El valor de Nombre no puede ser nulo")
    @CaseConversion
    @Column(name = "NOMBRE")
    protected String nombre;

    @NotNull(message = "Ingresar Nombre de Via")
    @Column(name = "NOMBRE_VIA")
    protected String nombreVia;

    @NotNull(message = "Ingresar Numero de Via")
    @Column(name = "NUMERO_VIA", length = 100)
    protected String numeroVia;

    @Column(name = "OBSERVACIONES")
    protected String observaciones;

    @NotNull(message = "Ingresar valor de Pais")
    @Column(name = "PAIS")
    protected String pais;

    @Column(name = "PISO", length = 50)
    protected String piso;

    @NotNull(message = "Ingresar valor de Poblacion")
    @Column(name = "POBLACION")
    protected String poblacion;

    @NotNull(message = "Indicar Provincia")
    @Column(name = "PROVINCIA")
    protected String provincia;

    @Column(name = "PUERTA", length = 25)
    protected String puerta;

    @NotNull(message = "Indicar Region")
    @Column(name = "REGION")
    protected String region;

    @NotNull(message = "Ingresar valor del campo Via")
    @Column(name = "VIA")
    protected String via;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PERSONA_ID")
    protected Persona persona;

    @Column(name = "ENVIAR_CORRESPONDENCIA_A_ESTA_DIRECCION")
    protected Boolean enviarCorrespondenciaAEstaDireccion;

    @Transient
    @MetaProperty
    public String getDireccionParaDocumento() {
        return this.nombreVia + " " + this.numeroVia + " " + this.piso + " " + this.puerta;
    }

    public Boolean getEnviarCorrespondenciaAEstaDireccion() {
        return enviarCorrespondenciaAEstaDireccion;
    }

    public void setEnviarCorrespondenciaAEstaDireccion(Boolean enviarCorrespondenciaAEstaDireccion) {
        this.enviarCorrespondenciaAEstaDireccion = enviarCorrespondenciaAEstaDireccion;
    }

    public String getVia() {
        return via;
    }

    public void setVia(String via) {
        this.via = via;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPuerta() {
        return puerta;
    }

    public void setPuerta(String puerta) {
        this.puerta = puerta;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getNumeroVia() {
        return numeroVia;
    }

    public void setNumeroVia(String numeroVia) {
        this.numeroVia = numeroVia;
    }

    public String getPoblacion() {
        return poblacion;
    }

    public void setPoblacion(String poblacion) {
        this.poblacion = poblacion;
    }

    public String getPiso() {
        return piso;
    }

    public void setPiso(String piso) {
        this.piso = piso;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getNombreVia() {
        return nombreVia;
    }

    public void setNombreVia(String nombreVia) {
        this.nombreVia = nombreVia;
    }

    public String getEscalera() {
        return escalera;
    }

    public void setEscalera(String escalera) {
        this.escalera = escalera;
    }

    public String getDireccionCompleta() {
        return direccionCompleta;
    }

    public void setDireccionCompleta(String direccionCompleta) {
        this.direccionCompleta = direccionCompleta;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @PrePersist
    public void prePersist() {
        int y = 2;

    }

    public static Direccion getDireccionDesdeNombre(Persona p, String nombre) throws Exception{
        List<Direccion> l = p.getDirecciones();
        for (int i = 0; i < l.size(); i++) {
            Direccion d = l.get(i);
            if (d.getNombre().compareTo(nombre)==0){
                return d;
            }
        }
        throw new Exception("Direccion con nombre '" + nombre + "' no encontrada");
    }

    public static Direccion getDireccionDesdeEnum(Persona p, NombreTipoDireccion ntd) throws Exception{
        List<Direccion> l = p.getDirecciones();
        for (int i = 0; i < l.size(); i++) {
            Direccion d = l.get(i);
            if (d.getNombre().compareTo(ntd.getId().toString())==0){
                return d;
            }
        }
        throw new Exception("Direccion con nombre '" + ntd.toString() + "' no encontrada");
    }


}