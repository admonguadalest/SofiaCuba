package com.company.test1.jmx.importadores.plantillas;

import com.company.test1.entity.enums.TipoPlantillaEnum;
import com.company.test1.entity.reportsyplantillas.Plantilla;
import com.company.test1.jmx.Rentamaster2DB;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;

import java.sql.ResultSet;

public class Plantillas {

    public void realizaImportaciones(Persistence persistence) throws Exception{
        String sql = "select * from plantillas";
        ResultSet r = Rentamaster2DB.getResultSet(sql);
        while(r.next()){
            Plantilla p = new Plantilla();
            p.setRm2id(r.getInt("id"));
            p.setContenidoPlantilla(r.getString("contenidoPlantilla"));
            p.setNombrePlantilla(r.getString("nombrePlantilla"));
            p.setTipoPlantilla(TipoPlantillaEnum.fromId(r.getString("tipoPlantilla")));
            p.setDeSistema(r.getBoolean("deSistema"));
            p.setRuta(r.getString("ruta"));
            Transaction t = persistence.createTransaction();
            persistence.getEntityManager().persist(p);
            t.commit();

        }
        r.close();
    }

    public Plantilla devuelvePlantillaDesdeSofiaId(Integer id, Persistence persistence) throws Exception{
        Transaction t = persistence.createTransaction();
        String hql = "select p from test1_Plantilla p WHERE p.rm2id = " + id;
        Plantilla p = (Plantilla) persistence.getEntityManager().createQuery(hql).getFirstResult();
        t.close();
        return p;
    }

}
