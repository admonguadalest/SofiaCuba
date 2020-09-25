package com.company.test1.service;

import com.company.test1.entity.ciclos.Ciclo;
import com.company.test1.entity.departamentos.Departamento;
import com.company.test1.entity.departamentos.Ubicacion;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service(CicloService.NAME)
public class CicloServiceBean implements CicloService {

    @Inject
    Persistence persistence;

    public String calculaNuevoCodigoCiclo(Departamento d) throws Exception{
        String ci;


        Ubicacion u = d.getUbicacion();
        String cc = u.getAbreviacionUbicacion() + d.getAbreviacionPisoPuerta();
        //extraigo su codigo y en funcion de la fecha calculo uno nuevo
        SimpleDateFormat sdf = new SimpleDateFormat("yy");
        cc += "/" + sdf.format(new Date());
        return cc;
    }

    public String calculaNuevoTituloCiclo(Departamento d) throws Exception{
        String ci;


        Ubicacion u = d.getUbicacion();
        String cc = u.getAbreviacionUbicacion() + d.getAbreviacionPisoPuerta();
        //extraigo su codigo y en funcion de la fecha calculo uno nuevo
        SimpleDateFormat sdf = new SimpleDateFormat("MMyyyy");
        cc += "_" + sdf.format(new Date());
        return cc;
    }

}