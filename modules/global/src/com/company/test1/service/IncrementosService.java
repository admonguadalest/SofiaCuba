package com.company.test1.service;

import com.company.test1.entity.coeficientes.TipoCoeficiente;
import com.company.test1.entity.contratosinquilinos.ContratoInquilino;
import com.company.test1.entity.departamentos.Departamento;
import com.company.test1.entity.enums.IncrementoGeneralObrasModoReparticion;
import com.company.test1.entity.enums.TipoContratoInquilinoEnum;
import com.company.test1.entity.recibos.Concepto;
import com.company.test1.entity.recibos.ConceptoRecibo;
import com.company.test1.entity.recibos.ProgramacionRecibo;
import com.company.test1.entity.recibos.RegistroIndiceReferencia;

import java.util.Date;
import java.util.List;

public interface IncrementosService {
    String NAME = "test1_IncrementosService";

    double devuelveCoeficienteDepartamentoSobreTotalUbicacion(Departamento d, TipoCoeficiente tc) throws Exception;
    ConceptoRecibo[] creaConceptosReciboParaIncrementosGeneralesYObras(
            Concepto concepto,
            ContratoInquilino contratoInquilino,
            List<Departamento> deptos,
            TipoCoeficiente tipoCoeficiente,
            Date fechaAplicacionIncremento,
            Double importeAumento,
            TipoContratoInquilinoEnum disciminadorLau,
            Boolean aplicacion12PrcContratosIndefinidos,
            Boolean actualizableIPC,
            IncrementoGeneralObrasModoReparticion modoReparto,
            Concepto conceptoAtrasos,
            Integer nMesesAtrasos,
            String descripcionIncremento,
            Object[] infoVigencia
    )
            throws Exception;
    void realizaAdjuntacionConceptosAdicionalesConceptoReciboEnConceptoReciboSegunConceptoRecibo(ConceptoRecibo cr, String nombreConceptoReciboBase)
            throws Exception;

    ConceptoRecibo[] creaConceptosReciboParaIncrementosIndiceReferencia(
            Concepto concepto,
            ContratoInquilino contratoInquilino,
            Integer mes,
            Integer anno,
            Date fechaAplicacionIncremento,
            Concepto conceptoAtrasos,
            Integer nMesesAtrasos

    ) throws Exception;

    String calculaProximoValorMesAnyoAplicacionIPC(ContratoInquilino c) throws Exception;

    List<ConceptoRecibo> getConceptosReciboActualizablesIPC(ProgramacionRecibo pr);

    RegistroIndiceReferencia getRegistroForValues(Integer mes, Integer anno, String nombreTipo) throws Exception;
}