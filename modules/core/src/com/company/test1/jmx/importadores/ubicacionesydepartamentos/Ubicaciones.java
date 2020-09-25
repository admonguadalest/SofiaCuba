package com.company.test1.jmx.importadores.ubicacionesydepartamentos;

import com.company.test1.entity.ColeccionArchivosAdjuntos;
import com.company.test1.entity.Direccion;
import com.company.test1.entity.departamentos.Ubicacion;
import com.company.test1.entity.extroles.Propietario;
import com.company.test1.jmx.Rentamaster2DB;
import com.company.test1.jmx.importadores.ColeccionesAdjuntos;
import com.company.test1.jmx.importadores.personasyroles.Personas;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;

import javax.inject.Inject;
import java.sql.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Ubicaciones {


    ArrayList entitiesToPersist = new ArrayList();

    public void realizaImportacion(DataManager dataManager, Persistence persistence) throws Exception{
        Connection conn = Rentamaster2DB.getConnection();
        String sql = "select * from ubicaciones";
        ResultSet r = Rentamaster2DB.getResultSet(sql, Rentamaster2DB.getConnection());
        while(r.next()){

            Ubicacion u = new Ubicacion();
            entitiesToPersist.add(u);
            u.setRm2id(r.getInt("id"));
            u.setAbreviacionUbicacion(r.getString("abreviacionUbicacion"));
            u.setEsPropiedadVertical(r.getBoolean("esPropiedadVertical"));
            u.setInformacionCatastral(r.getString("informacionCatastral"));
            u.setLatitud(r.getDouble("latitud"));
            u.setLongitud(r.getDouble("longitud"));
            u.setNombre(r.getString("nombre"));
            u.setNombreDistrito("nombreDistrito");
            u.setNumAscensores(r.getInt("numAscensores"));
            Integer coleccion_adjuntos_id = r.getInt("coleccion_adjuntos_id");
            if (coleccion_adjuntos_id!=null){
                ColeccionArchivosAdjuntos caa = new ColeccionesAdjuntos().realizaImportacionColeccion(coleccion_adjuntos_id, null, entitiesToPersist);
                if (caa.getNombre()==null){
                    caa.setNombre("Ubicacion");
                }
                u.setColeccionArchivosAdjuntos(caa);
            }
            if (r.getInt("propietario_id")!=0){
                Propietario prop = new Personas().devuelvePropietarioDesdeSofiaId(r.getInt("propietario_id"), persistence);
                u.setPropietario(prop);
            }

            //direccion de ubicacion
            Integer direccionId = r.getInt("direccion_id");
            //direcciones
            String sqld = "select * from direcciones where id = " + direccionId;
            ResultSet rd = Rentamaster2DB.getResultSet(sqld);
            while(rd.next()){
                Direccion d = new Direccion();
                entitiesToPersist.add(d);
                u.setDireccion(d);

                d.setCodigoPostal(rd.getString("codigoPostal"));
                d.setDireccionCompleta(rd.getString("direccionCompleta"));
                d.setEscalera(rd.getString("escalera"));
                d.setNombre(rd.getString("nombre"));
                d.setNombreVia(rd.getString("nombreVia"));
                d.setNumeroVia(rd.getString("numeroVia"));
                d.setObservaciones(rd.getString("observaciones"));
                d.setPais(rd.getString("pais"));
                d.setPiso(rd.getString("piso"));
                d.setPoblacion(rd.getString("poblacion"));
                d.setProvincia(rd.getString("provincia"));
                d.setPuerta(rd.getString("puerta"));
                d.setRegion(rd.getString("region"));
                d.setVia(rd.getString("via"));

                d.setEnviarCorrespondenciaAEstaDireccion(rd.getBoolean("enviarCorrespondenciaAEstaDireccion"));


            }






        }
        r.close();

        dataManager.commit(new CommitContext(entitiesToPersist));
    }

    public Ubicacion getUbicacionFromSofiaId(Integer sofiaId, Persistence persistence){
        Transaction t = persistence.createTransaction();
        String hql = "select u from test1_Ubicacion u where u.rm2id = " + sofiaId;
        Ubicacion u = (Ubicacion) persistence.getEntityManager().createQuery(hql).getFirstResult();
        t.close();
        return u;
    }


}
