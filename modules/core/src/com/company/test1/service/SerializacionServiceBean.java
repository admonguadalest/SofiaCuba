package com.company.test1.service;

import com.company.test1.entity.extroles.Propietario;
import com.company.test1.service.accessory.serialization.Util;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service(SerializacionService.NAME)
public class SerializacionServiceBean implements SerializacionService {

    public String reportMod347(Propietario prop, Date fechaDesde, Date fechaHasta) throws Exception{
        String s = Util.confeccionaImportable(prop, fechaDesde, fechaHasta);
        return s;
    }

}