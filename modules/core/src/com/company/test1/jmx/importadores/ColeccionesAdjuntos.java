package com.company.test1.jmx.importadores;

import com.company.test1.entity.ArchivoAdjunto;
import com.company.test1.entity.ColeccionArchivosAdjuntos;
import com.company.test1.jmx.Rentamaster2DB;

import java.sql.ResultSet;
import java.util.List;

public class ColeccionesAdjuntos {

    public ColeccionArchivosAdjuntos realizaImportacionColeccion(Integer id, ColeccionArchivosAdjuntos caaParent, List entitiesToPersist) throws Exception{
        ColeccionArchivosAdjuntos caa = new ColeccionArchivosAdjuntos();
        entitiesToPersist.add(caa);
        String sql = "SELECT * from colecciones_adjuntos where id = " + id;
        ResultSet r = Rentamaster2DB.getResultSet(sql, Rentamaster2DB.getConnection());
        while(r.next()){
            caa.setNombre(r.getString("nombreColeccion"));
            if (caaParent!=null){
                caa.setColeccionParent(caaParent);
                caaParent.getColecciones().add(caa);
            }
            //buscando colecciones hijas
            String ssql = "SELECT * from colecciones_adjuntos WHERE coleccion_parent_id = " + id;
            ResultSet r2 = Rentamaster2DB.getResultSet(ssql, Rentamaster2DB.getConnection());
            while(r2.next()){
                Integer sid = r2.getInt("id");
                ColeccionArchivosAdjuntos scaa = realizaImportacionColeccion(sid, caa, entitiesToPersist);
            }
            r2.close();
            //buscando archivos adjuntos
            ssql = "SELECT * FROM archivos_adjuntos WHERE coleccion_adjuntos_id = " + id;
            r2 = Rentamaster2DB.getResultSet(ssql, Rentamaster2DB.getConnection());
            while(r2.next()){
                Integer aaid = r2.getInt("id");
                ArchivoAdjunto aa = new ArchivosAdjuntos().realizaImportacion(aaid, caa, entitiesToPersist);

            }
            r2.close();
        }
        r.close();
        return caa;
    }

}
