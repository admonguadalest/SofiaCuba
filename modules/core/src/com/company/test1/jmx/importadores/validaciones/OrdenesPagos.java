package com.company.test1.jmx.importadores.validaciones;

import com.company.test1.entity.Persona;
import com.company.test1.entity.documentosImputables.FacturaProveedor;
import com.company.test1.entity.extroles.Proveedor;
import com.company.test1.entity.ordenespago.*;
import com.company.test1.jmx.Rentamaster2DB;
import com.company.test1.jmx.importadores.documentosimputables.FacturasProveedores;
import com.company.test1.jmx.importadores.personasyroles.Personas;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;

import java.sql.ResultSet;
import java.util.ArrayList;

public class OrdenesPagos {


    public void realizaImportacionesOrdenPagoProveedor(Persistence persistence) throws Exception{
        String sql = "select * from ordenes_pago_proveedor opp inner join ordenes_pago op on opp.id = op.id";
        ResultSet r = Rentamaster2DB.getResultSet(sql);
        while(r.next()){
            OrdenPagoProveedor opp = new OrdenPagoProveedor();
            opp.setRm2id(r.getInt("id"));
            Proveedor p = new Personas().devuelveProveedorDesdeSofiaId(r.getInt("proveedor_id"), persistence);
            opp.setProveedor(p);
            opp.setEsCompensacionAbono(r.getBoolean("esCompensacionAbono"));
            try{
                opp.setRealizacionPago(new RealizacionesPagos().devuelveRealizacionPagoDesdeSofiaId(r.getInt("realizacion_pago_id"), persistence));
            }catch(Exception e){

            }
            opp.setFechaValor(r.getDate("fechaValor"));
            opp.setImporte(r.getDouble("importe"));
            opp.setImporteEfectivo(r.getDouble("importeEfectivo"));
            opp.setDescripcion(r.getString("descripcion"));

            Transaction t = persistence.createTransaction();
            persistence.getEntityManager().persist(opp);
            t.commit();
        }
        r.close();
    }

    public void realizaImportacionesOrdenPagoAbono(Persistence persistence) throws Exception{
        String sql = "select * from ordenes_pago_abono opa inner join ordenes_pago op on opa.id = op.id";
        ResultSet r = Rentamaster2DB.getResultSet(sql);
        while(r.next()){
            OrdenPagoAbono opa = new OrdenPagoAbono();
            opa.setRm2id(r.getInt("id"));
            Proveedor p = new Personas().devuelveProveedorDesdeSofiaId(r.getInt("proveedor_id"), persistence);
            opa.setProveedor(p);
            FacturaProveedor fp = new FacturasProveedores().devuelveFacturaProveedorDesdeSofiaId(r.getInt("factura_proveedor_id"), persistence);
            opa.setFacturaProveedor(fp);

            opa.setFechaValor(r.getDate("fechaValor"));
            opa.setImporte(r.getDouble("importe"));
            opa.setImporteEfectivo(r.getDouble("importeEfectivo"));
            opa.setDescripcion(r.getString("descripcion"));

            Transaction t = persistence.createTransaction();
            persistence.getEntityManager().persist(opa);
            t.commit();
        }
        r.close();
    }

    public void realizaImportacionesOrdenPagoFacturaProveedor(DataManager dataManager, Persistence persistence) throws Exception{

        ArrayList entitiesToPersist = new ArrayList();

        String sql = "select * from ordenes_pago_factura_proveedor opfp inner join ordenes_pago op on opfp.id = op.id";
        ResultSet r = Rentamaster2DB.getResultSet(sql);
        while(r.next()){
            entitiesToPersist.clear();
            OrdenPagoFacturaProveedor opfp = new OrdenPagoFacturaProveedor();
            entitiesToPersist.add(opfp);
            opfp.setRm2id(r.getInt("id"));

            FacturaProveedor fp = new FacturasProveedores().devuelveFacturaProveedorDesdeSofiaId(r.getInt("factura_proveedor_id"), persistence);
            opfp.setFacturaProveedor(fp);
            opfp.setEsAbono(r.getBoolean("esAbono"));
            RealizacionPago rp = new RealizacionesPagos().devuelveRealizacionPagoDesdeSofiaId(r.getInt("realizacion_pago_id"), persistence);
            opfp.setRealizacionPago(rp);
            opfp.setFechaValor(r.getDate("fechaValor"));
            opfp.setImporte(r.getDouble("importe"));
            opfp.setImporteEfectivo(r.getDouble("importeEfectivo"));
            opfp.setDescripcion(r.getString("descripcion"));

            //compensaciones
            String sqlc = "select * from compensaciones_ordenes_pago_proveedor where orden_factura_pago_proveedor_id = " + opfp.getRm2id();
            ResultSet rc = Rentamaster2DB.getResultSet(sqlc);
            while(rc.next()){
                CompensacionOrdenPagoProveedor copp = new CompensacionOrdenPagoProveedor();
                entitiesToPersist.add(copp);
                copp.setImporte(rc.getDouble("importe"));
                copp.setOrdenPagoFacturaProveedor(opfp);
                opfp.getCompensaciones().add(copp);
                try{
                    OrdenPagoProveedor opp = devuelveOrdenPagoProveedorDesdeSofiaId(rc.getInt("orden_pago_proveedor_id"), persistence);
                    copp.setOrdenPagoProveedor(opp);
                }catch(Exception e){

                }
                try{
                    OrdenPagoAbono opa = devuelveOrdenPagoAbonoDesdeSofiaId(rc.getInt("orden_pago_abono_id"), persistence);
                    copp.setOrdenPagoAbono(opa);
                }catch(Exception e){

                }


            }
            rc.close();

            dataManager.commit(new CommitContext(entitiesToPersist));
        }
        r.close();
    }

    public OrdenPagoFacturaProveedor devuelveOrdenPagoFacturaProveedorDesdeSofiaId(Integer sofiaPersonaId, Persistence persistence) throws Exception{
        Transaction t = persistence.createTransaction();
        String hql = "SELECT opfp FROM test1_OrdenPagoFacturaProveedor opfp WHERE opfp.rm2id = " + sofiaPersonaId;
        OrdenPagoFacturaProveedor p = (OrdenPagoFacturaProveedor) persistence.getEntityManager().createQuery(hql).getFirstResult();
        t.close();
        return p;
    }

    public OrdenPagoProveedor devuelveOrdenPagoProveedorDesdeSofiaId(Integer sofiaPersonaId, Persistence persistence) throws Exception{
        Transaction t = persistence.createTransaction();
        String hql = "SELECT opp FROM test1_OrdenPagoProveedor opp WHERE opp.rm2id = " + sofiaPersonaId;
        OrdenPagoProveedor p = (OrdenPagoProveedor) persistence.getEntityManager().createQuery(hql).getFirstResult();
        t.close();
        return p;
    }

    public OrdenPagoAbono devuelveOrdenPagoAbonoDesdeSofiaId(Integer sofiaPersonaId, Persistence persistence) throws Exception{
        Transaction t = persistence.createTransaction();
        String hql = "SELECT opp FROM test1_OrdenPagoAbono opp WHERE opp.rm2id = " + sofiaPersonaId;
        OrdenPagoAbono p = (OrdenPagoAbono) persistence.getEntityManager().createQuery(hql).getFirstResult();
        t.close();
        return p;
    }


}
