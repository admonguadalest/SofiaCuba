package com.company.test1.service;


import com.company.test1.entity.extroles.Propietario;

import java.util.Date;

public interface SerializacionService {
    String NAME = "test1_SerializacionService";

    String importableMod347(Propietario prop, Date fechaDesde, Date fechaHasta) throws Exception;

}