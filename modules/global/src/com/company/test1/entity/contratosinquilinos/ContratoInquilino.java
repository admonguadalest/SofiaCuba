package com.company.test1.entity.contratosinquilinos;

import com.company.test1.entity.ArchivoAdjunto;
import com.company.test1.entity.AsTreeItem;
import com.company.test1.entity.ColeccionArchivosAdjuntos;
import com.company.test1.entity.Persona;
import com.company.test1.entity.ciclos.Ciclo;
import com.company.test1.entity.departamentos.Departamento;
import com.company.test1.entity.documentosfotograficos.CarpetaDocumentosFotograficos;
import com.company.test1.entity.enums.EstadoContratoInquilinoEnum;
import com.company.test1.entity.enums.FormaDeCobroContratoInquilinoEnum;
import com.company.test1.entity.enums.TipoContratoInquilinoEnum;
import com.company.test1.entity.enums.UsoContratoEnum;
import com.company.test1.entity.extroles.ComercialOfertas;
import com.company.test1.entity.ordenespago.OrdenPagoContratoInquilino;
import com.company.test1.entity.recibos.ProgramacionRecibo;
import com.company.test1.entity.recibos.Recibo;
import com.company.test1.service.NotificacionService;
import com.company.test1.validations.contratos.ContratoInquilinoBean;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.*;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DeletePolicy;
import com.haulmont.cuba.core.global.validation.groups.UiCrossFieldChecks;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@PublishEntityChangedEvents
@Table(name = "CONTRATO_INQUILINO")
@Entity(name = "test1_ContratoInquilino")
@ContratoInquilinoBean(groups = UiCrossFieldChecks.class)
@Listeners("test1_ContratoInquilinoEntityListener")
public class ContratoInquilino extends StandardEntity implements AsTreeItem {
    private static final long serialVersionUID = 2132833279630066267L;

    @JoinColumn(name = "CICLO_ID")
    @Lookup(type = LookupType.SCREEN, actions = {"lookup", "clear"})
    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.UNLINK)
    @OneToOne(fetch = FetchType.LAZY)
    protected Ciclo ciclo;

    @Column(name = "ARRENDATARIO_SIN_REPRESENTACION")
    protected Boolean arrendatarioSinRepresentacion;

    @NotNull(message = "Especificar Estado de Contrato")
    @Column(name = "ESTADO_CONTRATO")
    protected Integer estadoContrato;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_DESOCUPACION")
    protected Date fechaDesocupacion;

    @NotNull(message = "Indicar Fecha de Ocupacion")
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_OCUPACION")
    protected Date fechaOcupacion;

    @NotNull(message = "Indicar Fecha de Realizacion")
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_REALIZACION")
    protected Date fechaRealizacion;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_VENCIMIENTO_PREVISTO")
    protected Date fechaVencimientoPrevisto;

    @Column(name = "FORMA_DE_COBRO")
    protected Integer formaDeCobro;

    @NotNull(message = "Aportar Lugar de Realizacion")
    @Column(name = "LUGAR_REALIZACION")
    protected String lugarRealizacion;

    @NotNull(message = "Indicar mes/anno de referencia de aplicacion de IPC")
    @Column(name = "MES_ANYO_APLICACION_IPC", length = 50)
    protected String mesAnyoAplicacionIPC;

    @NotNull(message = "Indicar numero de contrato")
    @Column(name = "NUMERO_CONTRATO")
    protected String numeroContrato;

    @Column(name = "OMITIR_IPC_NEGATIVO")
    protected Boolean omitirIPCNegativo;

    @NotNull(message = "Indicar el periodo de actualizacion de IPC")
    @Column(name = "PERIODO_ACTUALIZACION_IPC")
    protected Integer periodoActualizacionIPC;

    @NotNull(message = "Indicar el plazo en Anyos")
    @Column(name = "PLAZO_ANYOS")
    protected Integer plazoAnyos;

    @NotNull(message = "Indicar plazo prorrogable de contrato")
    @Column(name = "PLAZO_ANYOS_PRORROGABLE")
    protected Integer plazoAnyosProrrogable;

    @NotNull(message = "Indicar la renta contractual")
    @Column(name = "RENTA_CONTRACTUAL")
    protected Double rentaContractual;

    @NotNull(message = "Indicar tipo de contrato")
    @Column(name = "TIPO_CONTRATO")
    protected Integer tipoContrato;

    @NotNull(message = "Uso de vivienda o local")
    @Column(name = "USO_CONTRATO")
    protected Integer usoContrato;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COLECCION_ARCHIVOS_ADJUNTOS_ID")
    protected ColeccionArchivosAdjuntos coleccionArchivosAdjuntos;

    @Lob
    @Column(name = "DETALLE_ENTREGA_LLAVES")
    protected String detalleEntregaLlaves;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PAGADOR_ID")
    protected Persona pagador;

    @NotNull(message = "Debe asignar un Departamento objeto del Contrato")
    @OnDeleteInverse(DeletePolicy.DENY)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEPARTAMENTO_ID")
    protected Departamento departamento;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ESCANEO_CONTRATO_ID")
    protected ArchivoAdjunto escaneoContrato;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IMPLEMENTACION_MODELO_ID")
    protected ImplementacionModelo implementacionModelo;

    @NotNull(message = "Seleccionar Inquilino")
    @OnDeleteInverse(DeletePolicy.DENY)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "INQUILINO_ID")
    protected Persona inquilino;

    @OnDeleteInverse(DeletePolicy.DENY)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REPRESENTANTE_ARRENDADOR_ID")
    protected Persona representanteArrendador;

    @OnDeleteInverse(DeletePolicy.DENY)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REPRESENTANTE_ARRENDATARIO_ID")
    protected Persona representanteArrendatario;

    @NotNull(message = "Especificar Indice de Referencia de Incrementos")
    @Column(name = "NOMBRE_TIPO_INDICE_INCREMENTOS", length = 10)
    protected String nombreTipoIndiceIncrementos;

    @NotNull(message = "Especificar si el Pagador es el titular")
    @Column(name = "EL_PAGADOR_ES_EL_TITULAR")
    protected Boolean elPagadorEsElTitular = true;

    @Column(name = "PERMITIR_APUNTES_PROCESOS_AGREGADOS")
    protected Boolean permitirApuntesProcesosAgregados = true;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMERCIAL_OFERTAS_ID")
    protected ComercialOfertas comercialOfertas;

    @Column(name = "EXCLUIR_MONITORIZACION_PTES_DEV")
    protected Boolean excluirDeMonitorizacionDeRecibosPendientesYDevueltos = false;

    @Column(name = "DEVENTO_COSTES_POSTALES")
    protected Boolean esteContratoDevengaCostesPostales = false;

    @Column(name = "RECIBOS_INCOBRABLES")
    protected Boolean recibosIncobrables = false;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_MANDATO")
    protected Date fechaMandato;

    @Column(name = "REFERENCIA_MANDATO")
    protected String referenciaMandato;

    @Column(name = "COMUNICACION_RENUNCIA")
    protected Boolean comunicacionRenuncia = false;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_PREVISTA_RENUNCIA")
    protected Date fechaPrevistaRenuncia;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.UNLINK)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OFERTA_ID")
    protected Oferta oferta;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.UNLINK)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ARCHIVO_ADJUNTO_RENUNCIA_ID")
    protected ArchivoAdjunto archivoAdjuntoRenuncia;

    @Lob
    @Column(name = "OBSERVACIONES_RENUNCIA")
    protected String observacionesRenuncia;

    @Lob
    @Column(name = "COMENTARIOS_ADMINISTRADOR")
    protected String comentariosAdministrador;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_COMUNICACION")
    protected Date fechaComunicacion;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CARPETA_DOCUMENTO_FOTOGRAFICO_FIRMA_ID")
    protected CarpetaDocumentosFotograficos carpetaDocumentoFotograficoFirma;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "contratoInquilino")
    protected List<CotitularContratoInquilino> cotitulares = new ArrayList<CotitularContratoInquilino>();

    @NotNull(message = "No puede existir un Contrato sin Fianza asociada")
    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "contratoInquilino")
    protected Fianza fianza = new Fianza(this);

    @NotNull(message = "Debe asociar un ProgramacionRecibo a Contrato")
    @OnDeleteInverse(DeletePolicy.DENY)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "contratoInquilino")
    protected ProgramacionRecibo programacionRecibo = new ProgramacionRecibo(this);

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "contratoInquilino")
    protected LiquidacionExtincion liquidacionExtincion = new LiquidacionExtincion(this);

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "contratoInquilino")
    protected LiquidacionSuscripcion liquidacionSuscripcion = new LiquidacionSuscripcion(this);

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "contratoInquilino")
    protected List<Anexo> anexos = new ArrayList<Anexo>();

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "contratoInquilino")
    protected List<Subrogador> subrogadores = new ArrayList<Subrogador>();

    @Column(name = "RM2ID")
    protected Integer rm2id;

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "contratoInquilino")
    protected List<OrdenPagoContratoInquilino> ordenesPagoContratoInquilino;

    public List<OrdenPagoContratoInquilino> getOrdenesPagoContratoInquilino() {
        return ordenesPagoContratoInquilino;
    }

    public void setOrdenesPagoContratoInquilino(List<OrdenPagoContratoInquilino> ordenesPagoContratoInquilino) {
        this.ordenesPagoContratoInquilino = ordenesPagoContratoInquilino;
    }

    public void setFechaDesocupacion(Date fechaDesocupacion) {
        this.fechaDesocupacion = fechaDesocupacion;
    }

    public Date getFechaDesocupacion() {
        return fechaDesocupacion;
    }

    public void setFechaOcupacion(Date fechaOcupacion) {
        this.fechaOcupacion = fechaOcupacion;
    }

    public Date getFechaOcupacion() {
        return fechaOcupacion;
    }

    public void setFechaRealizacion(Date fechaRealizacion) {
        this.fechaRealizacion = fechaRealizacion;
    }

    public Date getFechaRealizacion() {
        return fechaRealizacion;
    }

    public void setFechaVencimientoPrevisto(Date fechaVencimientoPrevisto) {
        this.fechaVencimientoPrevisto = fechaVencimientoPrevisto;
    }

    public Date getFechaVencimientoPrevisto() {
        return fechaVencimientoPrevisto;
    }

    public void setFechaMandato(Date fechaMandato) {
        this.fechaMandato = fechaMandato;
    }

    public Date getFechaMandato() {
        return fechaMandato;
    }

    public void setFechaPrevistaRenuncia(Date fechaPrevistaRenuncia) {
        this.fechaPrevistaRenuncia = fechaPrevistaRenuncia;
    }

    public Date getFechaPrevistaRenuncia() {
        return fechaPrevistaRenuncia;
    }

    public void setFechaComunicacion(Date fechaComunicacion) {
        this.fechaComunicacion = fechaComunicacion;
    }

    public Date getFechaComunicacion() {
        return fechaComunicacion;
    }

    public Integer getRm2id() {
        return rm2id;
    }

    public void setRm2id(Integer rm2id) {
        this.rm2id = rm2id;
    }

    public List<Subrogador> getSubrogadores() {
        return subrogadores;
    }

    public void setSubrogadores(List<Subrogador> subrogadores) {
        this.subrogadores = subrogadores;
    }

    public List<Anexo> getAnexos() {
        return anexos;
    }

    public void setAnexos(List<Anexo> anexos) {
        this.anexos = anexos;
    }

    public LiquidacionSuscripcion getLiquidacionSuscripcion() {
        return liquidacionSuscripcion;
    }

    public void setLiquidacionSuscripcion(LiquidacionSuscripcion liquidacionSuscripcion) {
        this.liquidacionSuscripcion = liquidacionSuscripcion;
    }

    public LiquidacionExtincion getLiquidacionExtincion() {
        return liquidacionExtincion;
    }

    public void setLiquidacionExtincion(LiquidacionExtincion liquidacionExtincion) {
        this.liquidacionExtincion = liquidacionExtincion;
    }

    public ProgramacionRecibo getProgramacionRecibo() {
        return programacionRecibo;
    }

    public void setProgramacionRecibo(ProgramacionRecibo programacionRecibo) {
        this.programacionRecibo = programacionRecibo;
    }

    public Fianza getFianza() {
        return fianza;
    }

    public void setFianza(Fianza fianza) {
        this.fianza = fianza;
    }

    public List<CotitularContratoInquilino> getCotitulares() {
        return cotitulares;
    }

    public void setCotitulares(List<CotitularContratoInquilino> cotitulares) {
        this.cotitulares = cotitulares;
    }

    public CarpetaDocumentosFotograficos getCarpetaDocumentoFotograficoFirma() {
        return carpetaDocumentoFotograficoFirma;
    }

    public void setCarpetaDocumentoFotograficoFirma(CarpetaDocumentosFotograficos carpetaDocumentoFotograficoFirma) {
        this.carpetaDocumentoFotograficoFirma = carpetaDocumentoFotograficoFirma;
    }

    public String getComentariosAdministrador() {
        return comentariosAdministrador;
    }

    public void setComentariosAdministrador(String comentariosAdministrador) {
        this.comentariosAdministrador = comentariosAdministrador;
    }

    public String getObservacionesRenuncia() {
        return observacionesRenuncia;
    }

    public void setObservacionesRenuncia(String observacionesRenuncia) {
        this.observacionesRenuncia = observacionesRenuncia;
    }

    public ArchivoAdjunto getArchivoAdjuntoRenuncia() {
        return archivoAdjuntoRenuncia;
    }

    public void setArchivoAdjuntoRenuncia(ArchivoAdjunto archivoAdjuntoRenuncia) {
        this.archivoAdjuntoRenuncia = archivoAdjuntoRenuncia;
    }

    public Oferta getOferta() {
        return oferta;
    }

    public void setOferta(Oferta oferta) {
        this.oferta = oferta;
    }

    public Boolean getComunicacionRenuncia() {
        return comunicacionRenuncia;
    }

    public void setComunicacionRenuncia(Boolean comunicacionRenuncia) {
        this.comunicacionRenuncia = comunicacionRenuncia;
    }

    public String getReferenciaMandato() {
        return referenciaMandato;
    }

    public void setReferenciaMandato(String referenciaMandato) {
        this.referenciaMandato = referenciaMandato;
    }

    public Boolean getRecibosIncobrables() {
        return recibosIncobrables;
    }

    public void setRecibosIncobrables(Boolean recibosIncobrables) {
        this.recibosIncobrables = recibosIncobrables;
    }

    public Boolean getEsteContratoDevengaCostesPostales() {
        return esteContratoDevengaCostesPostales;
    }

    public void setEsteContratoDevengaCostesPostales(Boolean esteContratoDevengaCostesPostales) {
        this.esteContratoDevengaCostesPostales = esteContratoDevengaCostesPostales;
    }

    public Boolean getExcluirDeMonitorizacionDeRecibosPendientesYDevueltos() {
        return excluirDeMonitorizacionDeRecibosPendientesYDevueltos;
    }

    public void setExcluirDeMonitorizacionDeRecibosPendientesYDevueltos(Boolean excluirDeMonitorizacionDeRecibosPendientesYDevueltos) {
        this.excluirDeMonitorizacionDeRecibosPendientesYDevueltos = excluirDeMonitorizacionDeRecibosPendientesYDevueltos;
    }

    public ComercialOfertas getComercialOfertas() {
        return comercialOfertas;
    }

    public void setComercialOfertas(ComercialOfertas comercialOfertas) {
        this.comercialOfertas = comercialOfertas;
    }

    public Boolean getPermitirApuntesProcesosAgregados() {
        return permitirApuntesProcesosAgregados;
    }

    public void setPermitirApuntesProcesosAgregados(Boolean permitirApuntesProcesosAgregados) {
        this.permitirApuntesProcesosAgregados = permitirApuntesProcesosAgregados;
    }

    public Boolean getElPagadorEsElTitular() {
        return elPagadorEsElTitular;
    }

    public void setElPagadorEsElTitular(Boolean elPagadorEsElTitular) {
        this.elPagadorEsElTitular = elPagadorEsElTitular;
    }

    public String getNombreTipoIndiceIncrementos() {
        return nombreTipoIndiceIncrementos;
    }

    public void setNombreTipoIndiceIncrementos(String nombreTipoIndiceIncrementos) {
        this.nombreTipoIndiceIncrementos = nombreTipoIndiceIncrementos;
    }

    public Persona getPagador() {
        return pagador;
    }

    public void setPagador(Persona pagador) {
        this.pagador = pagador;
    }

    public String getDetalleEntregaLlaves() {
        return detalleEntregaLlaves;
    }

    public void setDetalleEntregaLlaves(String detalleEntregaLlaves) {
        this.detalleEntregaLlaves = detalleEntregaLlaves;
    }

    public Persona getRepresentanteArrendatario() {
        return representanteArrendatario;
    }

    public void setRepresentanteArrendatario(Persona representanteArrendatario) {
        this.representanteArrendatario = representanteArrendatario;
    }

    public Persona getRepresentanteArrendador() {
        return representanteArrendador;
    }

    public void setRepresentanteArrendador(Persona representanteArrendador) {
        this.representanteArrendador = representanteArrendador;
    }

    public Persona getInquilino() {
        return inquilino;
    }

    public void setInquilino(Persona inquilino) {
        this.inquilino = inquilino;
    }

    public ImplementacionModelo getImplementacionModelo() {
        return implementacionModelo;
    }

    public void setImplementacionModelo(ImplementacionModelo implementacionModelo) {
        this.implementacionModelo = implementacionModelo;
    }

    public ArchivoAdjunto getEscaneoContrato() {
        return escaneoContrato;
    }

    public void setEscaneoContrato(ArchivoAdjunto escaneoContrato) {
        this.escaneoContrato = escaneoContrato;
    }

    public void setTipoContrato(TipoContratoInquilinoEnum tipoContrato) {
        this.tipoContrato = tipoContrato == null ? null : tipoContrato.getId();
    }

    public TipoContratoInquilinoEnum getTipoContrato() {
        return tipoContrato == null ? null : TipoContratoInquilinoEnum.fromId(tipoContrato);
    }

    public void setUsoContrato(UsoContratoEnum usoContrato) {
        this.usoContrato = usoContrato == null ? null : usoContrato.getId();
    }

    public UsoContratoEnum getUsoContrato() {
        return usoContrato == null ? null : UsoContratoEnum.fromId(usoContrato);
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public ColeccionArchivosAdjuntos getColeccionArchivosAdjuntos() {
        return coleccionArchivosAdjuntos;
    }

    public void setColeccionArchivosAdjuntos(ColeccionArchivosAdjuntos coleccionArchivosAdjuntos) {
        this.coleccionArchivosAdjuntos = coleccionArchivosAdjuntos;
    }

    public Double getRentaContractual() {
        return rentaContractual;
    }

    public void setRentaContractual(Double rentaContractual) {
        this.rentaContractual = rentaContractual;
    }

    public Integer getPlazoAnyosProrrogable() {
        return plazoAnyosProrrogable;
    }

    public void setPlazoAnyosProrrogable(Integer plazoAnyosProrrogable) {
        this.plazoAnyosProrrogable = plazoAnyosProrrogable;
    }

    public Integer getPlazoAnyos() {
        return plazoAnyos;
    }

    public void setPlazoAnyos(Integer plazoAnyos) {
        this.plazoAnyos = plazoAnyos;
    }

    public Integer getPeriodoActualizacionIPC() {
        return periodoActualizacionIPC;
    }

    public void setPeriodoActualizacionIPC(Integer periodoActualizacionIPC) {
        this.periodoActualizacionIPC = periodoActualizacionIPC;
    }

    public Boolean getOmitirIPCNegativo() {
        return omitirIPCNegativo;
    }

    public void setOmitirIPCNegativo(Boolean omitirIPCNegativo) {
        this.omitirIPCNegativo = omitirIPCNegativo;
    }

    public String getNumeroContrato() {
        return numeroContrato;
    }

    public void setNumeroContrato(String numeroContrato) {
        this.numeroContrato = numeroContrato;
    }

    public String getMesAnyoAplicacionIPC() {
        return mesAnyoAplicacionIPC;
    }

    public void setMesAnyoAplicacionIPC(String mesAnyoAplicacionIPC) {
        this.mesAnyoAplicacionIPC = mesAnyoAplicacionIPC;
    }

    public String getLugarRealizacion() {
        return lugarRealizacion;
    }

    public void setLugarRealizacion(String lugarRealizacion) {
        this.lugarRealizacion = lugarRealizacion;
    }

    public FormaDeCobroContratoInquilinoEnum getFormaDeCobro() {
        return formaDeCobro == null ? null : FormaDeCobroContratoInquilinoEnum.fromId(formaDeCobro);
    }

    public void setFormaDeCobro(FormaDeCobroContratoInquilinoEnum formaDeCobro) {
        this.formaDeCobro = formaDeCobro == null ? null : formaDeCobro.getId();
    }

    public EstadoContratoInquilinoEnum getEstadoContrato() {
        return estadoContrato == null ? null : EstadoContratoInquilinoEnum.fromId(estadoContrato);
    }

    public void setEstadoContrato(EstadoContratoInquilinoEnum estadoContrato) {
        this.estadoContrato = estadoContrato == null ? null : estadoContrato.getId();
    }

    public Boolean getArrendatarioSinRepresentacion() {
        return arrendatarioSinRepresentacion;
    }

    public void setArrendatarioSinRepresentacion(Boolean arrendatarioSinRepresentacion) {
        this.arrendatarioSinRepresentacion = arrendatarioSinRepresentacion;
    }

    public Ciclo getCiclo() {
        return ciclo;
    }

    public void setCiclo(Ciclo ciclo) {
        this.ciclo = ciclo;
    }

    @Override
    public String getTextAsTreeItem() {
        return this.getDepartamento().getPiso() + " " + this.getDepartamento().getPuerta();
    }

    public static Persona getPagadorEfectivo(ContratoInquilino ci) throws Exception{
        if (ci.getPagador()!=null){
            return ci.getPagador();
        }else{
            return getInquilinoEfectivo(ci);
        }
    }

    public static Persona getInquilinoEfectivo(ContratoInquilino ci) throws Exception{
        if (ci.getSubrogadores().size()==0){
            return ci.getInquilino();
        }else{
            for (int i = 0; i < ci.getSubrogadores().size(); i++) {
                Subrogador s = ci.getSubrogadores().get(i);
                if (s.getFechaHasta()==null){
                    return s.getSubrogador();
                }

            }
        }
        throw new Exception("No se hallo un subrogador vigente");
    }

    public int getNumRecibosPendientes() throws Exception{

        List l = AppBeans.get(NotificacionService.class).getRecibosPendientes(this);
        return l.size();
    }

    public String getTextoRecibosPendientes() throws Exception{
        List<Recibo> l = AppBeans.get(NotificacionService.class).getRecibosPendientes(this);
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        String texto = "<p>";
        for (int i = 0; i < l.size(); i++) {
            Recibo recibo = l.get(i);
            texto += df.format(recibo.getFechaEmision()) + " " + nf.format(recibo.getTotalPendiente() - recibo.getTotalCobrado()) + "<br/>";
        }
        texto += "</p>";
        return texto;
    }

    public String getImporteTotalPendienteFormateado() throws Exception{
        List<Recibo> l = AppBeans.get(NotificacionService.class).getRecibosPendientes(this);
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        double total = 0.0;
        for (int i = 0; i < l.size(); i++) {
            Recibo recibo = l.get(i);
            total += recibo.getTotalPendiente();
        }

        return nf.format(total);
    }

}