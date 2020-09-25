package com.company.test1.jmx.importadores.definicionesremesas;


import com.company.test1.entity.recibos.Concepto;
import com.company.test1.entity.recibos.DefinicionRemesa;
import com.company.test1.entity.recibos.Serie;
import com.company.test1.jmx.Rentamaster2DB;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.View;

import java.sql.ResultSet;

public class Series {

    public void realizaImportacion(Persistence persistence) throws Exception{

            String sql = "select * from series";
            ResultSet r = Rentamaster2DB.getResultSet(sql);
            while(r.next()){
                Serie s = new Serie();
                s.setRm2id(r.getInt("id"));
                s.setNombreSerie(r.getString("nombreSerie"));
                s.setDescripcion(r.getString("descripcion"));
                if (devuelveSerieDesdeTituloSerie(s.getNombreSerie(), persistence)!=null){
                    s.setMasterSerie(devuelveSerieDesdeTituloSerie(s.getNombreSerie(), persistence));
                }
                Transaction t = persistence.createTransaction();
                persistence.getEntityManager().persist(s);
                t.commit();
            }
            r.close();

    }

    public Serie devuelveSerieDesdeSofiaId(Integer id, Persistence persistence) throws Exception{
        Transaction t = persistence.createTransaction();
        String hql = "select s from test1_Serie s WHERE s.rm2id = " + id;

        Serie dr = (Serie) persistence.getEntityManager().createQuery(hql).setView(Serie.class, "serie-view").getFirstResult();
        t.close();
        if (dr.getMasterSerie()!=null){
            return dr.getMasterSerie();
        }
        return dr;
    }

    //accesoria para la unificacion de conceptos
    public Serie devuelveSerieDesdeTituloSerie(String id, Persistence persistence) throws Exception{
        Transaction t = persistence.createTransaction();
        String hql = "SELECT s FROM test1_Serie s WHERE s.nombreSerie = '" + id + "' and s.masterSerie is null";
        Serie s = (Serie) persistence.getEntityManager().createQuery(hql).getFirstResult();
        t.close();
        return s;

    }

    public void eliminaSeriesNoMaestros(Persistence persistence) throws Exception{

        Transaction t = persistence.createTransaction();
        persistence.getEntityManager().setSoftDeletion(false);
        String hql = "delete FROM test1_Serie s WHERE s.masterSerie is not null";

        persistence.getEntityManager().createQuery(hql).executeUpdate();
        persistence.getEntityManager().setSoftDeletion(true);
        t.commit();

    }

}
