package com.company.test1.entity.departamentos;

import com.company.test1.entity.ArchivoAdjunto;
import com.company.test1.entity.AsTreeItem;
import com.company.test1.entity.ColeccionArchivosAdjuntos;
import com.company.test1.entity.coeficientes.Coeficiente;
import com.company.test1.entity.extroles.Propietario;
import com.company.test1.validations.departamentosyubicacioneas.DepartamentoBean;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.chile.core.annotations.NumberFormat;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.*;
import com.haulmont.cuba.core.global.DeletePolicy;
import com.haulmont.cuba.core.global.validation.groups.UiCrossFieldChecks;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@PublishEntityChangedEvents
@Table(name = "DEPARTAMENTO")
@Entity(name = "test1_Departamento")
@DepartamentoBean(groups = UiCrossFieldChecks.class)
@Listeners("test1_DepartamentoEntityListener")
public class Departamento extends StandardEntity implements AsTreeItem {
    private static final long serialVersionUID = -4227392908348714738L;

    @NotNull(message = "Aportar valor Piso")
    @Column(name = "PISO")
    protected String piso;

    @NotNull(message = "Aportar valor Puerta")
    @Column(name = "PUERTA")
    protected String puerta;

    @Transient
    @MetaProperty
    protected String nombreDescriptivoCompleto;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "departamento")
    protected List<CedulaHabitabilidad> cedulasHabitabilidad = new ArrayList<CedulaHabitabilidad>();

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "departamento")
    protected List<CertificadoCalificacionEnergetica> certificadosCalificacionEnergetica = new ArrayList<CertificadoCalificacionEnergetica>();

    @NotNull(message = "Especificar si es vivienda o local")
    @Column(name = "VIVIENDA_LOCAL")
    protected Boolean viviendaLocal = true;

    @NotNull(message = "Aportar valor de superficie")
    @NumberFormat(pattern = "##0.00", decimalSeparator = ",", groupingSeparator = ".")
    @Column(name = "SUPERFICIE")
    protected Double superficie;

    @NotNull(message = "Aportar abreviacion para el valor Piso/Puerta")
    @Column(name = "ABREVIACION_PISO_PUERTA", length = 50)
    protected String abreviacionPisoPuerta;

    @Column(name = "DADO_DE_BAJA")
    protected Boolean dadoDeBaja;

    @Lob
    @Column(name = "DESCRIPCION")
    protected String descripcion;

    @Column(name = "CON_AIRE_ACONDICIONADO")
    protected Boolean conAireAcondicionado;

    @Column(name = "CON_CALEFACCION")
    protected Boolean conCalefaccion;

    @Lob
    @Column(name = "OBSEVACIONES")
    protected String obsevaciones;

    @Column(name = "REFERENCIA_CATASTRAL", length = 100)
    protected String referenciaCatastral;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COLECCION_ADJUNTOS_ID")
    protected ColeccionArchivosAdjuntos coleccionAdjuntos = null;

    @Lookup(type = LookupType.SCREEN, actions = {"lookup", "open"})
    @OnDeleteInverse(DeletePolicy.DENY)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROPIETARIO_ID")
    protected Propietario propietario;

    @NotNull(message = "Departamento debe depender de una Ubicacion (no establecida)")
    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UBICACION_ID")
    protected Ubicacion ubicacion;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PLANO_DEPARTAMENTO_ID")
    protected ArchivoAdjunto planoDepartamento;

    @Column(name = "EXCLUIR_DE_MONITORIZACION_PARA_BUSQUEDA_DE_PISOS_VACIOS")
    protected Boolean excluirDeMonitorizacionParaBusquedaDePisosVacios;

    @Column(name = "EXCLUIR_DE_MONITORIZACION_NO_EMITIDOS_O_ANOMALOS")
    private Boolean excluirDeMonitorizacionNoEmitidosOAnomalos;

    @Column(name = "CON_SALIDA_DE_HUMOS")
    protected Boolean conSalidaDeHumos;

    @NotNull(message = "Establecer valor para numero de banyos")
    @Column(name = "NUM_BANOS")
    protected Integer numBanos;

    @NotNull(message = "Establecer valor para numero de habitaciones")
    @Column(name = "NUM_HABITACIONES")
    protected Integer numHabitaciones;

    @Column(name = "ES_ESTUDIO")
    protected Boolean esEstudio;

    @Column(name = "RM2ID")
    protected Integer rm2id;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "departamento")
    protected List<Coeficiente> coeficientes;

    @MetaProperty
    public String getPisoPuerta() {
        return piso + " " + puerta;
    }

    public List<Coeficiente> getCoeficientes() {
        return coeficientes;
    }

    public void setCoeficientes(List<Coeficiente> coeficientes) {
        this.coeficientes = coeficientes;
    }

    public void setPiso(String piso) {
        this.piso = piso;
    }

    public String getPiso() {
        return piso;
    }

    public void setPuerta(String puerta) {
        this.puerta = puerta;
    }

    public String getPuerta() {
        return puerta;
    }

    @MetaProperty
    public Propietario getPropietarioEfectivo() {
        if (this.getPropietario() != null) return this.getPropietario();
        else {
            return this.getUbicacion().getPropietario();
        }
    }

    public void setPropietario(Propietario propietario) {
        this.propietario = propietario;
    }

    public Propietario getPropietario() {
        return propietario;
    }

    public Integer getRm2id() {
        return rm2id;
    }

    public void setRm2id(Integer rm2id) {
        this.rm2id = rm2id;
    }

    public String getNombreDescriptivoCompleto() {
        try {
            return this.ubicacion.getNombre() + " " + this.getPiso() + " " + this.getPuerta();
        } catch (Exception exc) {
            int y = 2;
        }
        return "";
    }

    public List<CedulaHabitabilidad> getCedulasHabitabilidad() {
        return cedulasHabitabilidad;
    }

    public void setCedulasHabitabilidad(List<CedulaHabitabilidad> cedulasHabitabilidad) {
        this.cedulasHabitabilidad = cedulasHabitabilidad;
    }

    public static CedulaHabitabilidad getCedulaHabitabilidadMasVigente(Departamento d) {
        CedulaHabitabilidad ch = null;
        CedulaHabitabilidad chc = null;

        for (int i = 0; i < d.cedulasHabitabilidad.size(); i++) {
            chc = d.cedulasHabitabilidad.get(i);
            if (ch == null) {
                ch = chc;
            } else {
                if (chc.getFechaEmision().getTime() > ch.getFechaEmision().getTime()) {
                    ch = chc;
                }
            }
        }
        return ch;
    }

    public List<CertificadoCalificacionEnergetica> getCertificadosCalificacionEnergetica() {
        return certificadosCalificacionEnergetica;
    }

    public void setCertificadosCalificacionEnergetica(List<CertificadoCalificacionEnergetica> certificadosCalificacionEnergetica) {
        this.certificadosCalificacionEnergetica = certificadosCalificacionEnergetica;
    }

    public static CertificadoCalificacionEnergetica getCerficadoMasVigente(Departamento d) {
        CertificadoCalificacionEnergetica ch = null;
        CertificadoCalificacionEnergetica chc = null;

        for (int i = 0; i < d.certificadosCalificacionEnergetica.size(); i++) {
            chc = d.certificadosCalificacionEnergetica.get(i);
            if (ch == null) {
                ch = chc;
            } else {
                if (chc.getFechaVencimiento().getTime() > ch.getFechaVencimiento().getTime()) {
                    ch = chc;
                }
            }
        }
        return ch;
    }

    public Boolean getEsEstudio() {
        return esEstudio;
    }

    public void setEsEstudio(Boolean esEstudio) {
        this.esEstudio = esEstudio;
    }

    public Integer getNumHabitaciones() {
        return numHabitaciones;
    }

    public void setNumHabitaciones(Integer numHabitaciones) {
        this.numHabitaciones = numHabitaciones;
    }

    public Integer getNumBanos() {
        return numBanos;
    }

    public void setNumBanos(Integer numBanos) {
        this.numBanos = numBanos;
    }

    public Boolean getConSalidaDeHumos() {
        return conSalidaDeHumos;
    }

    public void setConSalidaDeHumos(Boolean conSalidaDeHumos) {
        this.conSalidaDeHumos = conSalidaDeHumos;
    }

    public Boolean getExcluirDeMonitorizacionParaBusquedaDePisosVacios() {
        return excluirDeMonitorizacionParaBusquedaDePisosVacios;
    }

    public void setExcluirDeMonitorizacionParaBusquedaDePisosVacios(Boolean excluirDeMonitorizacionParaBusquedaDePisosVacios) {
        this.excluirDeMonitorizacionParaBusquedaDePisosVacios = excluirDeMonitorizacionParaBusquedaDePisosVacios;
    }

    public ColeccionArchivosAdjuntos getColeccionAdjuntos() {
        return coleccionAdjuntos;
    }

    public void setColeccionAdjuntos(ColeccionArchivosAdjuntos coleccionAdjuntos) {
        this.coleccionAdjuntos = coleccionAdjuntos;
    }

    public String getReferenciaCatastralEfectiva() {
        if (this.getUbicacion() == null) {
            return referenciaCatastral;
        }
        if (this.getUbicacion().getEsPropiedadVertical()) {
            return this.getUbicacion().getInformacionCatastral();
        }
        return referenciaCatastral;
    }

    public String getReferenciaCatastral() {
        return referenciaCatastral;
    }

    public void setReferenciaCatastral(String referenciaCatastral) {
        this.referenciaCatastral = referenciaCatastral;
    }

    public String getObsevaciones() {
        return obsevaciones;
    }

    public void setObsevaciones(String obsevaciones) {
        this.obsevaciones = obsevaciones;
    }

    public Boolean getConCalefaccion() {
        return conCalefaccion;
    }

    public void setConCalefaccion(Boolean conCalefaccion) {
        this.conCalefaccion = conCalefaccion;
    }

    public Boolean getConAireAcondicionado() {
        return conAireAcondicionado;
    }

    public void setConAireAcondicionado(Boolean conAireAcondicionado) {
        this.conAireAcondicionado = conAireAcondicionado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getDadoDeBaja() {
        return dadoDeBaja;
    }

    public void setDadoDeBaja(Boolean dadoDeBaja) {
        this.dadoDeBaja = dadoDeBaja;
    }

    public String getAbreviacionPisoPuerta() {
        return abreviacionPisoPuerta;
    }

    public void setAbreviacionPisoPuerta(String abreviacionPisoPuerta) {
        this.abreviacionPisoPuerta = abreviacionPisoPuerta;
    }

    public Double getSuperficie() {
        return superficie;
    }

    public void setSuperficie(Double superficie) {
        this.superficie = superficie;
    }

    public Boolean getViviendaLocal() {
        return viviendaLocal;
    }

    public void setViviendaLocal(Boolean viviendaLocal) {
        this.viviendaLocal = viviendaLocal;
    }

    public ArchivoAdjunto getPlanoDepartamento() {
        return planoDepartamento;
    }

    public void setPlanoDepartamento(ArchivoAdjunto planoDepartamento) {
        this.planoDepartamento = planoDepartamento;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    @Override
    public String getTextAsTreeItem() {
        return this.getPiso() + " " + this.getPuerta();
    }


    public static Comparator rm2idComparator = new Comparator<Departamento>() {
        @Override
        public int compare(Departamento o1, Departamento o2) {
            return o1.getRm2id().compareTo(o2.getRm2id());
        }
    };

    public Boolean getExcluirDeMonitorizacionNoEmitidosOAnomalos() {
        return excluirDeMonitorizacionNoEmitidosOAnomalos;
    }

    public void setExcluirDeMonitorizacionNoEmitidosOAnomalos(Boolean excluirDeMonitorizacionNoEmitidosOAnomalos) {
        this.excluirDeMonitorizacionNoEmitidosOAnomalos = excluirDeMonitorizacionNoEmitidosOAnomalos;
    }


}