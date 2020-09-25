package com.company.test1.jmx.importadores.validaciones;

import com.company.test1.entity.ciclos.ImputacionDocumentoImputable;
import com.company.test1.entity.enums.ValidacionEstado;
import com.company.test1.entity.extroles.Propietario;
import com.company.test1.entity.validaciones.ValidacionImputacionDocumentoImputable;
import com.company.test1.jmx.Rentamaster2DB;
import com.company.test1.jmx.importadores.ciclos.Ciclos;
import com.company.test1.jmx.importadores.personasyroles.Personas;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;

import java.sql.ResultSet;

public class Validaciones {

    public void realizaImportaciones(Persistence persistence) throws Exception{
        String sql = "select * from validaciones v inner join validaciones_imputacion_documento_imputable vidi on v.id = vidi.id";
        ResultSet r = Rentamaster2DB.getResultSet(sql);
        while(r.next()){
            ValidacionImputacionDocumentoImputable vidi = new ValidacionImputacionDocumentoImputable();
            vidi.setRm2id(r.getInt("rm2id"));
            Propietario p = new Personas().devuelvePropietarioDesdeSofiaId(r.getInt("propietario_id"), persistence);
            vidi.setPropietario(p);
            ImputacionDocumentoImputable idi = new Ciclos().devuelveImputacionDocumentoImputableDesdeSofiaId(r.getInt("imputacion_documento_imputable_id"), persistence);
            vidi.setImputacionDocumentoImputable(idi);
            vidi.setEstadoValidacion(ValidacionEstado.fromId(r.getInt("estado")));
            vidi.setFechaAprobacionRechazo(r.getDate("fechaAprobacionRechazo"));

            Transaction t = persistence.createTransaction();
            persistence.getEntityManager().persist(vidi);
            t.commit();

        }
        r.close();

    }
}
