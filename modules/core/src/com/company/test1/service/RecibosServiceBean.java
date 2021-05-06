package com.company.test1.service;

import com.company.test1.core.HelperRecibosInformeIva;
import com.company.test1.entity.conceptosadicionales.RegistroAplicacionConceptoAdicional;
import com.company.test1.entity.contratosinquilinos.ContratoInquilino;
import com.company.test1.entity.departamentos.Departamento;
import com.company.test1.entity.departamentos.Ubicacion;
import com.company.test1.entity.enums.EstadoContratoInquilinoEnum;
import com.company.test1.entity.enums.recibos.ConceptoReciboVigenciaEnum;
import com.company.test1.entity.enums.recibos.DefinicionRemesaTipoGiroEnum;
import com.company.test1.entity.enums.recibos.ReciboCobradoModoIngreso;
import com.company.test1.entity.enums.recibos.ReciboGradoImpago;
import com.company.test1.entity.extroles.Propietario;
import com.company.test1.entity.ordenescobro.OrdenCobro;
import com.company.test1.entity.recibos.*;
import com.company.test1.service.accessory.OrdenacionImplementacionConcepto;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.PersistenceHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.*;

@Service(RecibosService.NAME)
public class RecibosServiceBean implements RecibosService {

    @Inject
    Persistence persistence;
    @Inject
    DataManager dataManager;
    @Inject
    NumberUtilsService numberUtilsService;



    public List<Recibo> devuelveRecibosPendientesDeConciliacionZHelper(Date fechaDesde, Date fechaHasta) throws Exception{
        List<Recibo> al = new ArrayList<Recibo>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String sql = "SELECT r.id FROM recibo r LEFT JOIN z_helper_proceso_recibos_informeiva z on z.recibo_id = r.id WHERE z.id is null and " +
                "r.FECHA_EMISION >= '" + sdf.format(fechaDesde) + "' and r.FECHA_EMISION <= '" + sdf.format(fechaHasta) + "'";
        Transaction t = persistence.createTransaction();
        List<String> ids = persistence.getEntityManager().createNativeQuery(sql).getResultList();
        for (int i = 0; i < ids.size(); i++) {
            UUID id = com.company.test1.StringUtils.toUUID(ids.get(i));
            Recibo r = persistence.getEntityManager().find(Recibo.class, id);
            if (r == null) continue;
            al.add(r);
        }
        t.close();
        return al;
    }


    public Remesa generaRemesaAcordeADatos(Date fechaRealizacion, Date fechaValor, Date fechaCargo, List<ContratoInquilino> contratos, Serie serie) throws Exception{

        String tipoGiros = "";
        /* Montando hashtable de contratos por definicion remesa*/
        Hashtable definicionesRemesaContratos = new Hashtable();
        for (int i = 0; i < contratos.size(); i++) {
            ContratoInquilino contratoInquilino = contratos.get(i);
            contratoInquilino = dataManager.reload(contratoInquilino, "contratoInquilino-view");
            DefinicionRemesa dr = contratoInquilino.getProgramacionRecibo().getDefinicionRemesa();
            if (tipoGiros.indexOf(dr.getTipoGiro().getId().toString())==-1){
                tipoGiros+= dr.getTipoGiro().getId().toString() + ";";
            }
            List<ContratoInquilino> cc = (List<ContratoInquilino>) definicionesRemesaContratos.get(dr);
            if (cc==null){
                cc = new ArrayList();
                definicionesRemesaContratos.put(dr,cc);
            }
            cc.add(contratoInquilino);
        }

        //si se encuentra mas de un tipo de giro lanzar exception
        tipoGiros.substring(0, tipoGiros.length()-1);
        String[] arrTipos = tipoGiros.split(";");
        if (arrTipos.length!=1){
            throw new Exception("No se puede combinar en una misma remesa diferentes tipos de giro");
        }


        Remesa r = new Remesa();

        if (definicionesRemesaContratos.size()==1){
            r.setDefinicionRemesa((DefinicionRemesa) new ArrayList(definicionesRemesaContratos.keySet()).get(0));
        }

        r.setFechaAdeudo(fechaCargo);
        r.setFechaRealizacion(fechaRealizacion);
        r.setFechaValor(fechaValor);

        double totalesRemesa = 0.0;


        ArrayList<DefinicionRemesa> l_ddrr = new ArrayList(definicionesRemesaContratos.keySet());
        Enumeration ddrr = definicionesRemesaContratos.keys();
        while(ddrr.hasMoreElements()){
            DefinicionRemesa dr = (DefinicionRemesa) ddrr.nextElement();
            List<ContratoInquilino> cc = (List<ContratoInquilino>)definicionesRemesaContratos.get(dr);
            OrdenanteRemesa or = new OrdenanteRemesa();
            or.setDefinicionRemesa(dr);
            or.setRemesa(r);
            for (int i = 0; i < cc.size(); i++) {
                ContratoInquilino contratoInquilino = cc.get(i);
                if (contratoInquilino.getProgramacionRecibo().getDesactivarProgramacion()==null){

                }else{
                    if (contratoInquilino.getProgramacionRecibo().getDesactivarProgramacion()){
                        continue;
                    }
                }

                Recibo rbo = null;
                try {
                    contratoInquilino = dataManager.reload(contratoInquilino, "contratoInquilino-genera-recibo-view");
                    rbo = this.generaReciboParaContrato(contratoInquilino, or, fechaValor, serie);

                    if (rbo != null) {
                        //si la programacion de recibo esta desactivada devuelve null
                        or.getRecibos().add(rbo);
                    }
                    totalesRemesa += rbo.getTotalReciboPostCCAA();
                }catch(Exception exc){
                    throw exc;
                }
            }
            r.getOrdenantesRemesa().add(or);
        }

         if (l_ddrr.size()==1){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd hhmm");
            DefinicionRemesa drRemesa = l_ddrr.get(0);
            r.setDefinicionRemesa(drRemesa);
            r.setIdentificadorRemesa(calculaIdentificadorRemesa(drRemesa, fechaRealizacion));
        }

        r.setTotalRemesa(totalesRemesa);

        realizaNumeracionDeRecibosEnRemesas(Arrays.asList(new Remesa[]{r}), fechaValor);

//        //generando la numeracion de recibos
//        Hashtable<Ubicacion,Integer> contadorUbicaciones = new Hashtable<Ubicacion,Integer>();
//        List<OrdenanteRemesa> oorr = r.getOrdenantesRemesa();
//        for (int i = 0; i < oorr.size(); i++) {
//            OrdenanteRemesa or = oorr.get(i);
//            List<Recibo> rr = or.getRecibos();
//            for (int j = 0; j < rr.size(); j++) {
//                Recibo rbo = rr.get(j);
//                //generacion del numero de recibo y asignacion
//                Ubicacion u = rbo.getProgramacionRecibo().getContratoInquilino().getDepartamento().getUbicacion();
//                Integer numRbo = contadorUbicaciones.get(u);
//                if (numRbo==null){
//                    String strNumRbo = generaNuevoNumeroReciboSegunUbicacionYAno(u, r.getFechaValor());
//
//                    rbo.setNumRecibo(strNumRbo);
//                    contadorUbicaciones.put(u, strNumRbo);
//                }else{
//                    ++numRbo;
//                    contadorUbicaciones.put(u, numRbo);
//                    rbo.setNumRecibo(numRbo.toString());
//                }
//            }
//        }

        return r;
    }

    public boolean persisteRemesas(List<Remesa> rr) throws Exception{
        Transaction t = persistence.createTransaction();

        List<Recibo> rbos = new ArrayList<Recibo>();

        for (int i = 0; i < rr.size(); i++) {
            persistence.getEntityManager().merge(rr.get(i));
            Remesa r = rr.get(i);
            for (int j = 0; j < r.getOrdenantesRemesa().size(); j++) {
                OrdenanteRemesa or = r.getOrdenantesRemesa().get(j);
                persistence.getEntityManager().merge(or);
                for (int k = 0; k < or.getRecibos().size(); k++) {
                    Recibo rbo = or.getRecibos().get(k);
                    persistence.getEntityManager().merge(rbo);
                    rbos.add(rbo);
                    for (int l = 0; l < rbo.getImplementacionesConceptos().size(); l++) {
                        ImplementacionConcepto ic = rbo.getImplementacionesConceptos().get(l);
                        persistence.getEntityManager().merge(ic);
                        for (int m = 0; m < ic.getRegistroAplicacionesConceptosAdicionales().size(); m++) {
                            RegistroAplicacionConceptoAdicional raca = ic.getRegistroAplicacionesConceptosAdicionales().get(m);
                            persistence.getEntityManager().merge(raca);
                        }
                    }
                    for (int l = 0; l < rbo.getRecibosCobrados().size(); l++) {
                        ReciboCobrado rc = rbo.getRecibosCobrados().get(l);
                        persistence.getEntityManager().merge(rc);

                    }
                    for (int l = 0; l < rbo.getOrdenesCobro().size(); l++) {
                        OrdenCobro oc = rbo.getOrdenesCobro().get(l);
                        persistence.getEntityManager().merge(oc);
                    }
                }
            }
        }
        t.commit();
        t.close();

        //generando los registros de iva: pendiente reubicar
        for (int i = 0; i < rbos.size(); i++) {
           registraReciboEnTablaZHelper(rbos.get(i));

        }

        return true;
    }

    //pendiente: eliminar este metodo por una solucino unificada de creeacion
    //de registros en tabla helper. Idealmente en el entitylistener de recibo
    public boolean registraReciboEnTablaZHelper(Recibo r) throws Exception{
        if (r.getSerie().getNombreSerie().compareTo("REGULARIZACIONES")==0){
            //los recibos de la serie de regularizaciones no se comtabilizan en el iva
            return false;
        }
        new HelperRecibosInformeIva().procesaRecibo(r, persistence);
        return true;
    }

    //pendiente: eliminar por el mismo motivo que el metodo anterior
    //solucionar el metodo unificado de escritura retrocesion en z_helper
    public boolean retrocedeRecibosEnZHelper(List<Recibo> rr) throws Exception{
        Transaction t = persistence.createTransaction();
        for (int i = 0; i < rr.size() ; i++) {
            UUID id = rr.get(i).getUuid();
            String iddelete = id.toString().replace("-", "");
            String deleteSql = "delete from z_helper_proceso_recibos_informeiva WHERE recibo_id = '" + iddelete + "'";
            int res = persistence.getEntityManager().createNativeQuery(deleteSql).executeUpdate();

        }
        t.commit();
        return true;
    }

    public String calculaIdentificadorRemesa(DefinicionRemesa dr, Date fechaRealizacion) throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd hhmm");
        String identificador = dr.getNombreRemesa() + sdf.format(fechaRealizacion) + "000";

        Remesa r = devuelveRemesaDesdeIdentificador(identificador);
        while(r!=null){
            String s = identificador.substring(identificador.length()-1);
            try{
                Integer i = new Integer(s);
                i++;
                identificador = identificador.substring(0,identificador.length()-1) + i.toString();
                r = devuelveRemesaDesdeIdentificador(identificador);
            }catch(Exception exc){
                int y = 2;
            }
        }

        return identificador;
    }

    @Override
    /**
     * Metodo anulado, ejecutado en el GUI
     */
    public Recibo generaReciboIndividualizado(ContratoInquilino ci, Date fechaEmision, Boolean incluirEnRemesa, Serie serie, List<ImplementacionConcepto> l) throws Exception{
        Recibo rbo = dataManager.create(Recibo.class);
        String nuevoNumRecibo = generaNuevoNumeroReciboSegunUbicacionYAno(ci.getDepartamento().getUbicacion(), fechaEmision);
        rbo.setNumRecibo(nuevoNumRecibo);
        DefinicionRemesa dr = ci.getProgramacionRecibo().getDefinicionRemesa();
        if (incluirEnRemesa){
            Remesa r = dataManager.create(Remesa.class);
            r.setDefinicionRemesa(dr);
            r.setFechaAdeudo(fechaEmision);
            r.setFechaRealizacion(fechaEmision);
            r.setFechaValor(fechaEmision);
            r.setTotalRemesa(rbo.getTotalReciboPostCCAA());
            String nombreDefinicionRemesa = ci.getProgramacionRecibo().getDefinicionRemesa().getNombreRemesa();
            String abrevUbicDepto = ci.getDepartamento().getUbicacion().getAbreviacionUbicacion() + ci.getDepartamento().getAbreviacionPisoPuerta();
            String identificadorRemesa = generaIdentificadorRemesa(nombreDefinicionRemesa, fechaEmision, abrevUbicDepto);
            r.setIdentificadorRemesa(identificadorRemesa);
            OrdenanteRemesa or = dataManager.create(OrdenanteRemesa.class);
            r.getOrdenantesRemesa().add(or);
            rbo.setOrdenanteRemesa(or);

        }
        rbo.setFechaEmision(fechaEmision);
        rbo.setAmpliacion("");
        rbo.setGradoEstadoImpago(ReciboGradoImpago.ORDINARIO);
        rbo.setSerie(serie);
        rbo.setUtilitarioContratoInquilino(ci);
        rbo.setUtilitarioInquilino(ci.getInquilino());
        rbo.setImplementacionesConceptos(l);
        return rbo;
    }

    public String generaIdentificadorRemesa(String nombreDefinicionRemesa, Date fechaEmision, String abrevUbicacionDepto) throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String strFechaEmision = sdf.format(fechaEmision);
        String identificadorRemesa = nombreDefinicionRemesa + strFechaEmision + abrevUbicacionDepto;
        Remesa rem = devuelveRemesaDesdeIdentificador(identificadorRemesa);
        Integer contador = 1;
        while(rem!=null){
            String nuevoIdentificadorRemesa = identificadorRemesa + "_" + contador.toString();
            rem = devuelveRemesaDesdeIdentificador(nuevoIdentificadorRemesa);
            if (rem==null){
                identificadorRemesa = nuevoIdentificadorRemesa;
            }else{
                contador += 1;
            }
        }
        return identificadorRemesa;
    }

    public Remesa devuelveRemesaDesdeIdentificador(String identificador) throws Exception{
        String sql = "SELECT r FROM test1_Remesa r WHERE r.identificadorRemesa LIKE :ir";
        Hashtable ht = new Hashtable();
        ht.put("ir", identificador);
        Transaction t = persistence.createTransaction();
        Remesa r = (Remesa) persistence.getEntityManager().createQuery(sql).setParameter("ir", identificador).getFirstResult();
        t.close();
        return r;
    }

    public Recibo generaReciboParaContrato(ContratoInquilino contratoInquilino, OrdenanteRemesa ordentanteRemesa, Date fechaValorP, Serie serie) throws Exception{

        Date fechaValor = DateUtils.truncate(fechaValorP, Calendar.DAY_OF_MONTH);

        Recibo recibo = new Recibo();
        recibo.setSerie(serie);
        recibo.setUtilitarioContratoInquilino(contratoInquilino);
        recibo.setUtilitarioInquilino(contratoInquilino.getInquilino());

        recibo.setAmpliacion("");
        recibo.setFechaEmision(fechaValor);
        recibo.setFechaValor(fechaValor);

        double totalRecibo = 0.0;
        double totalReciboPostCCAA = 0.0;

        //implementacion de conceptos de recibo
        ArrayList<ImplementacionConcepto> iiccrr = new ArrayList<ImplementacionConcepto>();
        ProgramacionRecibo pr = contratoInquilino.getProgramacionRecibo();
        if (pr.getDesactivarProgramacion()) return null;

        List<ConceptoRecibo> ccrr = pr.getConceptosRecibo();
        for (int i = 0; i < ccrr.size(); i++) {
            ConceptoRecibo conceptoRecibo = ccrr.get(i);


            boolean omitir = !incluirConceptoReciboEnReciboParaFechaValor(conceptoRecibo, fechaValor);

            if (omitir) continue;

            ImplementacionConcepto ic = new ImplementacionConcepto();
            ic.setConceptoRecibo(conceptoRecibo);

            List<ConceptoAdicionalConceptoRecibo> lcacr = conceptoRecibo.getConceptosAdicionalesConceptoRecibo();
            for (int j = 0; j < lcacr.size(); j++) {
                ConceptoAdicionalConceptoRecibo cacr = lcacr.get(j);
                RegistroAplicacionConceptoAdicional raca = new RegistroAplicacionConceptoAdicional();
                raca.setBase(getImporteAjustadoProrrateo(conceptoRecibo));
                raca.setConceptoAdicional(cacr.getConceptoAdicional());
                raca.setFechaValor(recibo.getFechaEmision());
                raca.setImplementacionConcepto(ic);
                raca.setPorcentaje(cacr.getPorcentaje());
                double importeAplicado = raca.getBase() * raca.getPorcentaje();
                importeAplicado = numberUtilsService.roundToNDecimals(importeAplicado, 2.0);
                raca.setImporteAplicado(importeAplicado);
                raca.setNifDni(contratoInquilino.getInquilino().getNifDni());
                ic.getRegistroAplicacionesConceptosAdicionales().add(raca);
            }



            //redondear
            double importe = numberUtilsService.roundToNDecimals(getImporteAjustadoProrrateo(conceptoRecibo), 2.0);
            ic.setImporte(importe);



            List<RegistroAplicacionConceptoAdicional> lrrccaa = ic.getRegistroAplicacionesConceptosAdicionales();
            double importePostCCAA = getImporteAjustadoProrrateo(conceptoRecibo);
            for (int j = 0; j < lrrccaa.size(); j++) {
                RegistroAplicacionConceptoAdicional raca = lrrccaa.get(j);
                importePostCCAA += numberUtilsService.roundToNDecimals(getImporteAplicadoConSignoSegunConceptoAdicional(raca), 2.0);
            }

            ic.setImportePostCCAA(numberUtilsService.roundToNDecimals(importePostCCAA, 2.0));
            ic.setRecibo(recibo);

            totalRecibo += numberUtilsService.roundToNDecimals(getImporteSegunSigno(ic), 2.0);
            totalReciboPostCCAA += numberUtilsService.roundToNDecimals(getImportePostCCAASegunSigno(ic), 2.0);

            iiccrr.add(ic);
        }

        recibo.setImplementacionesConceptos(iiccrr);

        /**
         *  El nº de recibo debe generarse antes de guardar las remesas de manera que
         *  obtengamos el primero de cada ubicacion por bbdd y el resto acumulemos dinamicamente
         */

        recibo.setProgramacionRecibo(pr);
        if (ordentanteRemesa!=null){
            recibo.setOrdenanteRemesa(ordentanteRemesa);
        }

        recibo.setTotalRecibo(numberUtilsService.roundToNDecimals(totalRecibo, 2.0));
        recibo.setTotalReciboPostCCAA(numberUtilsService.roundToNDecimals(totalReciboPostCCAA, 2.0));

        //generando movimiento de cobro
        if (recibo.getOrdenanteRemesa()==null){
            return recibo;
        }
        if (recibo.getOrdenanteRemesa().getDefinicionRemesa().getTipoGiro() == DefinicionRemesaTipoGiroEnum.BANCARIA){

            OrdenCobro ocr = generaOrdenCobroDesdeRecibo(recibo);
            recibo.getOrdenesCobro().add(ocr);

        }

        return recibo;
    }

    public List<Recibo> getTodosLosRecibosAsociadosParaRemesas(List<Remesa> remesas){

        ArrayList<Recibo> al = new ArrayList<Recibo>();
        for (int i = 0; i < remesas.size(); i++) {
            Remesa remesa =  remesas.get(i);
            List<OrdenanteRemesa> oorr = remesa.getOrdenantesRemesa();
            for (int j = 0; j < oorr.size(); j++) {
                OrdenanteRemesa ordenanteRemesa =  oorr.get(j);
                List<Recibo> rr = ordenanteRemesa.getRecibos();
                for (int k = 0; k < rr.size(); k++) {
                    Recibo recibo =  rr.get(k);
                    al.add(recibo);
                }
            }
        }
        return al;
    }

    private OrdenCobro generaOrdenCobroDesdeRecibo(Recibo recibo){
        OrdenCobro ocr = new OrdenCobro();
        ocr.setRecibo(recibo);
        ocr.setFechaValor(recibo.getFechaEmision());
        ocr.setImporte(recibo.getTotalReciboPostCCAA());

        return ocr;
    }

    private boolean incluirConceptoReciboEnReciboParaFechaValor(ConceptoRecibo conceptoRecibo, Date fechaValor) throws Exception{
        //comprobacion vigencia
        boolean omitir = false; // Por defecto false, a menos que se diga lo contamos
        fechaValor = DateUtils.truncate(fechaValor, Calendar.DAY_OF_MONTH);
        ConceptoReciboVigenciaEnum vigencia = conceptoRecibo.getVigencia();
        if (vigencia == ConceptoReciboVigenciaEnum.PERMANENTE){
            Date fvcr = conceptoRecibo.getFechaValor();
            if ((fvcr != null) && (fvcr.getTime() > fechaValor.getTime())) {
                omitir = true;
            }
        }else if (vigencia == ConceptoReciboVigenciaEnum.ACTIVACION){
            omitir = !conceptoRecibo.getActivadoDesactivado();

        }else if (vigencia == ConceptoReciboVigenciaEnum.ENTRE_FECHAS){
            Date fechaDesde = conceptoRecibo.getFechaDesde();
            Date fechaHasta = conceptoRecibo.getFechaHasta();

            omitir = (fechaValor.getTime() < fechaDesde.getTime()) || (fechaValor.getTime() > fechaHasta.getTime());

        }else if (vigencia == ConceptoReciboVigenciaEnum.NUMERO_EMISIONES){
            try {
                //obtención del nº de veces que se ha emitido este conceptorecibo
                String sql = "SELECT count(irid.id) FROM test1_ImplementacionConcepto irid "
                        + " JOIN irid.conceptoRecibo cr WHERE cr.id = '" + conceptoRecibo.getId() + "'";
                Transaction t = persistence.createTransaction();
                Number n = (Number) persistence.getEntityManager().createQuery(sql).getFirstResult();
                t.close();
                if (conceptoRecibo.getNumEmisiones().intValue()==0){
                    omitir = true;
                }
                else omitir = conceptoRecibo.getNumEmisiones().intValue() <= n.intValue();
            } catch (Exception ex) {
                throw ex;
            }
        }
        boolean incluir = !omitir;
        return incluir;
    }

    public Double getImporteAjustadoProrrateo(ConceptoRecibo cr){
        //redondear
        double importeRedondeado = numberUtilsService.roundToNDecimals(cr.getImporte(), 2.0);

        if (cr.getProgramacionRecibo()!=null){
            Boolean omitirEnProrrateo = cr.getOmitirEnProrrateo();
            if (omitirEnProrrateo==null){
                omitirEnProrrateo = false;
            }
            if (!omitirEnProrrateo){
                if ((cr.getProgramacionRecibo().getProrrateoProximaEmision()!=null)&&(cr.getProgramacionRecibo().getProrrateoProximaEmision()>0.0)){
                    importeRedondeado = importeRedondeado * cr.getProgramacionRecibo().getProrrateoProximaEmision().doubleValue();
                    importeRedondeado = numberUtilsService.roundToNDecimals(importeRedondeado, 2.0);
                }
            }
        }

        return importeRedondeado;
    }

    //metodos accesorios RegistroAplicacionConceptoAdicional

    private Double getImporteAplicadoConSignoSegunConceptoAdicional(RegistroAplicacionConceptoAdicional raca){
        if (raca.getConceptoAdicional().getAdicionSustraccion()) {
            return raca.getImporteAplicado();
        } else {
            return -raca.getImporteAplicado();
        }
    }

    //metodos accesorios ImplementacionConcepto

    private Double getImporteSegunSigno(ImplementacionConcepto ic){
        if (ic.getConcepto().getAdicionSustraccion()) return ic.getImporte();

        return -ic.getImporte();
    }

    private Double getImportePostCCAASegunSigno(ImplementacionConcepto ic){
        if (ic.getConcepto().getAdicionSustraccion()) return ic.getImportePostCCAA();

        return -ic.getImportePostCCAA();
    }

    public Double getTotalesRemesa(Remesa r){
        Transaction t = persistence.createTransaction();
        Number n = (Number) persistence.getEntityManager().createQuery("select sum(r.totalReciboPostCCAA) from test1_Recibo r join r.ordenanteRemesa orm join orm.remesa rem where rem.id = '" + r.getId() + "'").getFirstResult();
        t.close();
        return n.doubleValue();
    }


    public List<Recibo> devuelveImpagadosAsociados(List<Propietario> propietarios, List<Departamento> ddpp,
                                                   Date fechaInicial, Date fechaFinal, String estadoContrato, String incobrables, Serie serie) throws Exception {
//        tipocontrato => 0=nueva lau 1=antigua lau
        List<UUID> listIdsDepartamentos = new ArrayList<UUID>();
        for (int i = 0; i < ddpp.size(); i++) {
            Departamento  d =  ddpp.get(i);
            listIdsDepartamentos.add(d.getId());
        }




        List res = new ArrayList();

        String hql = "SELECT r, rc, ci, s  FROM test1_Recibo r  LEFT JOIN r.recibosCobrados rc JOIN r.utilitarioContratoInquilino ci " +
                "JOIN r.serie s JOIN ci.departamento d WHERE " +
                "r.fechaEmision >= :fd AND r.fechaEmision <= :fh " +
                "AND d.id in :list";

        Transaction t = persistence.createTransaction();
        List l = persistence.getEntityManager().createQuery(hql).setParameter("fd", fechaInicial).setParameter("fh", fechaFinal).
                setParameter("list", listIdsDepartamentos).getResultList();
        t.close();
        ArrayList<Recibo> rbosImpagados = new ArrayList<Recibo>();
        HashMap<Recibo, Double> map = new HashMap<Recibo, Double>();

        for (int i = 0; i < l.size(); i++) {
            Recibo recibo = (Recibo) ((Object[])l.get(i))[0];
            ReciboCobrado rc = (ReciboCobrado) ((Object[])l.get(i))[1];
            ContratoInquilino ci = (ContratoInquilino) ((Object[])l.get(i))[2];
            if (estadoContrato!=null){
                if (estadoContrato.compareTo("VIGENTE")==0){
                    if (ci.getEstadoContrato().compareTo(EstadoContratoInquilinoEnum.VIGENTE)!=0){
                        continue;
                    }
                }
                if (estadoContrato.compareTo("NO VIGENTE")==0){
                    if (ci.getEstadoContrato().compareTo(EstadoContratoInquilinoEnum.VIGENTE)==0){
                        continue;
                    }
                }
//                EstadoContratoInquilinoEnum estado = EstadoContratoInquilinoEnum.valueOf(estadoContrato);
//                if (ci.getEstadoContrato().compareTo(estado)!=0){
//                    continue;
//                }
            }
            if (incobrables!=null){
                ReciboGradoImpago gi = ReciboGradoImpago.valueOf(incobrables);
                if (recibo.getGradoEstadoImpago().compareTo(gi)!=0){
                    continue;
                }
            }
            if (serie!=null){
                Serie s_ = (Serie) ((Object[])l.get(i))[3];
                if (s_.getUuid().compareTo(serie.getUuid())!=0){
                    continue;
                }

            }
            Serie s = (Serie) ((Object[])l.get(i))[3];
            if (!map.containsKey(recibo)){
                map.put(recibo, 0.0);
            }
            Double acc = map.get(recibo);
            try{
                if (rc!=null){
                    if (rc.getModoIngreso()== ReciboCobradoModoIngreso.BANCARIO){
                        acc += rc.getTotalIngreso();
                    }
                    if (rc.getModoIngreso()== ReciboCobradoModoIngreso.DEVUELTO){
                        acc -= rc.getTotalIngreso();
                    }
                    if (rc.getModoIngreso()== ReciboCobradoModoIngreso.ADMINISTRACION){
                        acc += rc.getTotalIngreso();
                    }
                    if (rc.getModoIngreso()== ReciboCobradoModoIngreso.INGRESO_TALON){
                        acc += rc.getTotalIngreso();
                    }
                    if (rc.getModoIngreso()== ReciboCobradoModoIngreso.COMPENSACION_ABONO_RECIBO){
                        acc += rc.getTotalIngreso();
                    }
                }

            }catch(Exception exc){
                int y = 2;
            }


            map.put(recibo, acc);
        }

        Iterator<Recibo> ki = map.keySet().iterator();
        while(ki.hasNext()){
            Recibo r = ki.next();
            Double acc = map.get(r);
            Double diff = r.getTotalReciboPostCCAA()-acc;
            if (diff>0.0099){
                rbosImpagados.add(r);
            }
//            if (r.getTotalReciboPostCCAA()>acc){
//                rbosImpagados.add(r);
//            }
        }

        return rbosImpagados;
    }

    public Double getTotalIngresadoDeRecibo(Recibo r){
        List<ReciboCobrado> rrcc = r.getRecibosCobrados();
        Double acc = 0.0;
        for (int i = 0; i < rrcc.size(); i++) {
            ReciboCobrado rc =  rrcc.get(i);

            if (rc.getModoIngreso()== ReciboCobradoModoIngreso.BANCARIO){
                acc += rc.getTotalIngreso();
            }
            if (rc.getModoIngreso()== ReciboCobradoModoIngreso.DEVUELTO){
                acc -= rc.getTotalIngreso();
            }
            if (rc.getModoIngreso()== ReciboCobradoModoIngreso.ADMINISTRACION){
                acc += rc.getTotalIngreso();
            }
            if (rc.getModoIngreso()== ReciboCobradoModoIngreso.INGRESO_TALON){
                acc += rc.getTotalIngreso();
            }
            if (rc.getModoIngreso()== ReciboCobradoModoIngreso.COMPENSACION_ABONO_RECIBO){
                acc += rc.getTotalIngreso();
            }
        }
        return acc;
    }

    public String generaNuevoNumeroReciboSegunUbicacionYAno(Ubicacion ub, Date fecha) throws Exception{


        String annoNombre = new SimpleDateFormat("yyyy").format(fecha);
        Integer anno = Integer.parseInt(annoNombre);

        String sql = "SELECT DISTINCT r.numRecibo FROM test1_Recibo r"

                //"SELECT MAX(CONVERT(INTEGER, r.numRecibo)) FROM Recibo r"
//                + " JOIN r.programacionRecibo pr"
                + " JOIN r.utilitarioContratoInquilino c"
                + " JOIN c.departamento dep"
                + " JOIN dep.ubicacion ub"
                + " WHERE FUNCTION('YEAR',r.fechaEmision) = " + anno.toString()
                + " AND ub.id = :ubid"
                + " AND r.numRecibo IS NOT NULL";







        Transaction t = persistence.createTransaction();

        List<String> listaNumerosRecibo = (List<String>) persistence.getEntityManager().createQuery(sql).setParameter("ubid", ub.getId()).getResultList();

        int maxNumRecibo = -1;
        for (int i = 0; i < listaNumerosRecibo.size(); i++) {
            String string = listaNumerosRecibo.get(i);
            /**
             * Desde Enero de 2016 se adopta la forma [yyyy][AbreviacionUbicacion]-[NoRecibo]
             * Ej: 2016V62-23, 2016PAX-2, etc...
             *
             */
            //tomo unicamnete l oque hay a la derecha del guion
            //a efectos de la generacion del recibo tomo lo que está a la derecha del guion
            //y así reaprovecho el patron de generación anterior
            String[] arr = string.split("-");
            if (arr.length==2){
                //tomo lo de la derecha
                string = arr[1];
            }else if(arr.length==1){
                //en el caso que se cuele un numero de recibo con el patron anterior
                string = arr[0];
            }
            if ((string!=null)&&(string.compareTo("PTE"))!=0){

                int valor = Integer.parseInt(string);
                if (valor > maxNumRecibo){
                    maxNumRecibo = valor;
                }
            }
        }

        String resNum = "";
        Integer num = null;
        if (maxNumRecibo==-1){
            num = 1;
        }else{
            num =  maxNumRecibo + 1;
        }

        resNum = StringUtils.repeat("0", 4 - num.toString().length()) + num.toString();

        SimpleDateFormat sdf = new SimpleDateFormat("yy");

        return sdf.format(fecha) + ub.getAbreviacionUbicacion().toUpperCase() + "-" + resNum;
        //Integer maxNumRecibo = (Integer) sl.executeNamedDynamicQuerySingleResult(sql, pams);
        //return maxNumRecibo + 1;

    }

    public List<Recibo> devuelveRecibosSinRemesasParaDatos(Date fechaDesde, Date fechaHasta, DefinicionRemesaTipoGiroEnum tipoGiro){

        String hql = "select r FROM test1_Recibo r where r.ordenanteRemesa is null and r.fechaEmision >= :fd and r.fechaEmision <= :fh";
        Transaction t = persistence.createTransaction();

        List<Recibo> rr = persistence.getEntityManager().createQuery(hql).setParameter("fd", fechaDesde).setParameter("fh",fechaHasta).getResultList();
        t.close();
        return rr;
    }


    public Remesa generaRemesaParaRecibos(List<Recibo> rbos, DefinicionRemesa dr, Date fechaValor, Date fechaAdeudo, Date fechaRealizacion, String identificadorRemesa){
        OrdenanteRemesa orem = new OrdenanteRemesa();
//        for (int i = 0; i < rbos.size(); i++) {
//            Recibo recibo =  rbos.get(i);
//            recibo.setOrdenanteRemesa(orem);
//            orem.getRecibos().add(recibo);
//        }
        Remesa remesa = new Remesa();
        orem.setRemesa(remesa);
        remesa.getOrdenantesRemesa().add(orem);
        remesa.setFechaValor(fechaValor);
        remesa.setFechaAdeudo(fechaAdeudo);
        remesa.setFechaRealizacion(fechaRealizacion);
        remesa.setDefinicionRemesa(dr);
        remesa.setIdentificadorRemesa(identificadorRemesa);
        return remesa;
    }

    public Double getTotalConceptoAdicionalAplicadoARecibo(String nombreCA, Recibo r){
        double totales = 0.0;
        List<ImplementacionConcepto> l = r.getImplementacionesConceptos();
        for (int i = 0; i < l.size(); i++) {
            ImplementacionConcepto implementacionConcepto = l.get(i);
            List<RegistroAplicacionConceptoAdicional> l2 = implementacionConcepto.getRegistroAplicacionesConceptosAdicionales();
            for (int j = 0; j < l2.size(); j++) {
                RegistroAplicacionConceptoAdicional raca = l2.get(j);
                String nombreConcepto = raca.getConceptoAdicional().getNombre();
                if (nombreConcepto.compareTo(nombreCA)==0){

                    if (implementacionConcepto.getConcepto().getAdicionSustraccion()){
                        totales += raca.getImporteAplicado();
                    }else{
                        totales -= raca.getImporteAplicado();
                    }
                }
            }

        }
        return totales;
    }

    public List<ImplementacionConcepto> getVersionAgregadaPorConceptos(List<ImplementacionConcepto> l){

        Hashtable<Concepto, ImplementacionConcepto> htIC = new Hashtable<Concepto, ImplementacionConcepto>();

        for (int i = 0; i < l.size(); i++) {
            ImplementacionConcepto ic = l.get(i);
            Transaction t = persistence.createTransaction();
            if (!PersistenceHelper.isNew(ic)){
                ic = persistence.getEntityManager().reload(ic, "implementacionConcepto-view");
            }

            t.close();
            ImplementacionConcepto icr = htIC.get(ic.getConcepto());
            if (icr==null){
                icr = new ImplementacionConcepto();


                icr.setImporte(ic.getImporte());
                icr.setImportePostCCAA(ic.getImportePostCCAA());
                icr.setOverrideConcepto(ic.getConcepto());
                icr.setRecibo(ic.getRecibo());
                htIC.put(ic.getConcepto(), icr);

                List<RegistroAplicacionConceptoAdicional> rrccaa = ic.getRegistroAplicacionesConceptosAdicionales();
                List<RegistroAplicacionConceptoAdicional> rrccaa_ = new ArrayList<RegistroAplicacionConceptoAdicional>();
                for (int j = 0; j < rrccaa.size(); j++) {
                    RegistroAplicacionConceptoAdicional raca = rrccaa.get(j);
                    RegistroAplicacionConceptoAdicional raca_ = new RegistroAplicacionConceptoAdicional();
                    raca_.setBase(raca.getBase());
                    raca_.setConceptoAdicional(raca.getConceptoAdicional());
                    raca_.setFechaValor(raca.getFechaValor());
                    raca_.setImplementacionConcepto(icr);
                    raca_.setImporteAplicado(raca.getImporteAplicado());
                    raca_.setNifDni(raca.getNifDni());
                    raca_.setPorcentaje(raca.getPorcentaje());
                    rrccaa_.add(raca_);
                }
            }else{
                double importe = icr.getImporte() + ic.getImporte();
                importe = numberUtilsService.roundTo2Decimals(importe);
                icr.setImporte(importe);

                double importeP = icr.getImportePostCCAA() + ic.getImportePostCCAA();
                importeP = numberUtilsService.roundTo2Decimals(importeP);
                icr.setImportePostCCAA(importeP);

                //rrccaa
                List<RegistroAplicacionConceptoAdicional> rrccaa = ic.getRegistroAplicacionesConceptosAdicionales();
                List<RegistroAplicacionConceptoAdicional> rrccaa_ = icr.getRegistroAplicacionesConceptosAdicionales();
                for (int j = 0; j < rrccaa.size(); j++) {
                    RegistroAplicacionConceptoAdicional raca = rrccaa.get(j);
                    for (int k = 0; k < rrccaa_.size(); k++) {
                        RegistroAplicacionConceptoAdicional raca_ = rrccaa_.get(k);
                        //si coincide en tipo y nombre de concepto lo asigno
                        if (raca_.getConceptoAdicional().getId().compareTo(raca.getConceptoAdicional().getId())==0){
                            //si el porcentaje es el mismo
                            if (raca_.getPorcentaje().doubleValue()==raca.getPorcentaje().doubleValue()){
                                //lo añado
                                double base = raca_.getBase();
                                base += raca.getBase();
                                double ia = raca_.getImporteAplicado();
                                ia += raca.getImporteAplicado();
                                raca_.setBase(base);
                                raca_.setImporteAplicado(ia);
                            }
                        }
                    }
                }
            }

        }
        List<ImplementacionConcepto> res = new ArrayList<ImplementacionConcepto>();
        Enumeration<Concepto> e = htIC.keys();
        while(e.hasMoreElements()){
            Concepto c = e.nextElement();
            ImplementacionConcepto ic = htIC.get(c);
            res.add(ic);
        }
        return res;
    }

    public double getTotalCobrado(Recibo recibo){
        double d = 0;
        for (int i = 0; i < recibo.getRecibosCobrados().size(); i++) {
            ReciboCobrado rc = recibo.getRecibosCobrados().get(i);
            if ((rc.getModoIngreso()==ReciboCobradoModoIngreso.ADMINISTRACION)||(rc.getModoIngreso()==ReciboCobradoModoIngreso.BANCARIO)||(rc.getModoIngreso()==ReciboCobradoModoIngreso.INGRESO_TALON)){
                d += rc.getTotalIngreso();
            }else if(rc.getModoIngreso()==ReciboCobradoModoIngreso.DEVUELTO){
                d -= rc.getTotalIngreso();
            }
        }
        return d;
    }

    public double getTotalDevuelto(Recibo r){
        try {
            double d = 0;
            for (int i = 0; i < r.getRecibosCobrados().size(); i++) {
                ReciboCobrado rc = r.getRecibosCobrados().get(i);
                if (rc.getModoIngreso() == ReciboCobradoModoIngreso.DEVUELTO) {
                    d += rc.getTotalIngreso();
                    if (rc.getCobranzas()!=null){
                        d += rc.getCobranzas();
                    }

                }
            }
            return d;
        }catch(Exception exc){
            return 0.0;
        }
    }

    public Date getFechaDevuelto(Recibo r) throws Exception {
        Date fechaDev = null;
        List<ReciboCobrado> l = r.getRecibosCobrados();
        for (int i = 0; i < l.size(); i++) {
            ReciboCobrado rc = l.get(i);
            if (rc.getModoIngreso()==ReciboCobradoModoIngreso.DEVUELTO){
                fechaDev = rc.getFechaCobro();
            }
        }
        return fechaDev;
    }

    public double getPorcentajeCobrado(Recibo r){
        double d = this.getTotalCobrado(r) / r.getTotalReciboPostCCAA();
        return d;
    }


    public double getTotalPendiente(Recibo r){
        try{
            double pendiente = r.getTotalReciboPostCCAA() + this.getTotalCobranzas(r) - this.getTotalCobrado(r) - this.getTotalCompensado(r);
            return pendiente;
        }catch(Exception exc){
            return 0.0;
        }

    }

    public double getTotalCompensado(Recibo r){
        double d = 0;
        for (int i = 0; i < r.getRecibosCobrados().size(); i++) {
            ReciboCobrado rc = r.getRecibosCobrados().get(i);
            if (rc.getModoIngreso()==ReciboCobradoModoIngreso.COMPENSACION_ABONO_RECIBO){
                d += rc.getTotalIngreso();
            }
        }
        return d;
    }

    public double getTotalesConceptoVigente(ProgramacionRecibo pr, Concepto c)
            throws Exception{
        double totalConcepto = 0.0;
        List<ConceptoRecibo> ccrr = pr.getConceptosRecibo();
        for (int i = 0; i < ccrr.size(); i++) {
            ConceptoRecibo conceptoRecibo = ccrr.get(i);
            if (conceptoRecibo.getConcepto().getId().compareTo(c.getId())==0){
                if (getEsVigenteEnFecha(conceptoRecibo, new Date())){
                    totalConcepto += conceptoRecibo.getImporte();
                }
            }
        }
        return totalConcepto;
    }

    private boolean getEsVigenteEnFecha(ConceptoRecibo cr, Date d)
            throws Exception{
        if (cr.getVigencia().compareTo(ConceptoReciboVigenciaEnum.PERMANENTE)==0){
            return true;
        }else if(cr.getVigencia().compareTo(ConceptoReciboVigenciaEnum.ENTRE_FECHAS)==0){
            return (cr.getFechaDesde().getTime() < d.getTime()) && (cr.getFechaHasta().getTime() > d.getTime());
        }else if(cr.getVigencia().compareTo(ConceptoReciboVigenciaEnum.ACTIVACION)==0){
            return cr.getActivadoDesactivado();
        }else if(cr.getVigencia().compareTo(ConceptoReciboVigenciaEnum.NUMERO_EMISIONES)==0){
            int numemisiones = cr.getNumEmisiones();
            String sql = "SELECT count(ic.id) FROM ImplementacionConcepto ic JOIN ic.conceptoRecibo cr WHERE "
                    + "cr.id = " + cr.getId();
            Transaction t = persistence.createTransaction();
            Number n = (Number) persistence.getEntityManager().createQuery(sql).getFirstResult();
            t.close();
            return n.intValue() < numemisiones;
        }
        return false;
    }

    public double getTotalCobranzas(Recibo r){
        double cobranzas = 0;
        for (int i = 0; i < r.getRecibosCobrados().size(); i++) {
            ReciboCobrado rc = r.getRecibosCobrados().get(i);
            if ((rc.getModoIngreso()==ReciboCobradoModoIngreso.ADMINISTRACION)||(rc.getModoIngreso()==ReciboCobradoModoIngreso.BANCARIO)){
                //no afecta
            }else if(rc.getModoIngreso()==ReciboCobradoModoIngreso.DEVUELTO){
                if (rc.getCobranzas()==null){
                    rc.setCobranzas(0.0);
                }
                cobranzas += rc.getCobranzas();
            }
        }
        return cobranzas;
    }

    public Recibo getUltimoReciboGirado(Departamento d) throws Exception{
        String s = "SELECT r FROM test1_Recibo r JOIN r.programacionRecibo pr JOIN pr.contratoInquilino c JOIN c.departamento d WHERE d.id = :did ORDER BY r.fechaEmision desc";
        Hashtable pams = new Hashtable();
        pams.put("did",d.getId());
        Transaction t = persistence.createTransaction();
        List<Recibo> rr = persistence.getEntityManager().createQuery(s).setParameter("did", d.getId()).getResultList();
        t.close();
        if (rr.size()>0)
            return rr.get(0);
        else return null;
    }

    public Double getProrrateoUltimoContratoDepartamento(Departamento d) throws Exception{
        String sql = "SELECT pr.prorrateoProximaEmision FROM test1_ProgramacionRecibo pr"
                + " JOIN pr.contratoInquilino c"
                + " JOIN c.departamento dep"
                + " WHERE 1=1"
                + " AND dep.id = :did"
                + " ORDER BY c.fechaRealizacion DESC";


        Transaction t = persistence.createTransaction();
        List<Double> prorrateos = (List<Double>) persistence.getEntityManager().createQuery(sql).setParameter("did", d.getId()).getResultList();
        t.close();

        if ((prorrateos == null) || (prorrateos.isEmpty())){
            return null;
        }

        return prorrateos.get(0);
    }

    public void realizaNumeracionDeRecibosEnRemesas(List<Remesa> rr, Date fechaValor) throws Exception{

        Hashtable<Ubicacion, List<Recibo>> uurbos = new Hashtable<Ubicacion,List<Recibo>>();
        /**
         * Organizacion de las remesas en listados de recibos por ubicacion
         */
        for (int i = 0; i < rr.size(); i++) {
            Remesa remesa = rr.get(i);
            List<OrdenanteRemesa> oorr = remesa.getOrdenantesRemesa();
            for (int j = 0; j < oorr.size(); j++) {
                OrdenanteRemesa or = oorr.get(j);
                List<Recibo> rbos = or.getRecibos();
                for (int k = 0; k < rbos.size(); k++) {
                    Recibo rbo = rbos.get(k);
                    Ubicacion u = rbo.getProgramacionRecibo().getContratoInquilino().getDepartamento().getUbicacion();
                    List<Recibo> l = uurbos.get(u);
                    if (l==null){
                        l = new ArrayList<Recibo>();
                        uurbos.put(u, l);
                    }
                    l.add(rbo);
                }

            }
        }

        //ahora ordeno cada lista de recibos por el id del departamento
        Enumeration<Ubicacion> e = uurbos.keys();
        while(e.hasMoreElements()){
            Ubicacion u = e.nextElement();
            List<Recibo> l = uurbos.get(u);
            //ordeno por id de departamento
            Recibo[] rbos = l.toArray(new Recibo[0]);
            Arrays.sort(rbos, new Comparator(){

                @Override
                public int compare(Object o1, Object o2) {
                    Recibo r1 = (Recibo) o1;
                    Recibo r2 = (Recibo) o2;
                    Departamento d1 = r1.getProgramacionRecibo().getContratoInquilino().getDepartamento();
                    Departamento d2 = r2.getProgramacionRecibo().getContratoInquilino().getDepartamento();

                    if  (d1.getRm2id().intValue()<d2.getRm2id().intValue())  return -1;

                    if  (d1.getRm2id().intValue()==d2.getRm2id().intValue()) return 0;

                    if (d1.getRm2id().intValue()>d2.getRm2id().intValue())  return 1;

                    return 0;
                }

            });
            //restitucion del array ordenado
            uurbos.put(u, Arrays.asList(rbos));
        }

        //ahora que ya tengo los recibos ordenados
        //los numero
        //primero almaceno los ultimos numeros de recibo por año/ubicacion para cada ubicacion
        Hashtable<Ubicacion, String> numeracion = new Hashtable<Ubicacion, String>();
        e = uurbos.keys();
        while(e.hasMoreElements()){
            Ubicacion u = e.nextElement();
            String numRbo = numeracion.get(u);
            if (numRbo==null){
                try{
//                    numRbo = Recibo.generaNuevoNumeroReciboSegunUbicacionYAno(u, fechaValor, sl);
                    numRbo = generaNuevoNumeroReciboSegunUbicacionYAno(u, fechaValor);
                }catch(Exception exc){
                    numRbo = new SimpleDateFormat("yyyy").format(fechaValor) + u.getAbreviacionUbicacion().toUpperCase() + "-" + "1";
                }

                numeracion.put(u, numRbo);
            }
        }
        //segundo: con dichos numeros asigno el numero acumulado en la lista ordenada de recibos
        e = uurbos.keys();
        while(e.hasMoreElements()){
            Ubicacion u = e.nextElement();
            List<Recibo> l = uurbos.get(u);
            for (int i = 0; i < l.size(); i++) {
                Recibo recibo = l.get(i);
                String numRbo = numeracion.get(u);

                recibo.setNumRecibo(numRbo);
                numRbo = incrementaNumRecibo(numRbo);
                numeracion.put(u, numRbo);
            }
        }
    }

    private static String incrementaNumRecibo(String numRecibo) throws Exception{
        //tomo lo que esta a la derecha del guion que ha de ser un entero y l eañado 1
        String[] arr = numRecibo.split("-");
        if (arr.length==2){
            String noRboString = arr[1];
            Integer i = Integer.parseInt(noRboString);
            i++;
            return arr[0] + "-" + StringUtils.repeat("0", 4 - i.toString().length()) + i;
        }else{
            throw new Exception("Error al incrementar la numeracion de recibo");
        }
    }


    public List<Recibo> getRecibosAsociadosAUbicacionesEntreFechas(Date fechaDesde, Date fechaHasta, List<Ubicacion> ubs) throws Exception{
        ArrayList<Recibo> al = new ArrayList<Recibo>();
        for (int i = 0; i < ubs.size(); i++) {
            Ubicacion u = ubs.get(i);
            List l = getRecibosAsociadosAUbicacionEntreFechas(fechaDesde, fechaHasta, u);
            al.addAll(l);
        }
        return al;
    }

    public List<Recibo> getRecibosAsociadosAUbicacionEntreFechas(Date fechaDesde, Date fechaHasta, Ubicacion ub) throws Exception {

        String sql = "SELECT r FROM test1_Recibo r"
                + " JOIN r.utilitarioContratoInquilino c "

                + " JOIN c.departamento dep"
                + " JOIN dep.ubicacion ub"
                + " WHERE r.fechaEmision BETWEEN :fechaInicial AND :fechaFinal"
                + " AND ub = :ubicacion";

        Hashtable ht = new Hashtable();

        ht.put("fechaInicial", fechaDesde);
        ht.put("fechaFinal", fechaHasta);
        ht.put("ubicacion", ub);


        Query q = persistence.getEntityManager().createQuery(sql);
        Enumeration en = ht.keys();
        while(en.hasMoreElements()){
            String k = (String) en.nextElement();
            q.setParameter(k, ht.get(k));
        }
        List<Recibo> recibos = q.getResultList();
        return recibos;
    }

    public List<ImplementacionConcepto> getImplementacionesConceptosAgregadas(Recibo recibo){
        List<ImplementacionConcepto> iicc = recibo.getImplementacionesConceptos();
        List<ImplementacionConcepto> iiccAgregados =  new ArrayList<ImplementacionConcepto>();

        boolean yaEstabaIncluido;

        for (int i = 0; i < iicc.size(); i++) {
            ImplementacionConcepto implementacionConcepto = iicc.get(i);
            yaEstabaIncluido = false;

            for (int j = 0; j < iiccAgregados.size(); j++) {
                ImplementacionConcepto ic = iiccAgregados.get(j);
                if (implementacionConcepto.getConcepto().getId() == ic.getConcepto().getId()){
                    yaEstabaIncluido=true;
                    ic.setImporte(ic.getImporte() + implementacionConcepto.getImporte());
                    ic.setImportePostCCAA(ic.getImportePostCCAA() + implementacionConcepto.getImportePostCCAA());
                    break;
                }
            }

            if (yaEstabaIncluido==false){
                ImplementacionConcepto ic = new ImplementacionConcepto();
                ic.setOverrideConcepto(implementacionConcepto.getConcepto());
                ic.setImporte(implementacionConcepto.getImporte());
                ic.setImportePostCCAA(implementacionConcepto.getImportePostCCAA());
//                ArrayListUtils.anadeSiNoPresenteEnLista(iiccAgregados, ic);
                if (iiccAgregados.indexOf(ic)==-1){
                    iiccAgregados.add(ic);
                }
            }
        }

        Collections.sort(iiccAgregados,getOrdenacionImplmentacionConcepto());
        return iiccAgregados;
    }

    public static Comparator getOrdenacionImplmentacionConcepto(){
        Comparator icComparator = new OrdenacionImplementacionConcepto();
        return icComparator;
    }

    public Concepto getConceptoDesdeAbreviacion(String abreviacion) throws Exception{
        String sql = "SELECT c FROM test1_Concepto c WHERE c.abreviacion LIKE :ab ";
        Hashtable pams = new Hashtable();
        pams.put("ab",abreviacion);

        Transaction t = persistence.createTransaction();
//        ConceptoBean c = (ConceptoBean ) sl.executeNamedDynamicQuerySingleResult(sql, pams);

        Concepto c = (Concepto) persistence.getEntityManager().createQuery(sql).setParameter("ab", abreviacion).getFirstResult();
        t.close();
        return c;
    }

    public List<Concepto> getConceptosDesdeAbreviacion(String abreviacion) throws Exception{
        String sql = "SELECT c FROM test1_Concepto c WHERE c.abreviacion LIKE :ab ";
        Hashtable pams = new Hashtable();
        pams.put("ab",abreviacion);

        Transaction t = persistence.createTransaction();
//        ConceptoBean c = (ConceptoBean ) sl.executeNamedDynamicQuerySingleResult(sql, pams);

        List<Concepto> cc = (List<Concepto>) persistence.getEntityManager().createQuery(sql).setParameter("ab", abreviacion).getResultList();
        t.close();
        return cc;
    }

    public Integer getValorOrdenacionParaNuevoConcepto(){
        String hql = "select max(c.ordenacion) from test1_Concepto c";
        Transaction t = persistence.createTransaction();
        Number n = (Number) persistence.getEntityManager().createQuery(hql).getSingleResult();
        if (n==null){
            n = 1;
        }
        t.close();
        return n.intValue()+1;
    }






}