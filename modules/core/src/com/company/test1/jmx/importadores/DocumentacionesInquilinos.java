package com.company.test1.jmx.importadores;

import com.company.test1.entity.ColeccionArchivosAdjuntos;
import com.company.test1.entity.documentacionesinquilinos.DocumentacionInquilino;
import com.company.test1.jmx.Rentamaster2DB;
import com.company.test1.jmx.importadores.contratosinquilinos.ContratosInquilinos;
import com.company.test1.listeners.ArchivoAdjuntoEntityListener;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DocumentacionesInquilinos {

    public void realizaImportacion(DataManager dataManager, Persistence persistence) throws Exception{
        ArchivoAdjuntoEntityListener.activated=false;
        String sql = "select * from documentaciones_candidaturas_inquilinos";
        ResultSet r = Rentamaster2DB.getResultSet(sql);
        List entitiesToPersist = new ArrayList();
        while(r.next()){
            DocumentacionInquilino dci = new DocumentacionInquilino();
            entitiesToPersist.add(dci);
            dci.setNombreCompleto(r.getString("nombreCompleto"));
            dci.setDni(r.getString("dni"));
            dci.setFechaRegistro(r.getDate("timestamp"));
            dci.setObjetoCanidado(r.getString("objetoCandidado"));
            dci.setInformacionDeContacto(r.getString("informacionDeContacto"));
            dci.setPresentacion(r.getString("presentacion"));
            dci.setContratoInquilino(new ContratosInquilinos().devuelveContratoInquilinoDesdeSofiaId(r.getInt("contrato_inquilino_id"), persistence));
            new ColeccionesAdjuntos().realizaImportacionColeccion(r.getInt("coleccionAdjuntos_id"), null, entitiesToPersist);
            dataManager.commit(new CommitContext(entitiesToPersist));
            entitiesToPersist.clear();
        }
        ArchivoAdjuntoEntityListener.activated = true;

    }

}
