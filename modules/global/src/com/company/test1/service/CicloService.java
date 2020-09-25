package com.company.test1.service;

import com.company.test1.entity.departamentos.Departamento;

import javax.inject.Inject;
import javax.persistence.Persistence;


public interface CicloService{
    String NAME = "test1_CicloService";



    String calculaNuevoCodigoCiclo(Departamento d) throws Exception;
    String calculaNuevoTituloCiclo(Departamento d) throws Exception;
}