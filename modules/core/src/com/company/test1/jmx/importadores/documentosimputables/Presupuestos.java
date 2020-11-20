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

public class Presupuestos {

    public Presupuesto realizaImportacion(Integer id, Persistence persistence, List entitiesToPersist) throws Exception{
        Presupuesto p = new Presupuesto();
        entitiesToPersist.add(p);
        p.setTipoEnum(DocumentoImputableTipoEnum.PRESUPUESTO);
        String sql = "select * from presupuestos p inner join documentos_proveedor dp on p.id = dp.id inner join documentos_imputables di on " +
                " di.id = dp.id where p.id = " + id;
        ResultSet r = Rentamaster2DB.getResultSet(sql, Rentamaster2DB.getConnection());
        while(r.next()){

            p.setRm2id(r.getInt("id"));
            p.setFechaValidezHasta(r.getDate("fechaValidezHasta"));
            p.setRealizadoPor(r.getString("realizadoPor"));
            p.setEsPresupuestoVerbal(r.getBoolean("esPresupuestoVerbal"));
            //override domiciliacion cuenta bancaria no se usa
            p.setProveedor(new Personas().devuelveProveedorDesdeSofiaId(r.getInt("proveedor_id"), persistence));


            p.setDescripcionDocumento(r.getString("descripcionDocumento"));
            p.setFechaEmision(r.getDate("fechaEmision"));
            p.setImporteTotalBase(r.getDouble("importeTotalBase"));
            p.setImportePostCCAA(r.getDouble("importePostCCAA"));
            p.setNumDocumento(r.getString("numDocumento"));
            p.setObservaciones(r.getString("observaciones"));
            ColeccionArchivosAdjuntos ca = new ColeccionesAdjuntos().realizaImportacionColeccion(r.getInt("coleccion_adjuntos_id"), null, entitiesToPersist);
            p.setColeccionArchivosAdjuntos(ca);
            p.setArchivado(r.getBoolean("archivado"));
            p.setOmitirControlDeOrdenPago(r.getBoolean("omitirControlDeOrdenPago"));

            //conceptos adicionales
            String sqlca = "SELECT * FROM registros_aplicaciones_conceptos_adicionales WHERE documento_imputable_id = " + p.getRm2id();
            ResultSet rca = Rentamaster2DB.getResultSet(sqlca);
            while(rca.next()){
                RegistroAplicacionConceptoAdicional raca = new RegistroAplicacionConceptoAdicional();
                entitiesToPersist.add(raca);
                p.getRegistroAplicacionConceptosAdicionales().add(raca);
                raca.setDocumentoImputable(p);
                raca.setConceptoAdicional(new ConceptosAdicionales().getConceptoAdicionalDesdeSofiaId(rca.getInt("concepto_adicional_id"), persistence));
                raca.setNumDocumento(p.getNumDocumento());
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
        return p;
    }

    public boolean esIdCorrespondienteAPresupuesto(Integer id, Persistence persistence) throws Exception{
        String sql = "select * from presupuestos p inner join documentos_proveedor dp on p.id = dp.id inner join documentos_imputables di on " +
                "di.id = dp.id where p.id = " + id;
        Integer nor = Rentamaster2DB.getNumberOfRecords(sql);
        return nor == 1;
    }

    public Presupuesto devuelvePresupuestoDesdeSofiaId(Integer sofiaPresupuestoId, Persistence persistence) throws Exception{
        Transaction t = persistence.createTransaction();
        String hql = "SELECT p FROM test1_Presupuesto p WHERE p.rm2id = " + sofiaPresupuestoId;
        Presupuesto p = (Presupuesto) persistence.getEntityManager().createQuery(hql).getFirstResult();
        t.commit();
        return p;
    }

}
