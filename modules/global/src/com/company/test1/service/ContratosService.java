package com.company.test1.service;

import com.company.test1.entity.contratosinquilinos.ContratoInquilino;
import com.company.test1.entity.departamentos.Departamento;
import com.company.test1.entity.extroles.Propietario;
import com.company.test1.entity.recibos.Concepto;
import com.company.test1.entity.recibos.ConceptoRecibo;
import com.company.test1.entity.recibos.DefinicionRemesa;

import java.util.Date;
import java.util.Hashtable;
import java.util.List;

public interface ContratosService {
    String NAME = "test1_ContratosService";

    ContratoInquilino devuelveContratoVigenteParaDepartamento(Departamento d) throws Exception;

    ContratoInquilino devuelveContratoVigenteParaDepartamento(Departamento d, String viewName) throws Exception;

    List<ContratoInquilino> devuelveContratosParaPropietarios(List<DefinicionRemesa> ddrr, List<Propietario> props) throws Exception;

    Hashtable<ContratoInquilino,List<ConceptoRecibo>> getConceptosRecibosGeneradosEntreFechasParaConceptos(List<ContratoInquilino> contratos, Date fechaDesde, Date fechaHasta, List<Concepto> conceptos);

    String getNuevoNumeroParaContrato(ContratoInquilino ci) throws Exception;

    ContratoInquilino getContratoVigente(Departamento d) throws Exception;
}