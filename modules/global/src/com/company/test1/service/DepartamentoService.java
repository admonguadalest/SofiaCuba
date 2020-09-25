package com.company.test1.service;

import com.company.test1.entity.departamentos.Departamento;
import com.company.test1.entity.extroles.Propietario;

import java.util.List;

public interface DepartamentoService {
    String NAME = "test1_DepartamentoService";

    List<Departamento> devuelveDepartamentosAsociadosAPropietarios(List<Propietario> props);
}