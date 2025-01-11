package com.company.test1.entity.departamentos;

import com.company.test1.entity.ArchivoAdjunto;
import com.company.test1.entity.AsTreeItem;
import com.company.test1.entity.ColeccionArchivosAdjuntos;
import com.company.test1.entity.Direccion;
import com.company.test1.entity.coeficientes.UbicacionCoeficiente;
import com.company.test1.entity.extroles.Propietario;
import com.company.test1.validations.departamentosyubicacioneas.UbicacionBean;
import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.chile.core.annotations.NumberFormat;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.Listeners;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.entity.annotation.PublishEntityChangedEvents;
import com.haulmont.cuba.core.global.DeletePolicy;
import com.haulmont.cuba.core.global.validation.groups.UiCrossFieldChecks;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@PublishEntityChangedEvents
@Table(name = "UBICACION")
@Entity(name = "test1_Ubicacion")
@UbicacionBean(groups = UiCrossFieldChecks.class)
@Listeners("test1_UbicacionEntityListener")
public class Ubicacion extends StandardEntity implements AsTreeItem, Comparable<Ubicacion> {
    private static final long serialVersionUID = 1010023296036208154L;

    @NotNull(message = "Aportar nombre de ubicacion")
    @Column(name = "NOMBRE", nullable = false)
    protected String nombre;

    @NotNull(message = "Aportar una instancia de direccion")
    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DIRECCION_ID")
    protected Direccion direccion = new Direccion();

    @NotNull(message = "Aportar abreviacion de ubicacion")
    @Column(name = "ABREVIACION_UBICACION", length = 10)
    protected String abreviacionUbicacion;

    @Column(name = "ES_PROPIEDAD_VERTICAL")
    protected Boolean esPropiedadVertical = false;

    @NumberFormat(pattern = "##0.0000000", decimalSeparator = ",", groupingSeparator = ".")
    @Column(name = "LATITUD")
    protected Double latitud;

    @NumberFormat(pattern = "##0.00000", decimalSeparator = ",", groupingSeparator = ".")
    @Column(name = "LONGITUD")
    protected Double longitud;

    @NotNull(message = "Especificar numero de ascensores")
    @Column(name = "NUM_ASCENSORES")
    protected Integer numAscensores;

    @OnDeleteInverse(DeletePolicy.DENY)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROPIETARIO_ID")
    protected Propietario propietario;

    @Composition
    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "ubicacion")
    protected List<Departamento> departamentos = new ArrayList<Departamento>();

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COLECCION_ARCHIVOS_ADJUNTOS_ID")
    protected ColeccionArchivosAdjuntos coleccionArchivosAdjuntos = null;

    @Column(name = "INFORMACION_CATASTRAL", length = 50)
    protected String informacionCatastral;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FOTO_FACHADA_ID")
    protected ArchivoAdjunto fotoFachada;

    @NotNull(message = "Aportr nombre de Districto")
    @Column(name = "NOMBRE_DISTRITO")
    protected String nombreDistrito;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "ubicacion")
    protected List<UbicacionCoeficiente> ubicacionesCoeficientes;

    @Column(name = "RM2ID")
    protected Integer rm2id;

    @Lob
    @Column(name = "AMPLIACION")
    protected String ampliacion;

    public List<UbicacionCoeficiente> getUbicacionesCoeficientes() {
        return ubicacionesCoeficientes;
    }

    public void setUbicacionesCoeficientes(List<UbicacionCoeficiente> ubicacionesCoeficientes) {
        this.ubicacionesCoeficientes = ubicacionesCoeficientes;
    }

    public void setPropietario(Propietario propietario) {
        this.propietario = propietario;
    }

    public Propietario getPropietario() {
        return propietario;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public void setRm2id(Integer rm2id) {
        this.rm2id = rm2id;
    }

    public Integer getRm2id() {
        return rm2id;
    }

    public String getNombreDistrito() {
        return nombreDistrito;
    }

    public void setNombreDistrito(String nombreDistrito) {
        this.nombreDistrito = nombreDistrito;
    }

    public ArchivoAdjunto getFotoFachada() {
        return fotoFachada;
    }

    public void setFotoFachada(ArchivoAdjunto fotoFachada) {
        this.fotoFachada = fotoFachada;
    }

    public Integer getNumAscensores() {
        return numAscensores;
    }

    public void setNumAscensores(Integer numAscensores) {
        this.numAscensores = numAscensores;
    }

    public String getInformacionCatastral() {
        return informacionCatastral;
    }

    public void setInformacionCatastral(String informacionCatastral) {
        this.informacionCatastral = informacionCatastral;
    }

    public Boolean getEsPropiedadVertical() {
        return esPropiedadVertical;
    }

    public void setEsPropiedadVertical(Boolean esPropiedadVertical) {
        this.esPropiedadVertical = esPropiedadVertical;
    }

    public String getAbreviacionUbicacion() {
        return abreviacionUbicacion;
    }

    public void setAbreviacionUbicacion(String abreviacionUbicacion) {
        this.abreviacionUbicacion = abreviacionUbicacion;
    }

    public List<Departamento> getDepartamentos() {
        return departamentos;
    }

    public void setDepartamentos(List<Departamento> departamentos) {
        this.departamentos = departamentos;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public ColeccionArchivosAdjuntos getColeccionArchivosAdjuntos() {
        return coleccionArchivosAdjuntos;
    }

    public void setColeccionArchivosAdjuntos(ColeccionArchivosAdjuntos coleccionArchivosAdjuntos) {
        this.coleccionArchivosAdjuntos = coleccionArchivosAdjuntos;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String getTextAsTreeItem() {
        return this.getNombre();
    }

    @Override
    public int compareTo(Ubicacion o){
        Ubicacion u = o;
        return this.getNombre().compareTo(u.getNombre());
    }

    public static Comparator<Ubicacion> comparatorRm2id = new Comparator<Ubicacion>(){

        @Override
        public int compare(Ubicacion ubicacion, Ubicacion t1) {
            return ubicacion.getRm2id().compareTo(t1.getRm2id());
        }
    };

    public String getAmpliacion() {
        return ampliacion;
    }

    public void setAmpliacion(String ampliacion) {
        this.ampliacion = ampliacion;
    }
}