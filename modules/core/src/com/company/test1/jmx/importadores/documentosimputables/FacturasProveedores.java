package com.company.test1.jmx.importadores.documentosimputables;

import com.company.test1.entity.ColeccionArchivosAdjuntos;
import com.company.test1.entity.conceptosadicionales.RegistroAplicacionConceptoAdicional;
import com.company.test1.entity.documentosImputables.FacturaProveedor;
import com.company.test1.entity.documentosImputables.Presupuesto;
import com.company.test1.entity.enums.DocumentoImputableTipoEnum;
import com.company.test1.jmx.Rentamaster2DB;
import com.company.test1.jmx.importadores.ColeccionesAdjuntos;
import com.company.test1.jmx.importadores.conceptosadicionales.ConceptosAdicionales;
import com.company.test1.jmx.importadores.personasyroles.Personas;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.List;

public class FacturasProveedores {

    public FacturaProveedor realizaImportacion(Integer id, Persistence persistence, List entitiesToPersist) throws Exception{
        FacturaProveedor fp = new FacturaProveedor();
        entitiesToPersist.add(fp);
        fp.setTipoEnum(DocumentoImputableTipoEnum.FACTURA);
        String sql = "select * from facturas_proveedores fp inner join documentos_proveedor dp on fp.id = dp.id inner join documentos_imputables di on " +
                "di.id = dp.id where fp.id = " + id;
        ResultSet r = Rentamaster2DB.getResultSet(sql, Rentamaster2DB.getConnection());
        while(r.next()){

            fp.setRm2id(r.getInt("id"));
            fp.setFechaDevengo(r.getDate("fechaDevengo"));
            fp.setFechaPago(r.getDate("fechaPago"));
            fp.setPorcentajeFacturaPagado(r.getDouble("porcentajeFacturaPagado"));
            fp.setConsideracionesDocumentoImputable(r.getString("consideracionesDocumentoImputable"));
            fp.setPagoPorCaja(r.getBoolean("pagoPorCaja"));
            //override domiciliacion cuenta bancaria no se usa
            fp.setProveedor(new Personas().devuelveProveedorDesdeSofiaId(r.getInt("proveedor_id"), persistence));


            fp.setDescripcionDocumento(r.getString("descripcionDocumento"));
            fp.setFechaEmision(r.getDate("fechaEmision"));
            fp.setImporteTotalBase(r.getDouble("importeTotalBase"));
            fp.setImportePostCCAA(r.getDouble("importePostCCAA"));
            fp.setNumDocumento(r.getString("numDocumento"));
            fp.setObservaciones(r.getString("observaciones"));
            ColeccionArchivosAdjuntos ca = new ColeccionesAdjuntos().realizaImportacionColeccion(r.getInt("coleccion_adjuntos_id"), null, entitiesToPersist);
            fp.setColeccionArchivosAdjuntos(ca);
            fp.setArchivado(r.getBoolean("archivado"));
            fp.setOmitirControlDeOrdenPago(r.getBoolean("omitirControlDeOrdenPago"));

            try {
                Presupuesto p = new Presupuestos().realizaImportacion(r.getInt("presupuesto_id"), persistence, entitiesToPersist);
                fp.setPresupuesto(p);
            }catch(Exception exc){

            }
            //conceptos adicionales
            String sqlca = "SELECT * FROM registros_aplicaciones_conceptos_adicionales WHERE documento_imputable_id = " + fp.getRm2id();
            ResultSet rca = Rentamaster2DB.getResultSet(sqlca);
            while(rca.next()){
                RegistroAplicacionConceptoAdicional raca = new RegistroAplicacionConceptoAdicional();
                entitiesToPersist.add(raca);
                fp.getRegistroAplicacionConceptosAdicionales().add(raca);
                raca.setDocumentoImputable(fp);
                raca.setConceptoAdicional(new ConceptosAdicionales().getConceptoAdicionalDesdeSofiaId(rca.getInt("concepto_adicional_id"), persistence));
                raca.setNumDocumento(fp.getNumDocumento());
                raca.setNifDni(rca.getString("nifDni"));
                raca.setFechaValor(rca.getDate("fechaValor"));
                raca.setBase(rca.getDouble("base"));
                raca.setPorcentaje(rca.getDouble("porcentaje"));
                raca.setImporteAplicado(rca.getDouble("importeAplicado"));
                //pendiente itemfacturable
            }
            rca.close();




        }
        r.close();
        return fp;
    }

    public boolean esIdCorrespondienteAFacturaProveedor(Integer id, Persistence persistence) throws Exception{
        String sql = "select * from facturas_proveedores fp inner join documentos_proveedor dp on fp.id = dp.id inner join documentos_imputables di on " +
                "di.id = dp.id where fp.id = " + id;
        Integer nor = Rentamaster2DB.getNumberOfRecords(sql);
        return nor == 1;
    }

    public FacturaProveedor devuelveFacturaProveedorDesdeSofiaId(Integer sofiaFacturaProveedorId, Persistence persistence) throws Exception{
        Transaction t = persistence.createTransaction();
        String hql = "SELECT fp FROM test1_FacturaProveedor fp WHERE fp.rm2id = " + sofiaFacturaProveedorId;
        FacturaProveedor p = (FacturaProveedor) persistence.getEntityManager().createQuery(hql).getFirstResult();
        t.commit();
        return p;
    }

    public Presupuesto devuelvePresupuestoDesdeSofiaId(Integer sofiaPresupuestoId, Persistence persistence) throws Exception{
        Transaction t = persistence.createTransaction();
        String hql = "SELECT p FROM test1_Presupuesto p WHERE p.rm2id = " + sofiaPresupuestoId;
        Presupuesto p = (Presupuesto) persistence.getEntityManager().createQuery(hql).getFirstResult();
        t.commit();
        return p;
    }



}
