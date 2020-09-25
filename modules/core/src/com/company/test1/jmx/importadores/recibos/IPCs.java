package com.company.test1.jmx.importadores.recibos;

import com.company.test1.entity.Persona;
import com.company.test1.entity.recibos.RegistroIndiceReferencia;
import com.company.test1.jmx.Rentamaster2DB;
import com.company.test1.jmx.importadores.personasyroles.Personas;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;

import java.sql.ResultSet;

public class IPCs {

    public void realizaImportaciones(Persistence persistence) throws Exception{
        String sql = "select * from indices_incrementos";
        ResultSet r = Rentamaster2DB.getResultSet(sql);
        while(r.next()){
            RegistroIndiceReferencia rir = new RegistroIndiceReferencia();
            rir.setAnno(r.getInt("anno"));
            rir.setMes(r.getInt("mes"));
            rir.setNombreTipo(r.getString("nombreTipo"));
            Persona p = new Personas().devuelvePersonaDesdeSofiaId(1294, persistence);
            rir.setRealizadoPor(p);
            rir.setValor(r.getDouble("valor"));
            Transaction t = persistence.createTransaction();
            persistence.getEntityManager().persist(rir);
            t.commit();

        }


    }
}
