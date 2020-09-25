package com.company.test1.jmx.importadores.contratosinquilinos;

import com.company.test1.entity.recibos.Concepto;
import com.company.test1.jmx.Rentamaster2DB;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.PersistenceHelper;

import java.sql.ResultSet;

public class Conceptos {

    public void realizaImportacion(Persistence persistence) throws Exception{
        String sql = "select * from conceptos";
        ResultSet r = Rentamaster2DB.getResultSet(sql);
        while(r.next()){
            Concepto c = new Concepto();
            c.setRm2id(r.getInt("id"));
            c.setAdicionSustraccion(r.getBoolean("adicionSustraccion"));
            c.setAgregarConceptoEnRecibo(r.getBoolean("agregarConceptoEnRecibo"));
            c.setAjustableAgregadamente(r.getBoolean("ajustableAgregadamente"));
            c.setDescripcion(r.getString("descripcion"));
            c.setTituloConcepto(r.getString("titulo"));
            if (devuelveConceptoDesdeTituloConcepto(c.getTituloConcepto(), persistence)!=null){
                c.setMasterConcepto(devuelveConceptoDesdeTituloConcepto(c.getTituloConcepto(), persistence));
            }
            c.setAbreviacion(r.getString("abreviacion"));
            c.setOrdenacion(r.getInt("ordenacion"));
            Transaction t = persistence.createTransaction();
            persistence.getEntityManager().persist(c);
            t.commit();
        }
        r.close();
    }

    public Concepto devuelveConceptoDesdeIdSofia(Integer id, Persistence persistence) throws Exception{
        Transaction t = persistence.createTransaction();
        String hql = "SELECT c FROM test1_Concepto c WHERE c.rm2id = " + id;
        Concepto c = (Concepto) persistence.getEntityManager().createQuery(hql).getFirstResult();
        if (c==null) return null;
        if (c.getMasterConcepto()!=null){
            c = c.getMasterConcepto();
        }
        t.close();
        return c;

    }

    //accesoria para la unificacion de conceptos
    public Concepto devuelveConceptoDesdeTituloConcepto(String id, Persistence persistence) throws Exception{
        Transaction t = persistence.createTransaction();
        String hql = "SELECT c FROM test1_Concepto c WHERE c.tituloConcepto = '" + id + "' and c.masterConcepto is null";
        Concepto c = (Concepto) persistence.getEntityManager().createQuery(hql).getFirstResult();
        t.close();
        return c;

    }

    public void eliminaConceptosNoMaestros(Persistence persistence) throws Exception{

        Transaction t = persistence.createTransaction();
        persistence.getEntityManager().setSoftDeletion(false);
        String hql = "delete FROM test1_Concepto c WHERE c.masterConcepto is not null";

        persistence.getEntityManager().createQuery(hql).executeUpdate();
        persistence.getEntityManager().setSoftDeletion(true);
        t.commit();

    }

}
