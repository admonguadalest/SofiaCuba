package com.company.test1.service;

import com.company.test1.entity.contratosinquilinos.ParametroValorAnexo;
import com.company.test1.entity.reportsyplantillas.Plantilla;

import java.util.List;

public interface PlantillaService {
    String NAME = "test1_PlantillaService";

    List<ParametroValorAnexo> devuelveParametrosDePlantilla(Plantilla p);

}