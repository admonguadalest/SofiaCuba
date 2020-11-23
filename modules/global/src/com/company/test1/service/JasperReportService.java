package com.company.test1.service;

import com.company.test1.entity.coeficientes.TipoCoeficiente;
import com.company.test1.entity.contratosinquilinos.Anexo;
import com.company.test1.entity.contratosinquilinos.ContratoInquilino;
import com.company.test1.entity.departamentos.Departamento;
import com.company.test1.entity.departamentos.Ubicacion;
import com.company.test1.entity.extroles.Propietario;
import com.company.test1.entity.ordenespago.RealizacionPago;
import com.company.test1.entity.recibos.*;
import com.company.test1.entity.reportsyplantillas.FlexReport;
import com.haulmont.cuba.core.entity.Entity;


import java.util.*;

public interface JasperReportService {
    String NAME = "test1_JasperReportService";

    byte[] createReport() throws Exception;
    byte[] departamentosVacios(List<Departamento> dd) throws Exception;
    FlexReport getFlexReportDesdeNombre(String nombre);
    byte[] listadoResumenRecibos(List<Remesa> remesas) throws Exception;
    byte[] listadoResumenRecibosFromZHelper(List<Remesa> remesas) throws Exception;
    byte[] getReportDinamico(String titulo, Class baseClass, Collection<Entity> entities, List<String> idPaths, List<String> colnames) throws Exception;
    Object turnFileIntoJRRenderableObject(String fileName) throws Exception;
    byte[] getReportRecibo(Recibo r) throws Exception;
    byte[] impagados(Propietario p, List<Recibo> impagados, Date fechaInicial, Date fechaFinal, String vigencia, String tipoRecibos) throws Exception;
    byte[] reportIncrementosGenerales(Ubicacion ub, Hashtable<Departamento, ConceptoRecibo[]> htDeptosCCRR, Concepto concepto, TipoCoeficiente tipoCoef,
                                      Integer mesesAtrasos, Date fechaRepercusion, Double importeRepercusion);

    byte[] reportIncrementosIndiceReferencia(ArrayList al_, Integer mesesAtrasos, Integer mes, Integer anno, Date fechaRepercusion);

    byte[] reportDiferenciasEntreEmisiones(List<ContratoInquilino> ccii, Date fechaRealizacion, Date fechaValor, Date fechaCargo, Date fechaDesdePeriodoAnterior, Date fechaHastaPeriodoAnterior, Serie serie) throws Exception;

    byte[] devuelveReportPrevisualizacionRecibo(Recibo r) throws Exception;

    byte[] devuelveReportInformeIVA(List<Departamento> deptos, Date fechaDesde, Date fechaHasta, Propietario prop, boolean imprimirTrimestre, boolean imprimirTotalesGlobal) throws Exception;

    byte[] reportMod347(Propietario propietario, Date fechaDesde, Date fechaHasta) throws Exception;

    byte[] reportListadoAumentos(List<ContratoInquilino> ccii, Date fechaDesde, Date fechaHasta);

    byte[] produceReport(FlexReport fr, Hashtable baseEntrantes, List beans) throws Exception;

    byte[] realizaImprimiblesContratoInquilino(Boolean[] bools, ContratoInquilino ci);

    byte[] realizaImpresionAnexo(Anexo a) throws Exception;

    byte[] realizaInformeIva(Date fechaDesde, Date fechaHasta, Propietario propietario, List<Departamento> departamentos, boolean anadirInfoTrimestral, boolean anadirInfoGlobal) throws Exception;

    byte[] realizaReportRealizacionPago(RealizacionPago rp) throws Exception;

    public byte[] generaReportModeloRenunciaContratoInquilino(ContratoInquilino ci) throws Exception;
}