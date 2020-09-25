package com.company.test1.jmx.importadores.validaciones;

import com.company.test1.entity.CuentaBancaria;
import com.company.test1.entity.ordenespago.RealizacionPago;
import com.company.test1.jmx.Rentamaster2DB;
import com.company.test1.jmx.importadores.CuentasBancarias;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;

import java.sql.ResultSet;

public class RealizacionesPagos {

    public void realizaImportaciones(Persistence persistence) throws Exception{
        String sql = "select * from realizaciones_pagos";
        ResultSet r = Rentamaster2DB.getResultSet(sql);
        while(r.next()){
            RealizacionPago rp = new RealizacionPago();
            rp.setRm2id(r.getInt("id"));
            rp.setFechaValor(r.getDate("fechaValor"));
            rp.setSepa(r.getString("sepa"));
            rp.setXsd(r.getString("xsd"));
            rp.setInfoCuentaEmisora(r.getString("infoCuentaEmisora"));
            rp.setIdentificador(r.getString("identificador"));
            rp.setImporteTotal(r.getDouble("importeTotal"));
            CuentaBancaria cb = new CuentasBancarias().devuelveCuentaBancariaDesdeSofiaId(r.getInt("cuenta_bancaria_id"), persistence);
            rp.setCuentaBancaria(cb);
            Transaction t = persistence.createTransaction();
            persistence.getEntityManager().persist(rp);
            t.commit();


        }
        r.close();
    }

    public RealizacionPago devuelveRealizacionPagoDesdeSofiaId(Integer sofiaId, Persistence persistence) throws Exception{
        Transaction t = persistence.createTransaction();
        String hql = "select rp from test1_RealizacionPago rp where rp.rm2id = " + sofiaId;
        RealizacionPago rp = (RealizacionPago)persistence.getEntityManager().createQuery(hql).getFirstResult();
        t.close();
        return rp;
    }

}
