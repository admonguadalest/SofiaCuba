package com.company.test1.service;

import com.company.test1.entity.recibos.RegistroIndiceReferencia;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service(RegistroIndiceReferenciaService.NAME)
public class RegistroIndiceReferenciaServiceBean implements RegistroIndiceReferenciaService {

    @Inject
    Persistence persistence;

    public Integer[] devuelveSiguienteParAnnoMesParaRegistro(){
        Transaction t = persistence.createTransaction();
        String hql = "select rir FROM test1_RegistroIndiceReferencia rir order by rir.createTs desc";
        RegistroIndiceReferencia rir = (RegistroIndiceReferencia) persistence.getEntityManager().createQuery(hql).getFirstResult();
        Integer anno = null;
        Integer mes = null;
        if (rir.getMes()==11){
            anno = rir.getAnno() + 1;
            mes = 0;
        }else{
            anno = rir.getAnno();
            mes = rir.getMes() + 1;
        }
        t.close();
        return new Integer[]{anno, mes};
    }

}