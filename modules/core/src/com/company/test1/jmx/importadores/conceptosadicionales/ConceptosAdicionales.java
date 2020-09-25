package com.company.test1.jmx.importadores.conceptosadicionales;

import com.company.test1.entity.conceptosadicionales.ConceptoAdicional;
import com.company.test1.jmx.Rentamaster2DB;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;

import java.sql.ResultSet;

public class ConceptosAdicionales {

    public void realizaImportacion(Persistence persistence) throws Exception{

        String sql = "SELECT * FROM conceptos_adicionales";
        ResultSet r = Rentamaster2DB.getResultSet(sql);
        while(r.next()){
            ConceptoAdicional ca = new ConceptoAdicional();
            ca.setRm2id(r.getInt("id"));
            ca.setAbreviacion(r.getString("abreviacion"));
            ca.setAdicionSustraccion(r.getBoolean("adicionSustraccion"));
            ca.setNombre(r.getString("nombre"));
            Transaction t = persistence.createTransaction();
            persistence.getEntityManager().persist(ca);
            t.commit();


        }
        r.close();


    }

    public ConceptoAdicional getConceptoAdicionalDesdeSofiaId(Integer id, Persistence persistence) throws Exception{
        Transaction t = persistence.createTransaction();
        String hql = "SELECT ca FROM test1_ConceptoAdicional ca WHERE ca.rm2id = " + id;
        ConceptoAdicional ca = (ConceptoAdicional) persistence.getEntityManager().createQuery(hql).getFirstResult();
        t.close();
        return ca;
    }

}
