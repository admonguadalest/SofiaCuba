package com.company.test1.service;

import com.company.test1.entity.modeloscontratosinquilinos.ModeloContrato;
import com.company.test1.entity.modeloscontratosinquilinos.Seccion;

public interface ModeloContratoInquilinoService {
    String NAME = "test1_ModeloContratoInquilinoService";

    Integer getValorOrdenacionNuevaSeccion(ModeloContrato mc);
    Integer getValorOrdenacionNuevaClausula(Seccion s);
}