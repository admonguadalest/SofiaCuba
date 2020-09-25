package com.company.test1.jmx.importadores.notificaciones;

import com.company.test1.entity.contratosinquilinos.ContratoInquilino;
import com.company.test1.entity.notificaciones.NotificacionContratoInquilino;
import com.company.test1.entity.reportsyplantillas.Plantilla;
import com.company.test1.jmx.Rentamaster2DB;
import com.company.test1.jmx.importadores.contratosinquilinos.ContratosInquilinos;
import com.company.test1.jmx.importadores.plantillas.Plantillas;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;

import java.sql.ResultSet;

public class Notificaciones {

    public void realizaImportacionesNotificacionesContratosInquilinos(Persistence persistence) throws Exception{

        String sql = "select * from notificaciones_contratos_inquilino nci inner join notificaciones n on nci.id = n.id";
        ResultSet r = Rentamaster2DB.getResultSet(sql);
        while(r.next()){
            NotificacionContratoInquilino nci = new NotificacionContratoInquilino();
            ContratoInquilino ci = new ContratosInquilinos().devuelveContratoInquilinoDesdeSofiaId(r.getInt("contrato_inquilino_id"), persistence);
            nci.setContratoInquilino(ci);
            nci.setContenidoImplementado(r.getString("contenidoImplementado"));
            nci.setEnviado(r.getBoolean("enviado"));
            nci.setFechaRealizacion(r.getDate("fechaRealizacion"));
            nci.setImplementado(r.getBoolean("implementado"));
            Plantilla p = new Plantillas().devuelvePlantillaDesdeSofiaId(r.getInt("plantilla_id"), persistence);
            nci.setPlantilla(p);
            nci.setVersionPdf(r.getBytes("versionPdf"));
            nci.setFechaProgramadaEnvio(r.getDate("fechaProgramadaEnvio"));
            nci.setTitulo(r.getString("titulo"));
            Transaction t = persistence.createTransaction();
            persistence.getEntityManager().persist(nci);
            t.commit();
        }
        r.close();


    }

}
