package com.company.test1.jmx.importadores.coeficientes;

import com.company.test1.entity.coeficientes.Coeficiente;
import com.company.test1.entity.coeficientes.UbicacionCoeficiente;
import com.company.test1.entity.conceptosadicionales.ConceptoAdicional;
import com.company.test1.entity.departamentos.Departamento;
import com.company.test1.entity.departamentos.Ubicacion;
import com.company.test1.jmx.Rentamaster2DB;
import com.company.test1.jmx.importadores.contratosinquilinos.ContratosInquilinos;
import com.company.test1.jmx.importadores.ubicacionesydepartamentos.Departamentos;
import com.company.test1.jmx.importadores.ubicacionesydepartamentos.Ubicaciones;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.company.test1.entity.coeficientes.TipoCoeficiente;

import java.sql.ResultSet;

public class Coeficientes {

    public void realizaImportacion(Persistence persistence) throws Exception{

        String sql = "SELECT * FROM tipos_coeficiente";
        ResultSet r = Rentamaster2DB.getResultSet(sql);
        while(r.next()){
            TipoCoeficiente tc = new TipoCoeficiente();
            tc.setDescripcion(r.getString("descripcion"));
            tc.setNombre(r.getString("nombre"));
            tc.setValorMax(r.getDouble("valorMax"));
            tc.setValorMin(r.getDouble("valorMin"));
            tc.setRm2id(r.getInt("id"));
            Transaction t = persistence.createTransaction();
            persistence.getEntityManager().persist(tc);
            t.commit();


        }
        r.close();

        sql = "select * from ubicaciones_coeficientes";
        r = Rentamaster2DB.getResultSet(sql);
        while(r.next()){
            UbicacionCoeficiente uc = new UbicacionCoeficiente();
            Ubicacion u = new Ubicaciones().getUbicacionFromSofiaId(r.getInt("ubicacion_id"), persistence);
            uc.setUbicacion(u);
            TipoCoeficiente tc = new ContratosInquilinos().devuelveTipoCoeficienteDesdeSofiaId(r.getInt("tipo_coeficiente_id"), persistence);
            uc.setTipoCoeficiente(tc);
            uc.setTotalTipoCoeficiente(r.getDouble("totalTipoCoeficiente"));
            Transaction t = persistence.createTransaction();
            persistence.getEntityManager().persist(uc);
            t.commit();

        }
        r.close();

        sql = "select * from coeficientes";
        r = Rentamaster2DB.getResultSet(sql);
        while(r.next()){
            Coeficiente c = new Coeficiente();
            c.setValor(r.getDouble("valor"));
            TipoCoeficiente tc = new ContratosInquilinos().devuelveTipoCoeficienteDesdeSofiaId(r.getInt("tipo_coeficiente_id"), persistence);
            c.setTipoCoeficiente(tc);
            Departamento d = new Departamentos().getDepartamentoFromSofiaId(r.getInt("departamento_id"), persistence);
            c.setDepartamento(d);
            Transaction t = persistence.createTransaction();
            persistence.getEntityManager().persist(c);
            t.commit();

        }
        r.close();


    }



}
