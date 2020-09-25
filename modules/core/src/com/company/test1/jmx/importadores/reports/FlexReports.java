package com.company.test1.jmx.importadores.reports;

import com.company.test1.entity.enums.FlexReportModoProductor;
import com.company.test1.entity.enums.FlexReportTipo;
import com.company.test1.entity.reportsyplantillas.FlexReport;
import com.company.test1.jmx.Rentamaster2DB;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;

import java.sql.ResultSet;

public class FlexReports {

    public void realizaImportaciones(Persistence persistence) throws Exception{
        String sql = "SELECT * FROM flex_reports";
        ResultSet r = Rentamaster2DB.getResultSet(sql);
        while(r.next()){
            FlexReport fr = new FlexReport();
            fr.setContenidoJrxml(r.getString("contenidoJrxml"));
            fr.setFields(r.getString("fields"));
            fr.setForzarReportDeUnSoloRegistroVacio(r.getBoolean("forzarReportDeUnSoloRegistroVacio"));
            fr.setModoProductor(FlexReportModoProductor.fromId(r.getString("modoProductor")));
            fr.setNombre(r.getString("nombre"));
            fr.setParametrosManuales(r.getString("parametrosManuales"));
            fr.setProductor(r.getString("productor"));
            fr.setTipo(FlexReportTipo.fromId(r.getString("tipo")));
            fr.setParametrosProductor(r.getString("parametrosProductor"));
            fr.setRuta(r.getString("ruta"));
            fr.setUserDataSourceConnection(r.getBoolean("usarDataSourceConnection"));
            Transaction t = persistence.createTransaction();
            persistence.getEntityManager().persist(fr);
            t.commit();
        }
    }
}
