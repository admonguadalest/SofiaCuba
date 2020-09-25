package com.company.test1.jmx.importadores.recibos;

import com.company.test1.entity.ordenescobro.OrdenCobro;
import com.company.test1.entity.ordenescobro.RealizacionCobro;
import com.company.test1.entity.recibos.Recibo;
import com.company.test1.jmx.Rentamaster2DB;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;

import java.sql.ResultSet;

public class Cobros {

    public void realizaImportacionesOrdenesCobroRecibo(Persistence persistence) throws Exception{

        String sql = "select * from ordenes_cobros oc inner join ordenes_cobros_recibos ocr on oc.id = ocr.id";
        ResultSet r = Rentamaster2DB.getResultSet(sql);
        while(r.next()){
            OrdenCobro oc = new OrdenCobro();
            oc.setFechaValor(r.getDate("fechaValor"));
            oc.setImporte(r.getDouble("importe"));
            oc.setDescripcion(r.getString("descripcion"));
            oc.setEntToEntId(r.getString("entToEntId"));
            RealizacionCobro rc = devuelveRealizacionCobroDesdeSofiaId(r.getInt("realizacion_cobro_id"), persistence);
            oc.setRealizacionCobro(rc);
            Recibo rbo = new Recibos().devuelveReciboDesdeSofiaId(r.getInt("recibo_id"), persistence);
            oc.setRecibo(rbo);
            Transaction t = persistence.createTransaction();
            persistence.getEntityManager().persist(oc);
            t.commit();
        }
        r.close();

    }

    public void realizaImportacionesRealizacionesCobro(Persistence persistence) throws Exception{
        String sql = "select * from realizaciones_cobros";
        ResultSet r = Rentamaster2DB.getResultSet(sql);
        while(r.next()){
            RealizacionCobro rc = new RealizacionCobro();
            rc.setRm2id(r.getInt("id"));
            rc.setFechaValor(r.getDate("fechaValor"));
            rc.setXsd(r.getString("xsd"));
            rc.setSepa(r.getString("sepa"));
            rc.setIdentificador(r.getString("identificador"));
            rc.setImporteTotal(r.getDouble("importeTotal"));
            Transaction t = persistence.createTransaction();
            persistence.getEntityManager().persist(rc);
            t.commit();
        }
        r.close();
    }

    public RealizacionCobro devuelveRealizacionCobroDesdeSofiaId(Integer id, Persistence persistence) throws Exception{
        Transaction t = persistence.createTransaction();
        String hql = "select rc FROM test1_RealizacionCobro rc WHERE rc.rm2id = "+ id;
        RealizacionCobro rc = (RealizacionCobro) persistence.getEntityManager().createQuery(hql).getFirstResult();
        t.close();
        return rc;

    }




}
