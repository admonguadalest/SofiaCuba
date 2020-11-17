package com.company.test1.jmx.importadores.recibos;

import com.company.test1.core.HelperRecibosInformeIva;
import com.company.test1.entity.Persona;
import com.company.test1.entity.conceptosadicionales.RegistroAplicacionConceptoAdicional;
import com.company.test1.entity.enums.recibos.ReciboCobradoModoIngreso;
import com.company.test1.entity.enums.recibos.ReciboGradoImpago;
import com.company.test1.entity.recibos.*;
import com.company.test1.jmx.Cubatest1DB;
import com.company.test1.jmx.Rentamaster2DB;
import com.company.test1.jmx.importadores.conceptosadicionales.ConceptosAdicionales;
import com.company.test1.jmx.importadores.contratosinquilinos.Conceptos;
import com.company.test1.jmx.importadores.contratosinquilinos.ContratosInquilinos;
import com.company.test1.jmx.importadores.definicionesremesas.DefinicionesRemesas;
import com.company.test1.jmx.importadores.definicionesremesas.Series;
import com.company.test1.jmx.importadores.personasyroles.Personas;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
import org.apache.bcel.generic.ARRAYLENGTH;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Recibos {

    ArrayList entitiesToPersist = new ArrayList();

    public void createTableZHelperIva() throws Exception{
        String sql = "CREATE TABLE `z_helper_proceso_recibos_informeiva` (\n" +
                "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
                "  `entorno_id` varchar(32) DEFAULT NULL,\n" +
                "  `ubicacion_rm2id` varchar(32) DEFAULT NULL,\n" +
                "  `ubicacion_id` varchar(32) NOT NULL DEFAULT '0',\n" +
                "  `departamento_rm2id` varchar(32) DEFAULT NULL,\n" +
                "  `departamento_id` varchar(32) NOT NULL DEFAULT '0',\n" +
                "  `recibo_id` varchar(32) NOT NULL DEFAULT '0',\n" +
                "  `inquilino_id` varchar(32) NOT NULL DEFAULT '0',\n" +
                "  `fechaEmision` date DEFAULT NULL,\n" +
                "  `numRecibo` varchar(25) DEFAULT NULL,\n" +
                "  `nifDniInquilino` varchar(15) DEFAULT NULL,\n" +
                "  `nombreCompleto` varchar(255) DEFAULT NULL,\n" +
                "  `nombre_ubicacion` varchar(255) DEFAULT NULL,\n" +
                "  `piso` varchar(255) DEFAULT NULL,\n" +
                "  `puerta` varchar(255) DEFAULT NULL,\n" +
                "  `totalRecibo` double DEFAULT NULL,\n" +
                "  `totalReciboPostCCAA` double DEFAULT NULL,\n" +
                "  `importe` double DEFAULT NULL,\n" +
                "  `importePostCCAA` double DEFAULT NULL,\n" +
                "  `base` double DEFAULT NULL,\n" +
                "  `porcentajeIVA` double DEFAULT NULL,\n" +
                "  `importeAplicadoIVA` double DEFAULT NULL,\n" +
                "  `porcentajeIRPF` double DEFAULT NULL,\n" +
                "  `importeAplicadoIRPF` double DEFAULT NULL,\n" +
                "  PRIMARY KEY (`id`)\n" +
                ") ENGINE=InnoDB AUTO_INCREMENT=26052 DEFAULT CHARSET=latin1;";
        Connection conn = Cubatest1DB.getConnection();

            Cubatest1DB.executeSql(conn, sql);



    }

    String annoDesdeImportacion = "2000";

    public List<Recibo> realizaImportacionRecibos(Persistence persistence, OrdenanteRemesa ordenanteRemesa, List entitiesToPersist) throws Exception{
        ArrayList<Recibo> recibos = new ArrayList<Recibo>();
        try {

            String sql = "select * from recibos where ordenante_remesa_id = " + ordenanteRemesa.getRm2id();
            ResultSet r = Rentamaster2DB.getResultSet(sql);
            while (r.next()) {
                Recibo rbo = new Recibo();
                entitiesToPersist.add(rbo);
                try {
                    rbo.setRm2id(r.getInt("id"));
                    ordenanteRemesa.getRecibos().add(rbo);
                    rbo.setOrdenanteRemesa(ordenanteRemesa);

                    rbo.setAmpliacion(r.getString("ampliacion"));
                    rbo.setFechaEmision(r.getDate("fechaEmision"));
                    rbo.setTotalRecibo(r.getDouble("totalRecibo"));
                    rbo.setTotalReciboPostCCAA(r.getDouble("totalReciboPostCCAA"));
                    try {
                        ProgramacionRecibo pr = new ContratosInquilinos().devuelveProgramacionReciboDesdeSofiaId(r.getInt("programacion_recibo_id"), persistence);
                        rbo.setProgramacionRecibo(pr);
                    } catch (Exception exc) {
                        //no lanzamos exception pues puede ser un recibo individualizado con remesa

                    }
                    rbo.setNumRecibo(r.getString("numRecibo"));
                    rbo.setFechaValor(r.getDate("fechaValor"));


                    Serie serie = new Series().devuelveSerieDesdeSofiaId(r.getInt("serie_id"), persistence);
                    rbo.setSerie(serie);
                    rbo.setUtilitarioContratoInquilino(new ContratosInquilinos().devuelveContratoInquilinoDesdeSofiaId(r.getInt("utilitario_contrato_inquilino_id"), persistence));
                    rbo.setGradoEstadoImpago(ReciboGradoImpago.fromId(r.getInt("gradoEstadoImpago")));
                    rbo.setFechaEstadoPendiente(r.getDate("fechaEstadoPendiente"));
                    rbo.setFechaEstadoIncobrable(r.getDate("fechaEstadoIncobrable"));
                    rbo.setUtilitarioInquilino(new Personas().devuelvePersonaDesdeSofiaId(r.getInt("utilitario_inquilino_persona_id"), persistence));
//                    if (rbo.getUtilitarioInquilino()==null){
//                        if (rbo.getUtilitarioContratoInquilino()!=null){
//                            if (rbo.getUtilitarioContratoInquilino().getInquilino()!=null) {
//                                Persona p = rbo.getUtilitarioContratoInquilino().getInquilino();
//                                rbo.setUtilitarioInquilino(p);
//                            }
//                        }
//
//                    }

                    //implementaciones conceptos
                    String sqlic = "select * from implementaciones_conceptos_recibos where recibo_id = " + rbo.getRm2id();
                    ResultSet ric = Rentamaster2DB.getResultSet(sqlic);
                    while (ric.next()) {
                        ImplementacionConcepto ic = new ImplementacionConcepto();
                        entitiesToPersist.add(ic);
                        ic.setImporte(ric.getDouble("importe"));
                        ic.setImportePostCCAA(ric.getDouble("importePostCCAA"));
                        try {
                            ic.setConceptoRecibo(new ContratosInquilinos().devuelveConceptoRecibooDesdeSofiaId(ric.getInt("concepto_recibo_id"), persistence));
                        } catch (Exception exc) {
                            throw exc;
                        }
                        ic.setRecibo(rbo);
                        rbo.getImplementacionesConceptos().add(ic);
                        ic.setOverrideConcepto(new Conceptos().devuelveConceptoDesdeIdSofia(ric.getInt("override_concepto_id"), persistence));


                        //racas
                        String sqlraca = "select * from registros_aplicaciones_conceptos_adicionales where implementacion_concepto_id = " + ric.getInt("id");
                        ResultSet racas = Rentamaster2DB.getResultSet(sqlraca);
                        while (racas.next()) {
                            RegistroAplicacionConceptoAdicional raca = new RegistroAplicacionConceptoAdicional();
                            entitiesToPersist.add(raca);
                            raca.setConceptoAdicional(new ConceptosAdicionales().getConceptoAdicionalDesdeSofiaId(racas.getInt("concepto_adicional_id"), persistence));
                            raca.setImplementacionConcepto(ic);
                            ic.getRegistroAplicacionesConceptosAdicionales().add(raca);
                            raca.setNumDocumento(rbo.getNumRecibo());
                            try {
                                raca.setNifDni(rbo.getProgramacionRecibo().getContratoInquilino().getDepartamento().getPropietarioEfectivo().getPersona().getNifDni());
                            } catch (Exception exc) {
                                int y = 2;
                            }
                            raca.setFechaValor(racas.getDate("fechaValor"));
                            raca.setBase(racas.getDouble("Base"));
                            raca.setPorcentaje(racas.getDouble("porcentaje"));
                            raca.setImporteAplicado(racas.getDouble("importeAplicado"));
                        }
                        racas.close();


                    }
                    ric.close();

                    //recibos cobrados
                    String sqlrc = "select * from recibos_cobrados where recibo_id = " + rbo.getRm2id();
                    ResultSet rrc = Rentamaster2DB.getResultSet(sqlrc);
                    while (rrc.next()) {
                        ReciboCobrado rc = new ReciboCobrado();
                        entitiesToPersist.add(rc);
                        rc.setRecibo(rbo);
                        rbo.getRecibosCobrados().add(rc);
                        rc.setAmpliacionIngreso(rrc.getString("ampliacionIngreso"));
                        rc.setDescripcion(rrc.getString("descripcion"));
                        rc.setFechaCobro(rrc.getDate("fechaCobro"));
                        rc.setModoIngreso(ReciboCobradoModoIngreso.fromId(rrc.getInt("modoIngreso")));
                        rc.setTotalIngreso(rrc.getDouble("totalCobrado"));
                        rc.setCobranzas(rrc.getDouble("cobranzas"));

                    }
                    rrc.close();

                    recibos.add(rbo);
                }catch(Exception exc){
                    throw exc;
                }
            }
            r.close();
        }catch(Exception exc){
            throw exc;
        }
        return recibos;

    }

    public void realizaImportacionRemesas(DataManager dataManager, Persistence persistence) throws Exception{
        String sql = "select * from remesas where year(fechaAdeudo) >= " + annoDesdeImportacion;
        ResultSet r = Rentamaster2DB.getResultSet(sql);
        while(r.next()){
            entitiesToPersist.clear();
            double totalesRemesa = 0.0;
            Remesa rm = new Remesa();
            entitiesToPersist.add(rm);
            OrdenanteRemesa or = null;
            List<Recibo> recibos = null;
            rm.setRm2id(r.getInt("id"));
            rm.setFechaAdeudo(r.getDate("fechaAdeudo"));
            rm.setFechaRealizacion(r.getDate("fechaRealizacion"));
            rm.setIdentificadorRemesa(r.getString("identificadorRemesa"));
            DefinicionRemesa dr = new DefinicionesRemesas().devuelveDefinicionRemesaDesdeSofiaId(r.getInt("definicion_remesa_id"), persistence);
            rm.setDefinicionRemesa(dr);
            rm.setFechaValor(r.getDate("fechaValor"));
            //ordenantes remesa
            String sqlor = "select * from ordenantes_remesa where remesa_id = " + rm.getRm2id();
            ResultSet rom = Rentamaster2DB.getResultSet(sqlor);
            while(rom.next()){
                or = new OrdenanteRemesa();
                entitiesToPersist.add(or);
                or.setRm2id(rom.getInt("id"));
                or.setRemesa(rm);
                rm.getOrdenantesRemesa().add(or);
                or.setDefinicionRemesa(dr);
                recibos = realizaImportacionRecibos(persistence, or, entitiesToPersist);
                for (int i = 0; i < recibos.size(); i++) {
                    Recibo recibo =  recibos.get(i);
                    totalesRemesa += recibo.getTotalReciboPostCCAA();
                }
                or.setRecibos(recibos);

            }
            rm.setTotalRemesa(totalesRemesa);



            rom.close();

            dataManager.commit(new CommitContext(entitiesToPersist));

            realizaImportacionHelpersParaRemesa(persistence, rm);

        }
        r.close();



    }

    private void realizaImportacionHelpersParaRemesa(Persistence persistence, Remesa r){
        //realizo la migracion de datos para cada recibo importado a la tabla z_helper para acelerar la produccion de reports
        ArrayList rbos = new ArrayList();
        for(int i = 0;i < r.getOrdenantesRemesa().size();i++){
            OrdenanteRemesa or = r.getOrdenantesRemesa().get(i);
            for (int j = 0;j < or.getRecibos().size();j++){
                Recibo rbo = or.getRecibos().get(j);
                if (rbo.getNumRecibo().compareTo("18A19-0004")==0){
                    int y = 2;
                }
                AppBeans.get(HelperRecibosInformeIva.class)._procesaRecibo(rbo.getId());
            }
        }


    }

    /**
     * Creo que este metodo es innecesario pues los individualizados con ordenante remesa, podrian ir por el metodo normal
     * @param persistence
     * @return
     * @throws Exception
     */

    public void realizaImportacionRecibosIndividualizadosSinRemesa(DataManager dataManager, Persistence persistence) throws Exception{
        entitiesToPersist.clear();
        ArrayList<Recibo> recibos = new ArrayList<Recibo>();
        String sql = "select * from recibos where programacion_recibo_id is null and year(fechaEmision) >= " + annoDesdeImportacion;
        ResultSet r = Rentamaster2DB.getResultSet(sql);
        while(r.next()){
            //si el recibo ya se ha importado por la otra funcion se descarta
            Integer rm2id = r.getInt("id");
            if (devuelveReciboDesdeSofiaId(rm2id, persistence)!=null){
                continue;
            }
            entitiesToPersist.clear();
            Recibo rbo = new Recibo();
            entitiesToPersist.add(rbo);
            rbo.setRm2id(rm2id);
            rbo.setAmpliacion(r.getString("ampliacion"));
            rbo.setFechaEmision(r.getDate("fechaEmision"));
            rbo.setTotalRecibo(r.getDouble("totalRecibo"));
            rbo.setTotalReciboPostCCAA(r.getDouble("totalReciboPostCCAA"));

            rbo.setNumRecibo(r.getString("numRecibo"));
            rbo.setFechaValor(r.getDate("fechaValor"));


            rbo.setOrdenanteRemesa(null);//cuidado esto puede estar mal
            Serie serie = new Series().devuelveSerieDesdeSofiaId(r.getInt("serie_id"), persistence);
            rbo.setSerie(serie);
            rbo.setUtilitarioContratoInquilino(new ContratosInquilinos().devuelveContratoInquilinoDesdeSofiaId(r.getInt("utilitario_contrato_inquilino_id"), persistence));
            rbo.setGradoEstadoImpago(ReciboGradoImpago.fromId(r.getInt("gradoEstadoImpago")));
            rbo.setFechaEstadoPendiente(r.getDate("fechaEstadoPendiente"));
            rbo.setFechaEstadoIncobrable(r.getDate("fechaEstadoIncobrable"));
            rbo.setUtilitarioInquilino(new Personas().devuelvePersonaDesdeSofiaId(r.getInt("utilitario_inquilino_persona_id"), persistence));

            //implementaciones conceptos
            String sqlic = "select * from implementaciones_conceptos_recibos where recibo_id = " + rbo.getRm2id();
            ResultSet ric = Rentamaster2DB.getResultSet(sqlic);
            while(ric.next()){
                ImplementacionConcepto ic = new ImplementacionConcepto();
                entitiesToPersist.add(ic);
                ic.setImporte(ric.getDouble("importe"));
                ic.setImportePostCCAA(ric.getDouble("importePostCCAA"));
                try{
                    ic.setConceptoRecibo(new ContratosInquilinos().devuelveConceptoRecibooDesdeSofiaId(ric.getInt("concepto_recibo_id"), persistence));
                }catch(Exception exc){

                }
                ic.setRecibo(rbo);
                rbo.getImplementacionesConceptos().add(ic);
                ic.setOverrideConcepto(new Conceptos().devuelveConceptoDesdeIdSofia(ric.getInt("override_concepto_id"), persistence));

                //racas
                String sqlraca = "select * from registros_aplicaciones_conceptos_adicionales where implementacion_concepto_id = " + ric.getInt("id");
                ResultSet racas = Rentamaster2DB.getResultSet(sqlraca);
                while(racas.next()){
                    RegistroAplicacionConceptoAdicional raca = new RegistroAplicacionConceptoAdicional();
                    entitiesToPersist.add(raca);
                    raca.setConceptoAdicional(new ConceptosAdicionales().getConceptoAdicionalDesdeSofiaId(racas.getInt("concepto_adicional_id"), persistence));
                    raca.setImplementacionConcepto(ic);
                    ic.getRegistroAplicacionesConceptosAdicionales().add(raca);
                    raca.setNumDocumento(rbo.getNumRecibo());
                    raca.setNifDni(racas.getString("nifDni"));
                    raca.setFechaValor(racas.getDate("fechaValor"));
                    raca.setBase(racas.getDouble("Base"));
                    raca.setPorcentaje(racas.getDouble("porcentaje"));
                    raca.setImporteAplicado(racas.getDouble("importeAplicado"));
                }
                racas.close();


            }
            ric.close();

            //recibos cobrados
            String sqlrc = "select * from recibos_cobrados where recibo_id = " + rbo.getRm2id();
            ResultSet rrc = Rentamaster2DB.getResultSet(sqlrc);
            while(rrc.next()){
                ReciboCobrado rc = new ReciboCobrado();
                entitiesToPersist.add(rc);
                rc.setRecibo(rbo);
                rbo.getRecibosCobrados().add(rc);
                rc.setAmpliacionIngreso(rrc.getString("ampliacionIngreso"));
                rc.setDescripcion(rrc.getString("descripcion"));
                rc.setFechaCobro(rrc.getDate("fechaCobro"));
                rc.setModoIngreso(ReciboCobradoModoIngreso.fromId(rrc.getInt("modoIngreso")));
                rc.setTotalIngreso(rrc.getDouble("totalCobrado"));
                rc.setCobranzas(rrc.getDouble("cobranzas"));

            }
            rrc.close();




            recibos.add(rbo);

            dataManager.commit(new CommitContext(entitiesToPersist));
        }
        r.close();


    }

    public Recibo devuelveReciboDesdeSofiaId(Integer id, Persistence persistence) throws Exception{
        Transaction t = persistence.createTransaction();
        String hql = "SELECT r FROM test1_Recibo r WHERE r.rm2id = " + id;
        Recibo r = (Recibo) persistence.getEntityManager().createQuery(hql).getFirstResult();
        t.close();
        return r;
    }



}
