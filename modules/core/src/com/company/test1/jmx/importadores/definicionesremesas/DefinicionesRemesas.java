package com.company.test1.jmx.importadores.definicionesremesas;

import com.company.test1.entity.enums.recibos.DefinicionRemesaModoPresentacionEnum;
import com.company.test1.entity.enums.recibos.DefinicionRemesaTipoGiroEnum;
import com.company.test1.entity.enums.recibos.DefinicionRemesaUnidadPeriodicidadEnum;
import com.company.test1.entity.recibos.DefinicionRemesa;
import com.company.test1.jmx.Rentamaster2DB;
import com.company.test1.jmx.importadores.CuentasBancarias;
import com.company.test1.jmx.importadores.personasyroles.Personas;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;

import java.sql.ResultSet;

public class DefinicionesRemesas {


    public void realizaImportaciones(Persistence persistence) throws Exception{

        String sql = "select * from definiciones_remesa";
        ResultSet r = Rentamaster2DB.getResultSet(sql);
        while(r.next()){
            DefinicionRemesa dr = new DefinicionRemesa();
            dr.setRm2id(r.getInt("id"));
            dr.setCantidadPeriodicidad(r.getInt("cantidadPeriodicidad"));
            dr.setDescripcion(r.getString("descripcion"));
            dr.setNombreRemesa(r.getString("nombreRemesa"));
            dr.setTipoGiro(DefinicionRemesaTipoGiroEnum.fromId(r.getInt("tipoGiro")));
            dr.setUnidadPeriodicidad(DefinicionRemesaUnidadPeriodicidadEnum.fromId(r.getInt("unidadPeriodicidad")));
            dr.setCuentaBancaria(new CuentasBancarias().devuelveCuentaBancariaDesdeSofiaId(r.getInt("cuenta_bancaria_id"), persistence));
            dr.setPropietario(new Personas().devuelvePropietarioDesdeSofiaId(r.getInt("propietario_id"), persistence));
            dr.setModoPresentacion(DefinicionRemesaModoPresentacionEnum.fromId(r.getInt("modoPresentacion")));
            dr.setDelegado(new Personas().devuelvePersonaDesdeSofiaId(r.getInt("delegado_persona_id"), persistence));
            Transaction t = persistence.createTransaction();
            persistence.getEntityManager().persist(dr);
            t.commit();

        }
        r.close();

    }

    public DefinicionRemesa devuelveDefinicionRemesaDesdeSofiaId(Integer id, Persistence persistence) throws Exception{
        Transaction t = persistence.createTransaction();
        String hql = "select dr from test1_DefinicionRemesa dr WHERE dr.rm2id = " + id;
        DefinicionRemesa dr = (DefinicionRemesa) persistence.getEntityManager().createQuery(hql).getFirstResult();
        t.close();
        return dr;
    }

}
