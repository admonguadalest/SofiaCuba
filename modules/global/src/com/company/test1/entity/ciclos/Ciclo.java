package com.company.test1.entity.ciclos;

import com.company.test1.entity.ColeccionArchivosAdjuntos;
import com.company.test1.entity.contratosinquilinos.ContratoInquilino;
import com.company.test1.entity.departamentos.Departamento;
import com.company.test1.entity.documentosfotograficos.CarpetaDocumentosFotograficos;
import com.company.test1.entity.enums.EstadoCicloEnum;
import com.company.test1.entity.enums.TipoCiclo;
import com.company.test1.validations.ciclos.CicloBean;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;
import com.haulmont.cuba.core.global.validation.groups.UiCrossFieldChecks;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Table(name = "CICLO")
@Entity(name = "test1_Ciclo")
@CicloBean(groups = UiCrossFieldChecks.class)

public class Ciclo extends StandardEntity {
    private static final long serialVersionUID = -1564647085234250110L;

    @NotNull(message = "Aportar codigo de ciclo")
    @Column(name = "CODIGO_CICLO", length = 50)
    protected String codigoCiclo;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "ciclo")
    protected List<Evento> eventos = new ArrayList<Evento>();

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "ciclo")
    protected List<ImputacionDocumentoImputable> imputacionesDocumentoImputable = new ArrayList<ImputacionDocumentoImputable>();

    @Lob
    @Column(name = "DESCRIPCION")
    protected String descripcion;

    @Column(name = "ES_CICLO_CORRIENTE")
    protected Boolean esCicloCorriente;

    @NotNull(message = "Especificar estado de ciclo")
    @Column(name = "ESTADO_CICLO")
    protected Integer estadoCiclo;

    @NotNull(message = "Aportar Fecha de Ciclo")
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_CICLO")
    protected Date fechaCiclo;

    @Lob
    @Column(name = "OBSERVACIONES")
    protected String observaciones;

    @NotNull(message = "Aportar titulo para ciclo")
    @Column(name = "TITULO_CICLO")
    protected String tituloCiclo;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COLECCION_ADJUNTOS_ID")
    protected ColeccionArchivosAdjuntos coleccionAdjuntos = null;

    @NotNull(message = "Asociar departamento")
    @Lookup(type = LookupType.SCREEN, actions = {"lookup", "clear"})
    @OnDeleteInverse(DeletePolicy.CASCADE)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEPARTAMENTO_ID")
    protected Departamento departamento;

    @NotNull(message = "Indicar tipo de ciclo")
    @Column(name = "TIPO_CICLO")
    protected String tipoCiclo;

    @JoinColumn(name = "CONTRATO_INQUILINO_ID")
    @Lookup(type = LookupType.SCREEN)
    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.UNLINK)
    @OneToOne(fetch = FetchType.LAZY)
    protected ContratoInquilino contratoInquilino;

    @Column(name = "EXCLUIR_DE_MONITORIZACION_PARA_BUSQUEDA_ORDENES_TRABAJO")
    protected Boolean excluirDeMonitorizacionParaBusquedaOrdenesTrabajo;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "ciclo")
    protected List<Entrada> entradas = new ArrayList<Entrada>();

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "ciclo")
    protected List<CarpetaDocumentosFotograficos> carpetasDocumentosFotograficos = new ArrayList<CarpetaDocumentosFotograficos>();

    @Column(name = "RM2ID")
    protected Integer rm2id;

    public Integer getRm2id() {
        return rm2id;
    }

    public void setRm2id(Integer rm2id) {
        this.rm2id = rm2id;
    }

    public List<CarpetaDocumentosFotograficos> getCarpetasDocumentosFotograficos() {
        return carpetasDocumentosFotograficos;
    }

    public void setCarpetasDocumentosFotograficos(List<CarpetaDocumentosFotograficos> carpetasDocumentosFotograficos) {
        this.carpetasDocumentosFotograficos = carpetasDocumentosFotograficos;
    }

    public void setFechaCiclo(Date fechaCiclo) {
        this.fechaCiclo = fechaCiclo;
    }

    public Date getFechaCiclo() {
        return fechaCiclo;
    }

    public List<ImputacionDocumentoImputable> getImputacionesDocumentoImputable() {
        return imputacionesDocumentoImputable;
    }

    public void setImputacionesDocumentoImputable(List<ImputacionDocumentoImputable> imputacionesDocumentoImputable) {
        this.imputacionesDocumentoImputable = imputacionesDocumentoImputable;
    }

    public List<Evento> getEventos() {
        return eventos;
    }

    public void setEventos(List<Evento> eventos) {
        this.eventos = eventos;
    }

    public List<Entrada> getEntradas() {
        return entradas;
    }

    public void setEntradas(List<Entrada> entradas) {
        this.entradas = entradas;
    }

    public Boolean getExcluirDeMonitorizacionParaBusquedaOrdenesTrabajo() {
        return excluirDeMonitorizacionParaBusquedaOrdenesTrabajo;
    }

    public void setExcluirDeMonitorizacionParaBusquedaOrdenesTrabajo(Boolean excluirDeMonitorizacionParaBusquedaOrdenesTrabajo) {
        this.excluirDeMonitorizacionParaBusquedaOrdenesTrabajo = excluirDeMonitorizacionParaBusquedaOrdenesTrabajo;
    }

    public ContratoInquilino getContratoInquilino() {
        return contratoInquilino;
    }

    public void setContratoInquilino(ContratoInquilino contratoInquilino) {
        this.contratoInquilino = contratoInquilino;
    }

    public TipoCiclo getTipoCiclo() {
        return tipoCiclo == null ? null : TipoCiclo.fromId(tipoCiclo);
    }

    public void setTipoCiclo(TipoCiclo tipoCiclo) {
        this.tipoCiclo = tipoCiclo == null ? null : tipoCiclo.getId();
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public ColeccionArchivosAdjuntos getColeccionAdjuntos() {
        return coleccionAdjuntos;
    }

    public void setColeccionAdjuntos(ColeccionArchivosAdjuntos coleccionAdjuntos) {
        this.coleccionAdjuntos = coleccionAdjuntos;
    }

    public String getTituloCiclo() {
        return tituloCiclo;
    }

    public void setTituloCiclo(String tituloCiclo) {
        this.tituloCiclo = tituloCiclo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public EstadoCicloEnum getEstadoCiclo() {
        return estadoCiclo == null ? null : EstadoCicloEnum.fromId(estadoCiclo);
    }

    public void setEstadoCiclo(EstadoCicloEnum estadoCiclo) {
        this.estadoCiclo = estadoCiclo == null ? null : estadoCiclo.getId();
    }

    public Boolean getEsCicloCorriente() {
        return esCicloCorriente;
    }

    public void setEsCicloCorriente(Boolean esCicloCorriente) {
        this.esCicloCorriente = esCicloCorriente;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCodigoCiclo() {
        return codigoCiclo;
    }

    public void setCodigoCiclo(String codigoCiclo) {
        this.codigoCiclo = codigoCiclo;
    }
}