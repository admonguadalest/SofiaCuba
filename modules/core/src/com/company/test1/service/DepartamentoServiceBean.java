package com.company.test1.service;

import com.company.test1.entity.departamentos.Departamento;
import com.company.test1.entity.extroles.Propietario;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.Transaction;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service(DepartamentoService.NAME)
public class DepartamentoServiceBean implements DepartamentoService {

    @Inject
    Persistence persistence;

    public List<Departamento> devuelveDepartamentosAsociadosAPropietarios(List<Propietario> props){
        String hql = "select d from test1_Departamento d LEFT JOIN d.propietario prop1 JOIN d.ubicacion u JOIN u.propietario prop2 WHERE ";
        for (int i = 0; i < props.size(); i++) {
            if (i > 0){
                hql += " AND ";
            }
            hql += "(prop1 = :p" + i + " OR prop2 = :p" + i + ") ";
        }
        Transaction t = persistence.createTransaction();
        Query q = persistence.getEntityManager().createQuery(hql);
        for (int i = 0; i < props.size(); i++) {
            q.setParameter("p"+i, props.get(i));
        }
        List<Departamento> dd = q.getResultList();
        return dd;
    }

}