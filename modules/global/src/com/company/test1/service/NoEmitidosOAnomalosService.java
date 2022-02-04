package com.company.test1.service;

import com.company.test1.entity.contratosinquilinos.ContratoInquilino;
import com.company.test1.entity.departamentos.Departamento;

import java.util.Date;
import java.util.List;

public interface NoEmitidosOAnomalosService {
    String NAME = "test1_NoEmitidosOAnomalosService";

    public List<Departamento> getDepartamentosMonitorizados() throws Exception;

    public Object[] reportarDepartamento(Departamento d, Date fechaDesde, Date fechaHasta) throws Exception;

    public double devuelveCantidadesEmitidasEntreFechas(ContratoInquilino ci, Date fechaDesde, Date fechaHasta) throws Exception;

}