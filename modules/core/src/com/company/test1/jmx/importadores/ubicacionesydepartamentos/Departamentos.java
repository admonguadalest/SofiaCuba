package com.company.test1.jmx.importadores.ubicacionesydepartamentos;

import com.company.test1.entity.ArchivoAdjunto;
import com.company.test1.entity.ColeccionArchivosAdjuntos;
import com.company.test1.entity.departamentos.CedulaHabitabilidad;
import com.company.test1.entity.departamentos.CertificadoCalificacionEnergetica;
import com.company.test1.entity.departamentos.Ubicacion;
import com.company.test1.entity.departamentos.Departamento;
import com.company.test1.entity.extroles.Propietario;
import com.company.test1.jmx.Rentamaster2DB;
import com.company.test1.jmx.importadores.ArchivosAdjuntos;
import com.company.test1.jmx.importadores.ColeccionesAdjuntos;
import com.company.test1.jmx.importadores.personasyroles.Personas;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;

import javax.inject.Inject;
import java.sql.ResultSet;
import java.util.ArrayList;

public class Departamentos {


    ArrayList entitiesToPersist = new ArrayList();

    public void realizaImportacion(DataManager dataManager, Persistence persistence) throws Exception{
        String sql = "select * from departamentos";
        ResultSet r = Rentamaster2DB.getResultSet(sql);
        while(r.next()){
            entitiesToPersist.clear();
            Integer id = r.getInt("id");
            Ubicacion ub = new Ubicaciones().getUbicacionFromSofiaId(r.getInt("ubicacion_id"), persistence);
            Departamento d = realizaImportacion(id, ub, persistence);
            dataManager.commit(new CommitContext(entitiesToPersist));


        }
        r.close();

    }

    public Departamento realizaImportacion(Integer did, Ubicacion u, Persistence persistence) throws Exception{
        String sqlV = "SELECT * FROM rentamaster2.departamentos d inner join departamentos_viviendas dv on dv.id = d.id where d.id = " + did;
        String sqlL = "SELECT * FROM rentamaster2.departamentos d inner join departamentos_locales dl on dl.id = d.id where d.id = " + did;
        int nv = Rentamaster2DB.getNumberOfRecords(sqlV, Rentamaster2DB.getConnection());
        int nl = Rentamaster2DB.getNumberOfRecords(sqlL, Rentamaster2DB.getConnection());
        Departamento d = null;
        if (nv==1){
            ResultSet r = Rentamaster2DB.getResultSet(sqlV, Rentamaster2DB.getConnection());
            while(r.next()){
                d = new Departamento();
                entitiesToPersist.add(d);
                d.setUbicacion(u);
                d.setRm2id(r.getInt("id"));
                d.setAbreviacionPisoPuerta(r.getString("abreviacionPisoPuerta"));
                d.setConAireAcondicionado(r.getBoolean("conAireAcondicionado"));
                d.setConCalefaccion(r.getBoolean("conCalefaccion"));
                d.setDadoDeBaja(r.getBoolean("dadoDeBaja"));
                d.setDescripcion(r.getString("descripcion"));
                d.setObsevaciones(r.getString("observaciones"));
                d.setPiso(r.getString("piso"));
                d.setPuerta(r.getString("puerta"));
                d.setReferenciaCatastral(r.getString("referenciaCatastral"));
                d.setSuperficie(r.getDouble("superficie"));
                d.setViviendaLocal(true);
                try{
                    int coleccionAdjuntos_id = r.getInt("coleccionAdjuntos_id");
                    ColeccionArchivosAdjuntos caa = new ColeccionesAdjuntos().realizaImportacionColeccion(coleccionAdjuntos_id, null, entitiesToPersist);
                    d.setColeccionAdjuntos(caa);
                }catch(Exception exc){
                    throw exc;
                }
//                int propietario_id = r.getInt("propietario_id");
                if (r.getInt("propietario_id")!=0){
                    Propietario prop = new Personas().devuelvePropietarioDesdeSofiaId(r.getInt("propietario_id"), persistence);
                    d.setPropietario(prop);
                }

                if (u==null){
                    Ubicacion ub = new Ubicaciones().getUbicacionFromSofiaId(r.getInt("ubicacion_id"), persistence);
                    d.setUbicacion(ub);
                }
                Integer paa_id = r.getInt("plano_archivo_adjunto_id");
                ArchivoAdjunto aa = new ArchivosAdjuntos().realizaImportacion(paa_id, null, entitiesToPersist);
                d.setPlanoDepartamento(aa);
                d.setExcluirDeMonitorizacionParaBusquedaDePisosVacios(r.getBoolean("excluirDeMonitorizacionParaBusquedaDePisosVacios"));
                d.setDadoDeBaja(r.getBoolean("departamentoDeshabilitado"));
                d.setNumBanos(r.getInt("numBanos"));
                d.setNumHabitaciones(r.getInt("numHabitaciones"));
//           d.setConSalidaDeHumos(r.getBoolean("conSalidaDeHumos"));

                //cedulas de habitabilidad y certificados energeticos
                String sqlCH = "select * from cedulas_habitabilidad ch WHERE ch.departamento_vivienda_id = " + d.getRm2id() + "";
                ResultSet rCH = Rentamaster2DB.getResultSet(sqlCH, Rentamaster2DB.getConnection());
                while(rCH.next()){
                    CedulaHabitabilidad ch = new CedulaHabitabilidad();
                    entitiesToPersist.add(ch);
                    ch.setDepartamento(d);
                    ch.setFechaEmision(rCH.getDate("fechaEmision"));
                    ch.setFechaVencimiento(rCH.getDate("fechaVencimiento"));
                    ch.setNumeroCedula(rCH.getString("numeroCedula"));
                    ch.setObservaciones(rCH.getString("observaciones"));
                    Integer aaid = rCH.getInt("archivo_adjunto_id");
                    if (aaid!=null){
                        ArchivoAdjunto aaCH = new ArchivosAdjuntos().realizaImportacion(aaid, null, entitiesToPersist);
                        ch.setEscaneoCedula(aaCH);
                    }
                    d.getCedulasHabitabilidad().add(ch);
                }

                //certificados energeticos
                String sqlCE = "select * from certificados_calificacion_energetica cce WHERE cce.departamento_id = " + d.getRm2id() + "";
                ResultSet rCE = Rentamaster2DB.getResultSet(sqlCE, Rentamaster2DB.getConnection());
                while(rCE.next()){
                    CertificadoCalificacionEnergetica cce = new CertificadoCalificacionEnergetica();
                    entitiesToPersist.add(cce);
                    cce.setDepartamento(d);
                    cce.setCalificacionConsumoEnergetico(rCE.getString("calificacionConsumoEnergetico"));
                    cce.setCalificacionEmisiones(rCE.getString("calificacionEmisiones"));
                    cce.setNumeroRegistro(rCE.getString("numeroRegistro"));
                    cce.setFechaVencimiento(rCE.getDate("fechaVencimiento"));
                    cce.setObservaciones(rCE.getString("observaciones"));
                    Integer aaid = rCE.getInt("archivo_adjunto_id");
                    if (aaid!=null){
                        ArchivoAdjunto aaCCE = new ArchivosAdjuntos().realizaImportacion(aaid, null, entitiesToPersist);
                        cce.setArchivoAdjunto(aaCCE);
                    }
                    d.getCertificadosCalificacionEnergetica().add(cce);
                }

                return d;
            }
            r.close();

        }
        if (nl==1){
            ResultSet r = Rentamaster2DB.getResultSet(sqlL, Rentamaster2DB.getConnection());
            while(r.next()){
                d = new Departamento();
                entitiesToPersist.add(d);
                d.setUbicacion(u);
                d.setRm2id(r.getInt("id"));
                d.setAbreviacionPisoPuerta(r.getString("abreviacionPisoPuerta"));
                d.setConAireAcondicionado(r.getBoolean("conAireAcondicionado"));
                d.setConCalefaccion(r.getBoolean("conCalefaccion"));
                d.setDadoDeBaja(r.getBoolean("dadoDeBaja"));
                d.setDescripcion(r.getString("descripcion"));
                d.setObsevaciones(r.getString("observaciones"));
                d.setPiso(r.getString("piso"));
                d.setPuerta(r.getString("puerta"));
                d.setReferenciaCatastral(r.getString("referenciaCatastral"));
                d.setSuperficie(r.getDouble("superficie"));
                d.setViviendaLocal(false);
                try{
                    int coleccionAdjuntos_id = r.getInt("coleccionAdjuntos_id");
                    ColeccionArchivosAdjuntos caa = new ColeccionesAdjuntos().realizaImportacionColeccion(coleccionAdjuntos_id, null, entitiesToPersist);
                    d.setColeccionAdjuntos(caa);
                }catch(Exception exc){
                    throw exc;
                }
                int propietario_id = r.getInt("propietario_id");
                //pendiente importador propietarios

                if (u==null){
                    Ubicacion ub = new Ubicaciones().getUbicacionFromSofiaId(r.getInt("ubicacion_id"), persistence);
                    d.setUbicacion(ub);
                }
                Integer paa_id = r.getInt("plano_archivo_adjunto_id");
                ArchivoAdjunto aa = new ArchivosAdjuntos().realizaImportacion(paa_id, null, entitiesToPersist);
                d.setPlanoDepartamento(aa);
                d.setExcluirDeMonitorizacionParaBusquedaDePisosVacios(r.getBoolean("excluirDeMonitorizacionParaBusquedaDePisosVacios"));
                d.setDadoDeBaja(r.getBoolean("departamentoDeshabilitado"));
//            d.setNumBanos(r.getInt("numBanos"));
//            d.setNumHabitaciones(r.getInt("numHabitaciones"));
                d.setConSalidaDeHumos(r.getBoolean("conSalidaHumos"));
            }
            r.close();

            return d;

        }
        else return null;
    }

    public Departamento getDepartamentoFromSofiaId(Integer deptoSofiaId, Persistence persistence){
        Transaction t = persistence.createTransaction();
        String sql = "SELECT d FROM test1_Departamento d WHERE d.rm2id = " + deptoSofiaId;
        Departamento d = (Departamento) persistence.getEntityManager().createQuery(sql).getFirstResult();
        t.close();
        return d;
    }

}
