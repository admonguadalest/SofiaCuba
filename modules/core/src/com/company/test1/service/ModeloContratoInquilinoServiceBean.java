package com.company.test1.service;

import com.company.test1.entity.modeloscontratosinquilinos.Clausula;
import com.company.test1.entity.modeloscontratosinquilinos.ModeloContrato;
import com.company.test1.entity.modeloscontratosinquilinos.Seccion;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service(ModeloContratoInquilinoService.NAME)
public class ModeloContratoInquilinoServiceBean implements ModeloContratoInquilinoService {

    @Inject
    private Persistence persistence;

    public Integer getValorOrdenacionNuevaSeccion(ModeloContrato mc ){

        Integer n = 0;

        List<Seccion> ss = mc.getSecciones();
        for (int i = 0; i < ss.size(); i++) {
            if (ss.get(i).getOrdenacion().intValue()>n.intValue()){
                n = ss.get(i).getOrdenacion();
            }
        }
        return n.intValue()+1;

    }

    public Integer getValorOrdenacionNuevaClausula(Seccion s ){

        Integer n = 0;
        List<Clausula> ss = s.getClausulas();
        for (int i = 0; i < ss.size(); i++) {
            if (ss.get(i).getOrdenacion().intValue()>n.intValue()){
                n = ss.get(i).getOrdenacion();
            }
        }
        return n.intValue()+1;

    }

}