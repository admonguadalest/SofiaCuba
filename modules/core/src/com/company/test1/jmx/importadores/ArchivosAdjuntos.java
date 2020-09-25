package com.company.test1.jmx.importadores;

import com.company.test1.entity.ArchivoAdjunto;
import com.company.test1.entity.ColeccionArchivosAdjuntos;
import com.company.test1.jmx.Rentamaster2DB;

import java.sql.ResultSet;
import java.util.List;

public class ArchivosAdjuntos {

    public ArchivoAdjunto realizaImportacion(Integer id, ColeccionArchivosAdjuntos caa, List entitiesToPersist) throws Exception{
        ArchivoAdjunto aa = null;
        String sql = "SELECT * FROM archivos_adjuntos where id = " + id;
        ResultSet r = Rentamaster2DB.getResultSet(sql, Rentamaster2DB.getConnection());
        while(r.next()){
            aa = new ArchivoAdjunto();
            entitiesToPersist.add(aa);
            aa.setTamano(r.getInt("tamano"));
            aa.setRepresentacionSerial(r.getBytes("representacionSerial"));
            aa.setNombreArchivoOriginal(r.getString("nombreArchivoOriginal"));
            aa.setNombreArchivo(r.getString("nombreArchivo"));
            aa.setMimeType(r.getString("mimeType"));
            aa.setExtension(r.getString("extension"));
            aa.setDescripcion(r.getString("descripcion"));
            aa.setExtId(r.getInt("extId"));
            if (caa!=null){
                aa.setColeccionArchivosAdjuntos(caa);
                caa.getArchivos().add(aa);
            }

        }
        r.close();
        return aa;
    }

}
