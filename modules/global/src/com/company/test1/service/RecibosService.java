package com.company.test1.service;

import com.company.test1.entity.contratosinquilinos.ContratoInquilino;
import com.company.test1.entity.departamentos.Departamento;
import com.company.test1.entity.departamentos.Ubicacion;
import com.company.test1.entity.enums.recibos.DefinicionRemesaTipoGiroEnum;
import com.company.test1.entity.extroles.Propietario;
import com.company.test1.entity.recibos.*;

import java.util.Date;
import java.util.List;

public interface RecibosService {
    String NAME = "test1_RecibosService";

    List<Recibo> devuelveRecibosPendientesDeConciliacionZHelper(Date fechaDesde, Date fechaHasta) throws Exception;

    Recibo generaReciboParaContrato(ContratoInquilino contratoInquilino, OrdenanteRemesa ordentanteRemesa, Date fechaValorP, Serie serie) throws Exception;
    Remesa generaRemesaAcordeADatos(Date fechaRealizacion, Date fechaValor, Date fechaCargo, List<ContratoInquilino> contratos, Serie serie) throws Exception;

    boolean persisteRemesas(List<Remesa> rr) throws Exception;

    Double getTotalesRemesa(Remesa r);

    List<Recibo> devuelveImpagadosAsociados(List<Propietario> propietarios, List<Departamento> ddpp,
                                            Date fechaInicial, Date fechaFinal, String tipoContrato, String incobrables, Serie serie) throws Exception;
    Double getTotalIngresadoDeRecibo(Recibo r);

    String generaNuevoNumeroReciboSegunUbicacionYAno(Ubicacion ub, Date fecha) throws Exception;

    List<Recibo> devuelveRecibosSinRemesasParaDatos(Date fechaDesde, Date fechaHasta, DefinicionRemesaTipoGiroEnum tipoGiro);

    Remesa generaRemesaParaRecibos(List<Recibo> rbos, DefinicionRemesa dr, Date fechaValor, Date fechaAdeudo, Date fechaRealizacion, String identificadorRemesa);
    List<Recibo> getTodosLosRecibosAsociadosParaRemesas(List<Remesa> remesas);
    Double getTotalConceptoAdicionalAplicadoARecibo(String nombreCA, Recibo r);
    List<ImplementacionConcepto> getVersionAgregadaPorConceptos(List<ImplementacionConcepto> l);

    double getTotalCobranzas(Recibo r);
    double getTotalCobranzas(Recibo r, Date fechaInicial, Date fechaFinal);
    double getTotalCompensado(Recibo r);
    double getTotalCompensado(Recibo r, Date fechaInicial, Date fechaFinal);
    double getTotalPendiente(Recibo r);
    double getTotalPendiente(Recibo r, Date fechaInicial, Date fechaFinal);
    double getPorcentajeCobrado(Recibo r);
    Date getFechaDevuelto(Recibo r) throws Exception;
    double getTotalDevuelto(Recibo r);
    double getTotalDevuelto(Recibo r, Date fechaInicial, Date fechaFinal);
    double getTotalCobrado(Recibo recibo);
    double getTotalCobrado(Recibo recibo, Date fechaInicial, Date fechaFinal);

    Recibo getUltimoReciboGirado(Departamento d) throws Exception;
    Double getProrrateoUltimoContratoDepartamento(Departamento d) throws Exception;

    double getTotalesConceptoVigente(ProgramacionRecibo pr, Concepto c) throws Exception;

    void realizaNumeracionDeRecibosEnRemesas(List<Remesa> rr, Date fechaValor) throws Exception;

    List<Recibo> getRecibosAsociadosAUbicacionesEntreFechas(Date fechaDesde, Date fechaHasta, List<Ubicacion> ubs) throws Exception;

    List<Recibo> getRecibosAsociadosAUbicacionEntreFechas(Date fechaDesde, Date fechaHasta, Ubicacion ub) throws Exception;

    List<ImplementacionConcepto> getImplementacionesConceptosAgregadas(Recibo recibo);

    Concepto getConceptoDesdeAbreviacion(String abreviacion) throws Exception;

    /*PENDIENTE: Metodo habilitado para la generacion de notificaciones de aumentos de ipc pues el concepto
    esta duplicado en el historico. Para una misma abreviacion hay dos conceptos, y ello implica que si no
    selecciona exáctamnete el qeu toca no le salen las norificaciones por no encontrar conceptos de recibo.
    El día que se elimine esa duplicidad se puede elimiinar este metodo*/
    List<Concepto> getConceptosDesdeAbreviacion(String abreviacion) throws Exception;

    Integer getValorOrdenacionParaNuevoConcepto();

    String calculaIdentificadorRemesa(DefinicionRemesa dr, Date fechaRealizacion) throws Exception;

    Recibo generaReciboIndividualizado(ContratoInquilino ci, Date fechaEmision, Boolean incluirEnRemesa, Serie serie, List<ImplementacionConcepto> l) throws Exception;

    public Remesa devuelveRemesaDesdeIdentificador(String identificador) throws Exception;

    public String generaIdentificadorRemesa(String nombreDefinicionRemesa, Date fechaEmision, String abrevUbicacionDepto) throws Exception;

    public boolean registraReciboEnTablaZHelper(Recibo r) throws Exception;

    public boolean retrocedeRecibosEnZHelper(List<Recibo> rr) throws Exception;


    double getTotalIngresadoAdministracion(Recibo recibo, Date fechaInicial, Date fechaFinal);
    double getTotalIngresadoBancario(Recibo recibo, Date fechaInicial, Date fechaFinal);
    double getTotalIngresadoAdministracion(Recibo recibo);
    double getTotalIngresadoBancario(Recibo recibo);
}