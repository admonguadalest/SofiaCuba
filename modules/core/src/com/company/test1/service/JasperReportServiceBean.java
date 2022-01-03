package com.company.test1.service;

import com.company.test1.entity.FotosThumbnailExt;
import com.company.test1.entity.Persona;
import com.company.test1.entity.TreeItem;
import com.company.test1.entity.coeficientes.TipoCoeficiente;
import com.company.test1.entity.contratosinquilinos.Anexo;
import com.company.test1.entity.contratosinquilinos.ContratoInquilino;
import com.company.test1.entity.contratosinquilinos.Fianza;
import com.company.test1.entity.contratosinquilinos.ParametroValorAnexo;
import com.company.test1.entity.departamentos.Departamento;
import com.company.test1.entity.departamentos.Ubicacion;
import com.company.test1.entity.documentosfotograficos.CarpetaDocumentosFotograficos;
import com.company.test1.entity.documentosfotograficos.FotoDocumentoFotografico;
import com.company.test1.entity.documentosfotograficos.FotoThumbnail;
import com.company.test1.entity.enums.DocumentoImputableTipoEnum;
import com.company.test1.entity.enums.NombreTipoDireccion;
import com.company.test1.entity.extroles.Propietario;
import com.company.test1.entity.nonpersistententities.HelperInyeccionPlantilla;
import com.company.test1.entity.notificaciones.NotificacionContratoInquilino;
import com.company.test1.entity.ordenespago.RealizacionPago;
import com.company.test1.entity.recibos.*;
import com.company.test1.entity.reportsyplantillas.FlexReport;
import com.company.test1.entity.reportsyplantillas.Plantilla;
import com.company.test1.entity.validaciones.ValidacionImputacionDocumentoImputable;
import com.company.test1.jmx.Cubatest1DB;
import com.company.test1.jmx.Rentamaster2DB;
import com.company.test1.jmx.importadores.reports.FlexReports;
import com.company.test1.service.accessory.*;
import com.company.test1.service.accessory.imprimiblescontratoinquilino.AsistenteImpresionContrato;
import com.company.test1.service.accessory.reportprevisualizacionrecibo.HelperRecibo;
import com.google.common.io.Resources;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.Session;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.*;
import net.sf.jasperreports.data.DataSourceCollection;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.base.JRVirtualPrintPage;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.checkerframework.checker.signature.qual.InternalFormForNonArray;
import org.springframework.stereotype.Service;


import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service(JasperReportService.NAME)
public class JasperReportServiceBean implements JasperReportService {

    @Inject
    Persistence persistence;
    @Inject
    FileLoader fileLoader;
    @Inject
    Metadata metadata;
    @Inject
    NumberUtilsService numberUtilsService;
    @Inject
    MetadataTools metadataTools;
    @Inject
    RecibosService recibosService;
    @Inject
    NotificacionService notificacionService;

    public byte[] generaReportValidacionesIdis(List<ValidacionImputacionDocumentoImputable> l, DocumentoImputableTipoEnum tipo, Date fechaInicial, Date fechaFinal) throws Exception{
        if (fechaInicial == null){
            fechaInicial = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2000");
        }
        if (fechaFinal==null){
            fechaFinal = new Date();
        }

        ReportValidacionesImputacionDocumentoImputable rvidi = new ReportValidacionesImputacionDocumentoImputable(l, tipo, fechaInicial, fechaFinal);
        byte[] bb = rvidi.getReportAsByteArray();
        return bb;
    }


    public byte[] createReport() throws Exception{
        FlexReport fr = getFlexReportDesdeNombre("CARTA");
        try{
            JasperDesign designMaestro = JRXmlLoader.load(new ByteArrayInputStream(fr.getContenidoJrxml().getBytes()));
            JasperReport jr = JasperCompileManager.compileReport(designMaestro);
            Hashtable hm = new Hashtable();
            hm.put("backgroundSvg", turnFileIntoJRRenderable("carta.svg"));

            hm.put("contenidoNotificacion", "Pruebas de produccion de report sencillo");

            byte[] bb = produceReport(fr, hm, new ArrayList(), Cubatest1DB.getConnection());
            return bb;
        }catch(Exception exc){
            throw new Exception("El report JRXML no compilo exitósamente: " + fr.getNombre() + ". " + exc.getMessage());
        }


    }

    public byte[] generaReportModeloRenunciaContratoInquilino(ContratoInquilino ci) throws Exception{
        FlexReport fr = getFlexReportDesdeNombre("RENUNCIA_CONTRATO_INQUILINO");
        try{
            JasperDesign designMaestro = JRXmlLoader.load(new ByteArrayInputStream(fr.getContenidoJrxml().getBytes()));
            JasperReport jr = JasperCompileManager.compileReport(designMaestro);
            JRRenderable jrr = null;
            try{
                jrr = turnFileIntoJRRenderable("carta.svg");
            }catch(Exception exc){

            }
            Hashtable ht = new Hashtable();
            ht.put("contratoInquilinoId", ci.getId().toString().replace("-",""));
            ht.put("PLANTILLA_PAGINA_REPORT_PORTRAIT", jrr);
            String observaciones = ci.getObservacionesRenuncia();
            if (observaciones==null) observaciones = "";
            ht.put("observaciones", observaciones);
            byte[] bb = produceReport(fr, ht, new ArrayList(), Cubatest1DB.getConnection());
            return bb;
        }catch(Exception exc){
            throw new Exception("El report JRXML no compilo exitósamente: " + fr.getNombre() + ". " + exc.getMessage());
        }


    }

    public byte[] departamentosVacios(List<Departamento> dd) throws Exception{
        ReportVacios rv = new ReportVacios(dd);
        return rv.getReportAsByteArray();
    }

    public byte[] impagados(Propietario p, List<Recibo> impagados, Date fechaInicial, Date fechaFinal, String vigencia, String tipoRecibos) throws Exception{
        ReportImpagados ri = new ReportImpagados(recibosService, p, impagados, fechaInicial, fechaFinal, vigencia, tipoRecibos);
        return ri.getReportAsByteArray();
    }

    public byte[] __departamentosVacios_(List<Departamento> dd) throws Exception{
        FlexReport fr = getFlexReportDesdeNombre("DEPARTAMENTOS VACIOS");
        try{
            JasperDesign designMaestro = JRXmlLoader.load(new ByteArrayInputStream(fr.getContenidoJrxml().getBytes()));
            JasperReport jr = JasperCompileManager.compileReport(designMaestro);
            HashMap hm = new HashMap();
            hm.put("titulo", "Departamentos Vacios");



            ArrayList<Map<String,?>> al = new ArrayList<Map<String,?>>();
            for (int i = 0; i < dd.size(); i++) {
                Departamento d = dd.get(i);
                String hql = "select ci FROM test1_ContratoInquilino ci JOIN ci.departamento d WHERE d.id = '" + d.getId() + "' order by ci.fechaOcupacion desc";
                Transaction t = persistence.createTransaction();
                ContratoInquilino ci = (ContratoInquilino) persistence.getEntityManager().createQuery(hql).getFirstResult();
                t.close();
                Recibo r = null;
                if (ci!=null){
                    t = persistence.createTransaction();
                    hql = "select r FROM test1_Recibo r WHERE r.utilitarioContratoInquilino.id = '" + ci.getId() + "' order by r.fechaEmision desc";
                    r = (Recibo) persistence.getEntityManager().createQuery(hql).getFirstResult();
                    t.close();
                }


                HashMap<String,Object> m = new HashMap();
                m.put("nombre", d.getUbicacion().getNombre());
                m.put("piso", d.getUbicacion().getNombre());
                m.put("puerta", d.getUbicacion().getNombre());
                String tipo = "Vivienda";
                if (!d.getViviendaLocal()){
                    tipo = "Local";
                }
                m.put("tipo", tipo);
                m.put("estadoContrato", "RENUNCIADO");
                Double rc = 0.0;
                if (ci!=null) rc = ci.getRentaContractual();
                m.put("rentaContractual", rc);
                Date fechaUE = new Date();
                if (r!=null){
                    fechaUE = r.getFechaEmision();
                }
                m.put("ultimaEmisionRecibo", fechaUE);

                al.add(m);
            }

            byte[] bb = JasperRunManager.runReportToPdf(jr, hm, new JRMapCollectionDataSource(al));
            return bb;
        }catch(Exception exc){
            throw new Exception("El report JRXML no compilo exitósamente: " + fr.getNombre() + ". " + exc.getMessage());
        }
    }

    public byte[] listadoResumenRecibos(List<Remesa> remesas){
        ReportListadoResumenRecibos rlrr = new ReportListadoResumenRecibos(remesas);
        rlrr.setJasperReportService(this);

        byte[] bb = rlrr.getReportAsByteArray();
        return bb;
    }

    public byte[] listadoResumenRecibosFromZHelper(List<Remesa> remesas) throws Exception{
        String remesasIds = "";
        for (int i = 0; i < remesas.size(); i++) {
            Remesa get = remesas.get(i);
            if (i>0){
                remesasIds += ",";
            }
            remesasIds += "#" + get.getId().toString().replace("-", "") + "#";

        }

        Hashtable ht = new Hashtable();

        remesasIds = remesasIds.replace("-", "");
        ht.put("remesasIds", remesasIds);

        FlexReport fr = getFlexReportDesdeNombre("INFORME REMESAS DETALLE");
        try{
             JasperDesign designMaestro = JRXmlLoader.load(new ByteArrayInputStream(fr.getContenidoJrxml().getBytes()));
            JasperReport jr = JasperCompileManager.compileReport(designMaestro);

            //patch: PENDIENTE ARREGLAR
            Statement s = Cubatest1DB.getConnection().createStatement();
            s.execute("set sql_mode='STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';");


            ArrayList al = new ArrayList();al.add("0");
            byte[] bb = JasperRunManager.runReportToPdf(jr, ht, Cubatest1DB.getConnection());
            return bb;
        }catch(Exception exc){
            throw new Exception("El report JRXML no compilo exitósamente: " + fr.getNombre() + ". " + exc.getMessage());
        }



    }

    public JRRenderable turnFileIntoJRRenderable(String fileName) throws Exception{
        Transaction t = persistence.createTransaction();
        String hql = "select fd FROM sys$FileDescriptor fd WHERE fd.name = :n";
        FileDescriptor fd = (FileDescriptor) persistence.getEntityManager().createQuery(hql).setParameter("n", fileName).getFirstResult();


        InputStream bais  = fileLoader.openStream(fd);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        org.apache.commons.io.IOUtils.copy(bais, baos);
        JRRenderable jrr = null;
        if ("jpg gif png".indexOf(fd.getExtension())!=-1){
            jrr = JRImageRenderer.getInstance(baos.toByteArray());
        }
        if(fd.getExtension().compareTo("svg")==0){
            jrr = net.sf.jasperreports.renderers.BatikRenderer.getInstance(baos.toByteArray());
        }

        return jrr;

    }

    public Object turnFileIntoJRRenderableObject(String fileName) throws Exception{
        Transaction t = persistence.createTransaction();
        String hql = "select fd FROM sys$FileDescriptor fd WHERE fd.name = :n";

        //con sucesivas correcciones y migraciones de la bbdd es posible que se deba recargar los archivos. Con este script
        //salvo la excepcion hasta que encuentro uno valido. Sino halla ninguno valido saltará despues la excepcion
        //por tanto es correcto
        List<FileDescriptor> ffdd = persistence.getEntityManager().createQuery(hql).setParameter("n", fileName).getResultList();
        FileDescriptor fd = null;
        InputStream bais = null;
        for (int i = 0; i < ffdd.size(); i++) {
            fd = ffdd.get(i);
            try{
                bais = fileLoader.openStream(fd);
            }catch(Exception exc){

            }

        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        org.apache.commons.io.IOUtils.copy(bais, baos);
        JRRenderable jrr = null;
        if ("jpg gif png".indexOf(fd.getExtension())!=-1){
            jrr = JRImageRenderer.getInstance(baos.toByteArray());
        }
        if(fd.getExtension().compareTo("svg")==0){
            jrr = net.sf.jasperreports.renderers.BatikRenderer.getInstance(baos.toByteArray());
        }

        return jrr;

    }

    public FlexReport getFlexReportDesdeNombre(String nombre){
        String hql = "select fr FROM test1_FlexReport fr WHERE fr.nombre = :nombre";
        Transaction t = persistence.createTransaction();
        Query q = persistence.getEntityManager().createQuery(hql);
        q.setParameter("nombre", nombre);
        FlexReport fr = (FlexReport) q.getFirstResult();
        return fr;
    }

    @Override
    public byte[] getReportDinamico(String titulo, Class baseClass, Collection<Entity> entitiesCollection, List<String> idPaths, List<String> colnames) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        List<Entity> entities = new ArrayList<Entity>(entitiesCollection);
        List<List<String>> rows = new ArrayList<List<String>>();
        for (int i = 0; i < entities.size(); i++) {
            Entity entity =  entities.get(i);
            List<String> paths = idPaths;
            List<String> row = new ArrayList<String>();
            for (int j = 0; j < paths.size(); j++) {
                String s =  paths.get(j);
                Object o = MyBeanUtils.readBeanPath(entity, s);
                if (o instanceof Date){
                    o = sdf.format((Date)o);
                }
                if (o!=null){
                    row.add(o.toString());
                }else{
                    row.add("");
                }

            }
            rows.add(row);
        }
        ReportDinamico rd = new ReportDinamico(titulo, colnames, rows, null, new Hashtable<String, Object>());

        byte[] bb = rd.runReport();
        return bb;
    }

    @Override
    public byte[] getReportDinamico(String titulo, Class baseClass, Collection<Entity> entitiesCollection, List<String> idPaths, List<String> colnames, Hashtable<String, Object> camposFooter) throws Exception {
        List<Entity> entities = new ArrayList<Entity>(entitiesCollection);
        List<List<String>> rows = new ArrayList<List<String>>();
        for (int i = 0; i < entities.size(); i++) {
            Entity entity =  entities.get(i);
            List<String> paths = idPaths;
            List<String> row = new ArrayList<String>();
            for (int j = 0; j < paths.size(); j++) {
                String s =  paths.get(j);
                Object o = MyBeanUtils.readBeanPath(entity, s);
                if (o!=null){
                    row.add(o.toString());
                }else{
                    row.add("");
                }

            }
            rows.add(row);
        }
        ReportDinamico rd = new ReportDinamico(titulo, colnames, rows, null, new Hashtable<String, Object>());
        rd.setCamposFooter(camposFooter);
        byte[] bb = rd.runReport();
        return bb;
    }

    public byte[] imprimirRecibo(Recibo recibo){
        return null;
    }

    public byte[] getReportRecibo(Recibo r) throws Exception{
        Transaction t = persistence.createTransaction();
        Recibo recibo = persistence.getEntityManager().reload(r, "recibo-report-view");
        t.close();
        ReportRecibo rr = new ReportRecibo(recibo);
        byte[] bb = rr.getReportAsByteArray();
        return bb;
    }

    public byte[] reportIncrementosGenerales(Ubicacion ub, Hashtable<Departamento, ConceptoRecibo[]> htDeptosCCRR, Concepto concepto, TipoCoeficiente tipoCoef,
                                             Integer mesesAtrasos, Date fechaRepercusion, Double importeRepercusion){
        ReportUbicacionListadoIncrementosGenerales rulig =
                new ReportUbicacionListadoIncrementosGenerales(
                        ub,
                        htDeptosCCRR,
                        concepto,
                        tipoCoef,
                        mesesAtrasos,
                        fechaRepercusion,
                        importeRepercusion);
        byte[] bb = rulig.getReportAsByteArray();
        return bb;
    }

    public byte[] reportIncrementosIndiceReferencia(ArrayList al_, Integer mesesAtrasos, Integer mes, Integer anno, Date fechaRepercusion){
        ReportIndiceReferencia rir = new ReportIndiceReferencia(al_, mesesAtrasos, mes, anno, fechaRepercusion);
        byte[] bb = rir.getReportAsByteArray();
        return bb;
    }


    /**
     * FUNCIONES RELATIVAS A REPORT DE ANALISIS DE DIFERENCIAS
     */

    //funcion tonta
    public List<Recibo> getTodosLosRecibos(Remesa r){
        List<Recibo> recibos = new ArrayList<Recibo>();
        for (int i = 0; i < r.getOrdenantesRemesa().size(); i++) {
            OrdenanteRemesa ordenanteRemesa = r.getOrdenantesRemesa().get(i);
            for (int j = 0; j < ordenanteRemesa.getRecibos().size(); j++) {
                Recibo recibo = ordenanteRemesa.getRecibos().get(j);
//                ArrayListUtils.anadeSiNoPresenteEnLista(recibos, recibo);
                if (recibos.indexOf(recibo)==-1){
                    recibos.add(recibo);
                }
            }
        }

        return recibos;
    }

    /**
     *
     * @param ccii
     * @param fechaRealizacion
     * @param fechaValor
     * @param fechaCargo
     * @param fechaDesdePeriodoAnterior
     * @param fechaHastaPeriodoAnterior
     * @param serie
     * @return
     * @throws Exception
     */

    public byte[] reportDiferenciasEntreEmisiones(List<ContratoInquilino> ccii, Date fechaRealizacion, Date fechaValor, Date fechaCargo, Date fechaDesdePeriodoAnterior, Date fechaHastaPeriodoAnterior, Serie serie) throws Exception{

        Transaction t = persistence.createTransaction();

        Date fR = fechaRealizacion;
        Date fV = fechaValor;
        Date fC = fechaCargo;

        Date fechaDesdePA = fechaDesdePeriodoAnterior;
        Date fechaHastaPA = fechaHastaPeriodoAnterior;

        if ((fR==null)||(fV==null)||(fC==null)){
           throw new Exception("Los datos fecha de Valor, Fecha de Realizacion y Fecha de Cargo no pueden ser nulas");
        }
        if ((fechaDesdePA==null)||(fechaHastaPA==null)){
            throw new Exception("Las fechas desde y hasta para el periodo anterior deben ser ingresadas");
        }


        ArrayList<Ubicacion> ubicaciones = new ArrayList<Ubicacion>();
        List<ContratoInquilino> contratos = ccii;

        List<Recibo> rr = new ArrayList<Recibo>();

        //organizacion de contratos por definiciones remesa para producir una remesa
        Hashtable<DefinicionRemesa,List<ContratoInquilino>> htDDRRCC = new Hashtable<DefinicionRemesa,List<ContratoInquilino>>();
        for (int i = 0; i < contratos.size(); i++) {
            ContratoInquilino c = contratos.get(i);
//            c = (ContratoInquilino) SIApplication.getCurrent().getCurrentProcess().getSessionLayer().getInstance(ContratoInquilino.class, c.getId());
            c = persistence.getEntityManager().reload(c, "contratoInquilino-view");

            DefinicionRemesa dr = c.getProgramacionRecibo().getDefinicionRemesa();
            List<ContratoInquilino> cc = htDDRRCC.get(dr);
            if (cc==null){
                cc = new ArrayList<ContratoInquilino>();
                htDDRRCC.put(dr,cc);
            }
            if (ubicaciones.indexOf(c.getDepartamento().getUbicacion())==-1){
                ubicaciones.add(c.getDepartamento().getUbicacion());
            }
            cc.add(c);

            Recibo r = recibosService.generaReciboParaContrato(c, null, fechaValor, serie);
            if (r !=null){
                rr.add(r);
            }

        }






//        List<Remesa> remesas = new ArrayList<Remesa>();
//
//        try{
//
//            Enumeration<DefinicionRemesa> e = htDDRRCC.keys();
//            while(e.hasMoreElements()){
//                DefinicionRemesa dr = e.nextElement();
//                List<ContratoInquilino> cc = htDDRRCC.get(dr);
//
//                if (!contratos.isEmpty()){
////                    Remesa r = Remesa.generaRemesaAcordeADatos(fR, fV, fC, cc, (Serie) selSerie.getValue(),SIApplication.getCurrent().getCurrentProcess().getSessionLayer());
//                    Remesa r = AppBeans.get(RecibosService.class).generaRemesaAcordeADatos(fR,fV,fC,contratos,serie);
//
//                    remesas.add(r);
//                }
//            }
//
//        }catch(Exception exc){
//            throw exc;
//        }

//        try {
//            //antes de persistir las remesas se han de numerar los recibos
////            Recibo.realizaNumeracionDeRecibosEnRemesas(remesas, fV, SIApplication.getCurrent().getCurrentProcess().getSessionLayer());
//            AppBeans.get(RecibosService.class).realizaNumeracionDeRecibosEnRemesas(remesas, fV);
//        } catch (Exception ex) {
////            Logger.getLogger(UCCuadreYRegistroRemesas.class.getName()).log(Level.SEVERE, null, ex);
////            Notification.show("No se pudieron generar las remesas para la seleccion dada: no se pudo numerar los recibos");
//            throw new Exception("No se pudieron generar las remesas para la selecciond dada: no se pudo numerar los recibos", ex);
//        }
        Hashtable<Ubicacion, Double> htProxEmision = new Hashtable<Ubicacion, Double>();
        TreeMap<Ubicacion, List<Recibo>> tmUbicacionesRecibosProxPeriodo = new TreeMap<Ubicacion, List<Recibo>>();


        for (int j = 0; j < rr.size(); j++) {
            Recibo get1 = rr.get(j);
            if (tmUbicacionesRecibosProxPeriodo.get(get1.getUtilitarioContratoInquilino().getDepartamento().getUbicacion())==null){
                tmUbicacionesRecibosProxPeriodo.put(get1.getUtilitarioContratoInquilino().getDepartamento().getUbicacion(), new ArrayList<Recibo>());
            }
            List<Recibo> recibos = tmUbicacionesRecibosProxPeriodo.get(get1.getUtilitarioContratoInquilino().getDepartamento().getUbicacion());
            recibos.add(get1);
            //actualizando totales por ubicacion
            if (htProxEmision.get(get1.getUtilitarioContratoInquilino().getDepartamento().getUbicacion())==null){
                htProxEmision.put(get1.getUtilitarioContratoInquilino().getDepartamento().getUbicacion(), 0.0);
            }
            Double d = htProxEmision.get(get1.getUtilitarioContratoInquilino().getDepartamento().getUbicacion());
            d+=get1.getTotalReciboPostCCAA();
            htProxEmision.put(get1.getUtilitarioContratoInquilino().getDepartamento().getUbicacion(), d);
        }

        //realizo la misma estructura de ubicaciones->recibos pero con los recibos emitidos entre las fechas seleccionadas
        //y calculando el total


        List<Recibo> recibos = null;
        try{
//            recibos = Recibo.getRecibosAsociadosAUbicacionesEntreFechas(fechaDesdePA.getValue(), fechaHastaPA.getValue(), ubicaciones, sl);
            recibos = recibosService.getRecibosAsociadosAUbicacionesEntreFechas(fechaDesdePA, fechaHastaPA, ubicaciones);
        }catch(Exception ex){
            throw new Exception("Error al extraer los recibos historicos", ex);
        }
        Hashtable<Ubicacion, Double> htAntEmision = new Hashtable<Ubicacion, Double>();

        TreeMap<Ubicacion, List<Recibo>> tmUbicacionesRecibosAnteriorPeriodo = new TreeMap<Ubicacion, List<Recibo>>();
        for (int i = 0; i < recibos.size(); i++) {
            Recibo get = recibos.get(i);
            if (tmUbicacionesRecibosAnteriorPeriodo.get(get.getUtilitarioContratoInquilino().getDepartamento().getUbicacion())==null){
                tmUbicacionesRecibosAnteriorPeriodo.put(get.getUtilitarioContratoInquilino().getDepartamento().getUbicacion(), new ArrayList<Recibo>());
            }
            List<Recibo> rbos = tmUbicacionesRecibosAnteriorPeriodo.get(get.getUtilitarioContratoInquilino().getDepartamento().getUbicacion());
            rbos.add(get);
            //actualizando totales por ubicacion
            if (htAntEmision.get(get.getUtilitarioContratoInquilino().getDepartamento().getUbicacion())==null){
                htAntEmision.put(get.getUtilitarioContratoInquilino().getDepartamento().getUbicacion(), 0.0);
            }
            Double d = htAntEmision.get(get.getUtilitarioContratoInquilino().getDepartamento().getUbicacion());
            d+=get.getTotalReciboPostCCAA();
            htAntEmision.put(get.getUtilitarioContratoInquilino().getDepartamento().getUbicacion(), d);

        }

        //ahora ya puedo realizar la comparacion
        //primero por ubicacion las altas
        TreeMap<Ubicacion, List<Recibo>> tma = devuelveRecibosDadosDeAlta(tmUbicacionesRecibosAnteriorPeriodo, tmUbicacionesRecibosProxPeriodo);

        //luego por ubicacion las bajas
        TreeMap<Ubicacion, List<Recibo>> tmb = devuelveRecibosDadosDeBaja(tmUbicacionesRecibosAnteriorPeriodo, tmUbicacionesRecibosProxPeriodo);
        // por ultimo por ubicacion los cambios
        TreeMap<Ubicacion, List<Alteracion>> tmdiffs = devuelveRecibosCoincidentesConAlteraciones(tmUbicacionesRecibosAnteriorPeriodo, tmUbicacionesRecibosProxPeriodo);


        //ahora monto la clase helper
        List<UtilReportDiferencias> l = new ArrayList<UtilReportDiferencias>();
        for (int i = 0; i < ubicaciones.size(); i++) {
            Ubicacion u = ubicaciones.get(i);
            UtilReportDiferencias urd = new UtilReportDiferencias();
            urd.setUbicacion(u);
            if (tma.get(u)!=null){
                urd.setAltas(tma.get(u));
                for (int j = 0; j < urd.getAltas().size(); j++) {
                    Recibo r = urd.getAltas().get(j);
                    urd.setTotalIncrementos(urd.getTotalIncrementos() + r.getTotalReciboPostCCAA());
                }
            }
            if (tmb.get(u)!=null){
                urd.setBajas(tmb.get(u));
                for (int j = 0; j < urd.getBajas().size(); j++) {
                    Recibo r = urd.getBajas().get(j);
                    urd.setTotalDecrementos(urd.getTotalDecrementos() + r.getTotalReciboPostCCAA());
                }
            }
            if (tmdiffs.get(u)!=null){
                urd.setAlteraciones(tmdiffs.get(u));
                for (int a = 0; a < urd.getAlteraciones().size(); a++) {
                    Alteracion al = urd.getAlteraciones().get(a);
                    double diff = al.getRecibo().getTotalReciboPostCCAA() - al.getReciboAnterior().getTotalReciboPostCCAA();
                    if (diff >=0){
                        urd.setTotalIncrementos(urd.getTotalIncrementos() + diff);
                    }else{
                        urd.setTotalDecrementos(urd.getTotalDecrementos() + Math.abs(diff));
                    }
                }
            }
            urd.setTotalPostCCAAAnterior(htAntEmision.get(urd.getUbicacion()));
            urd.setTotalPostCCAAProxEmision(htProxEmision.get(urd.getUbicacion()));
            //si esta vacio, no lo anyado
            int s = urd.getAltas().size() + urd.getBajas().size() + urd.getAlteraciones().size();
            if (s == 0) continue;
            l.add(urd);
        }
        Collections.sort(l, new Comparator<UtilReportDiferencias>(){

            @Override
            public int compare(UtilReportDiferencias o1, UtilReportDiferencias o2) {
                return o1.getUbicacion().getNombre().compareTo(o2.getUbicacion().getNombre());
            }

        });

        FlexReport fr = getFlexReportDesdeNombre("REPORT_DIFERENCIAS_EMISION_PERIODOS");
        Hashtable ht = new Hashtable();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String txtFechas = sdf.format(fechaDesdePA) + "-" + sdf.format(fechaHastaPA);
        ht.put("TITULO_REPORT", "REPORT DIFERENCIAS DE EMISION ENTRE PERIODOS: PROX.EMISION VS. " + txtFechas);
//                ht.put("P_IMAGEN", null);
        byte[] bb = produceReport(fr, ht, l);
        return bb;
    }

    private TreeMap<Ubicacion,List<Recibo>> devuelveRecibosDadosDeAlta(TreeMap<Ubicacion,List<Recibo>> tmA, TreeMap<Ubicacion, List<Recibo>> tm){
        TreeMap<Ubicacion,List<Recibo>> tmr = new TreeMap<Ubicacion,List<Recibo>>();
        Iterator<Ubicacion> it = tm.keySet().iterator();
        while(it.hasNext()){
            Ubicacion u = it.next();
            List<Recibo> rr = tm.get(u);
            List<Recibo> rra = tmA.get(u);
            a:
            for (int i = 0; i < rr.size(); i++) {
                Recibo get = rr.get(i);
                ContratoInquilino ci = get.getUtilitarioContratoInquilino();

                for (int j = 0; j < rra.size(); j++) {
                    Recibo get1 = rra.get(j);
                    ContratoInquilino ci2 = get1.getUtilitarioContratoInquilino();
                    if (ci.getId().compareTo(ci2.getId())==0){
                        continue a;
                    }
                }
                //no lo hayo, lo anyado
                if (tmr.get(u)==null){
                    tmr.put(u, new ArrayList<Recibo>());
                }
                tmr.get(u).add(get);

            }
        }
        return tmr;
    }

    private TreeMap<Ubicacion,List<Recibo>> devuelveRecibosDadosDeBaja(TreeMap<Ubicacion,List<Recibo>> tmA, TreeMap<Ubicacion, List<Recibo>> tm){
        TreeMap<Ubicacion,List<Recibo>> tmr = new TreeMap<Ubicacion,List<Recibo>>();
        Iterator<Ubicacion> ita = tmA.keySet().iterator();
        while(ita.hasNext()){
            Ubicacion u = ita.next();
            List<Recibo> rr = tm.get(u);
            List<Recibo> rra = tmA.get(u);
            a:
            for (int i = 0; i < rra.size(); i++) {
                Recibo get = rra.get(i);
                ContratoInquilino ci = get.getUtilitarioContratoInquilino();

                for (int j = 0; j < rr.size(); j++) {
                    Recibo get1 = rr.get(j);
                    ContratoInquilino ci2 = get1.getUtilitarioContratoInquilino();
                    if (ci.getId().compareTo(ci2.getId())==0){
                        continue a;
                    }
                }
                //no lo hayo, lo anyado
                if (tmr.get(u)==null){
                    tmr.put(u, new ArrayList<Recibo>());
                }
                tmr.get(u).add(get);

            }
        }
        return tmr;
    }

    private TreeMap<Ubicacion,List<Alteracion>> devuelveRecibosCoincidentesConAlteraciones(TreeMap<Ubicacion,List<Recibo>> tmA, TreeMap<Ubicacion, List<Recibo>> tm){
        TreeMap<Ubicacion,List<Alteracion>> tmr = new TreeMap<Ubicacion,List<Alteracion>>();
        Iterator<Ubicacion> it = tm.keySet().iterator();
        while(it.hasNext()){
            Ubicacion u = it.next();
            List<Recibo> rr = tm.get(u);
            List<Recibo> rra = tmA.get(u);

            for (int i = 0; i < rr.size(); i++) {
                Recibo get = rr.get(i);
                ContratoInquilino ci = get.getUtilitarioContratoInquilino();

                for (int j = 0; j < rra.size(); j++) {
                    Recibo get1 = rra.get(j);
                    ContratoInquilino ci2 = get1.getUtilitarioContratoInquilino();
                    if (ci.getId().compareTo(ci2.getId())==0){
                        if (Math.abs(get.getTotalReciboPostCCAA()-get1.getTotalReciboPostCCAA())>0.001){
                            //lo incluyo, en un proceso posterior analizare las diferencias
                            if (tmr.get(u)==null){
                                tmr.put(u, new ArrayList<Alteracion>());
                            }
                            Alteracion a = new Alteracion();
                            a.setRecibo(get);
                            a.setReciboAnterior(get1);
                            a.setResumen(devuelveDescripcionAlteracion(a));
                            tmr.get(u).add(a);
                        }
                    }
                }


            }
        }
        return tmr;
    }

    private String devuelveDescripcionAlteracion(Alteracion a){
        String resumen = "";
        Recibo r = a.getRecibo();
        List<ImplementacionConcepto> iiccr = AppBeans.get(RecibosService.class).getImplementacionesConceptosAgregadas(a.getRecibo());
        List<ImplementacionConcepto> iiccra = AppBeans.get(RecibosService.class).getImplementacionesConceptosAgregadas(a.getReciboAnterior());


        List<ImplementacionConcepto> altas = new ArrayList<ImplementacionConcepto>();
        List<ImplementacionConcepto> bajas = new ArrayList<ImplementacionConcepto>();
        Hashtable cambios = new Hashtable();

        a:
        for (int i = 0; i < iiccr.size(); i++) {
            ImplementacionConcepto get = iiccr.get(i);
            boolean hallado = false;
            for (int j = 0; j < iiccra.size(); j++) {
                ImplementacionConcepto get1 = iiccra.get(j);
                if (get.getOverrideConcepto().getId().compareTo(get1.getOverrideConcepto().getId())==0){
                    hallado = true;
                    if ((get.getImportePostCCAA() - get1.getImportePostCCAA())>0.001){
                        //altero el importe por la diferencia
                        get.setImportePostCCAA(get.getImportePostCCAA() - get1.getImportePostCCAA());
                        altas.add(get);
                        continue a;
                    }
                }
            }
            //no hayado, incorporo a altas
            if (!hallado){
                altas.add(get);
            }
        }

        b:
        for (int i = 0; i < iiccra.size(); i++) {
            ImplementacionConcepto get = iiccra.get(i);
            boolean hallado = false;
            for (int j = 0; j < iiccr.size(); j++) {
                ImplementacionConcepto get1 = iiccr.get(j);

                if (get.getOverrideConcepto().getId().compareTo(get1.getOverrideConcepto().getId())==0){
                    hallado = true;
                    if ((get1.getImportePostCCAA() - get.getImportePostCCAA())>0.001){
                        //altero el importe por la diferencia
                        get.setImportePostCCAA(get1.getImportePostCCAA() - get.getImportePostCCAA());
                        bajas.add(get);
                        continue b;
                    }
                }
            }
            //no hayado, incorporo a altas
            if (!hallado){
                bajas.add(get);
            }

        }

        //cambios
//        for (int i = 0; i < iiccr.size(); i++) {
//            ImplementacionConcepto get = iiccr.get(i);
//            for (int j = 0; j < iiccra.size(); j++) {
//                ImplementacionConcepto get1 = iiccra.get(j);
//                if (ArrayListUtils.comparaObjetos(get.getConcepto(), get1.getConcepto())){
//                    if (Math.abs(get.getImportePostCCAA() - get1.getImportePostCCAA())>0.001){
//                         cambios.put(get.getOverrideConcepto(), get.getImportePostCCAA() - get1.getImportePostCCAA());
//                    }
//                }
//            }
//
//        }
        DecimalFormat df = new DecimalFormat("#,##0.00");
        resumen = "Altas:";
        String infoSigno = "";
        for (int i = 0; i < altas.size(); i++) {
            ImplementacionConcepto get = altas.get(i);
//            if (get.getImportePostCCAA()>=0.0) infoSigno = "+";
//            else infoSigno = "-";
            if (i>0){
                resumen += ",";
            }
            resumen += get.getOverrideConcepto().getAbreviacion() + "(" + df.format(get.getImportePostCCAA()) + ")";
        }
        resumen += "Bajas:";
        for (int i = 0; i < bajas.size(); i++) {
            ImplementacionConcepto get = bajas.get(i);
//            if (get.getImportePostCCAA()>=0.0) infoSigno = "+";
//            else infoSigno = "-";
            if (i>0){
                resumen += ",";
            }
            resumen += get.getOverrideConcepto().getAbreviacion() + "(-" + df.format(get.getImportePostCCAA()) + ")";
        }
        Iterator<Concepto> cc = cambios.keySet().iterator();
        while(cc.hasNext()){
            Concepto c = cc.next();
            resumen += c.getAbreviacion() + "(" + df.format(cambios.get(c)) + ")";
        }
        return resumen;
    }

    /**
     * FIN FUNCIONES REPORT ANALISIS DE DIFERENCIAS
     */


    public byte[] produceReport(FlexReport fr, Hashtable baseEntrantes, List beans) throws Exception{
        try{


            JasperDesign designMaestro = JRXmlLoader.load(new ByteArrayInputStream(fr.getContenidoJrxml().getBytes()));
            JasperReport jr = JasperCompileManager.compileReport(designMaestro);

            byte[] bb;

            //lanzando report
            if (fr.getUserDataSourceConnection()){
                bb = JasperRunManager.runReportToPdf(jr, baseEntrantes, new SIJRBeanDataSource(beans));
            }else{

//                Connection conn = null;
//                Object[] ddss = (Object[]) C3P0Registry.getPooledDataSources().toArray();
//                for (int i = 0; i < ddss.length; i++) {
//                    PoolBackedDataSource dds = (PoolBackedDataSource) ddss[i];
//                    String url = dds.getConnection().getMetaData().getURL();
//                    if (url.indexOf("rentamaster2")!=-1){
//                        conn = dds.getConnection();
//                    }
//                }
//                bb = JasperRunManager.runReportToPdf(jr, baseEntrantes, conn);
//                conn.close();
                try{
                    //solución aparentemente limpia para asegurar que el Connection está abierto.


                    Connection conn = Rentamaster2DB.getConnection();
//                    bb = JasperRunManager.runReportToPdf(pamCarrier.getJasperReport(), pamCarrier.getParameters(), conn);
                    bb = JasperRunManager.runReportToPdf(jr, baseEntrantes, conn);

                }catch(Exception exc){

                    throw exc;
                }


            }

            return bb;
        }catch(Exception exc){
            exc.printStackTrace();
            throw new Exception("El report JRXML " + fr.getNombre() + " no compilo exitósamente: " + exc.getMessage());
        }
    }

    public byte[] produceReport(FlexReport fr, Hashtable baseEntrantes, List beans, Connection conn) throws Exception{
        try{


            JasperDesign designMaestro = JRXmlLoader.load(new ByteArrayInputStream(fr.getContenidoJrxml().getBytes()));
            JasperReport jr = JasperCompileManager.compileReport(designMaestro);

            byte[] bb;

            //lanzando report
            if (fr.getUserDataSourceConnection()){
                bb = JasperRunManager.runReportToPdf(jr, baseEntrantes, new SIJRBeanDataSource(beans));
            }else{

//                Connection conn = null;
//                Object[] ddss = (Object[]) C3P0Registry.getPooledDataSources().toArray();
//                for (int i = 0; i < ddss.length; i++) {
//                    PoolBackedDataSource dds = (PoolBackedDataSource) ddss[i];
//                    String url = dds.getConnection().getMetaData().getURL();
//                    if (url.indexOf("rentamaster2")!=-1){
//                        conn = dds.getConnection();
//                    }
//                }
//                bb = JasperRunManager.runReportToPdf(jr, baseEntrantes, conn);
//                conn.close();
                try{
                    //solución aparentemente limpia para asegurar que el Connection está abierto.



//                    bb = JasperRunManager.runReportToPdf(pamCarrier.getJasperReport(), pamCarrier.getParameters(), conn);
                    bb = JasperRunManager.runReportToPdf(jr, baseEntrantes, conn);

                }catch(Exception exc){

                    throw exc;
                }


            }

            return bb;
        }catch(Exception exc){
            exc.printStackTrace();
            throw new Exception("El report JRXML " + fr.getNombre() + " no compilo exitósamente: " + exc.getMessage());
        }
    }

    public byte[] devuelveReportPrevisualizacionRecibo(Recibo r) throws Exception{
        JRRenderable jrr = (JRRenderable) turnFileIntoJRRenderableObject("Recibo.svg");
        FlexReport fr = getFlexReportDesdeNombre("RECIBO PREVISUALIZACION");
        Hashtable ht = new Hashtable();
        HelperRecibo hr = new HelperRecibo(r);
        ht.put("FONDORECIBO", jrr);
        ht.put("reciboBean", hr);

        byte[] bb = produceReport(fr, ht, Arrays.asList(hr));
        return bb;
    }

    public byte[] devuelveReportInformeIVA(List<Departamento> deptos, Date fechaDesde, Date fechaHasta, Propietario prop, boolean imprimirTrimestre, boolean imprimirTotalesGlobal) throws Exception{
        List<Departamento> departamentosSeleccionados = deptos;
        Hashtable<UUID,String> htUbicacionesDepartamentosIds = new Hashtable<UUID, String>();
        List<Ubicacion> ubicaciones = new ArrayList<Ubicacion>();

        String todos_departamentos = "";
        //MJOntaje del list que servira como base para el datasource y
        //el hashtable que me contendra los departamentos ids en csv's por ubicacion para el query del dataset incluido
        //en el report
        for (int i = 0; i < departamentosSeleccionados.size(); i++) {
            Departamento dep = departamentosSeleccionados.get(i);
            Ubicacion ub = dep.getUbicacion();
//            ArrayListUtils.anadeSiNoPresenteEnLista(ubicaciones, ub);
            if (ubicaciones.indexOf(ub)==-1) ubicaciones.add(ub);

            String dids = htUbicacionesDepartamentosIds.get(ub.getId());
            if (dids==null){
                dids = "";
                htUbicacionesDepartamentosIds.put(ub.getId(), dids);
            }
            if (dids.trim().length()==0){
                dids += "." + dep.getId().toString() + ".";
            }else{
                dids += "," + "." + dep.getId().toString() + ".";
            }
            htUbicacionesDepartamentosIds.put(ub.getId(), dids);

            if (todos_departamentos.length()==0){
                todos_departamentos += "." + dep.getId().toString() + ".";
            }else{
                todos_departamentos += "," + "." + dep.getId().toString() + ".";
            }

        }

        Collections.sort(ubicaciones, new Comparator<Ubicacion>(){
            public int compare(Ubicacion u1, Ubicacion u2){
                return u1.getRm2id().compareTo(u2.getRm2id());
            }
        });
//        ubicaciones = ArrayListUtils.sortList(ubicaciones, new Ubicacion.ComparadorUbicacionesPorId());

        Date fechaInicial = fechaDesde;
        Date fechaFinal = fechaHasta;

        if ((fechaInicial==null)||(fechaFinal==null)){
            throw new Exception("Las fechas no pueden ser nulas");

        }

        if (fechaInicial.after(fechaFinal)==true){

            throw new Exception("Las fecha inicial no puede ser posterior a la fecha final");

        }

        Calendar calFechaInicial = Calendar.getInstance();
        calFechaInicial.setTime(fechaInicial);

        Calendar calFechaFinal = Calendar.getInstance();
        calFechaFinal.setTime(fechaFinal);

        if (calFechaInicial.get(Calendar.YEAR) != calFechaFinal.get(Calendar.YEAR)){
            throw new Exception("Las fechas han de ser del mismo ejercicio");

        }

        Propietario propiertario = prop;
        Persona persona = propiertario.getPersona();




        Calendar calPrimeroDeAnno = Calendar.getInstance();
        calPrimeroDeAnno.setTime(fechaFinal);
        calPrimeroDeAnno.set(calFechaInicial.get(Calendar.YEAR),0, 1);




//        JRRenderable jrr = new RentamasterDocsLoader(sl, sled).getJRRenderableDeRecursoEntornoPropietario(null, e.getId(), "LOGO");
        JRRenderable jrr = null;
        try{
            jrr = (JRRenderable) turnFileIntoJRRenderableObject("LogoGuadalest.svg");
        }catch(Exception exc){

        }


        Hashtable ht = new Hashtable();
        ht.put("ht_ubicaciones_departamentos", htUbicacionesDepartamentosIds);
        ht.put("fechaInicial", fechaInicial);
        ht.put("fechaFinal", fechaFinal);
        ht.put("propietario", persona.getNombreCompleto());
        if (jrr!=null){
            ht.put("P_IMAGEN", jrr);
        }
        ht.put("P_INFORME_REALIZADO_POR", "USUARIO LOGGEADO PENDIENTE");
        ht.put("P_FECHAHORA", new Date());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        int year = calFechaInicial.get(Calendar.YEAR);

        Date fechaInicioAnno = null;
        Date fechaFinAnno = null;
        try {
            fechaInicioAnno = sdf.parse("01/01/" + year);
            fechaFinAnno = sdf.parse("31/12/" + year);
        } catch (ParseException parseException) {
//            Notification.show("No se pudo realizar el report");
            throw new Exception("No se pudo realizar el report");
        }

        ht.put("fechaInicioAnno", fechaInicioAnno);
        ht.put("fechaFinAnno", fechaFinAnno);

        ht.put("todos_departamentos", todos_departamentos);


        ht.put("imprimirTrimestre", imprimirTrimestre);
        ht.put("imprimirTotalesGlobal", imprimirTotalesGlobal);


        FlexReport fr = null;
        byte[] bb = null;
        try {
            fr = getFlexReportDesdeNombre("INFORME IVA");
            //no lo puedo hacer por el productor normal porque este report combina un DataSource entrante y un SQL posterior
            //bb = Productor.produceReport(fr, ht, ubicaciones, SIApplication.getCurrent().getCurrentProcess().getSessionLayer(), SIApplication.getCurrent().getCurrentProcess().getSessionLayerExtDocs());
            String jrxml = fr.getContenidoJrxml();
            InputStream is = new ByteArrayInputStream(jrxml.getBytes());
            JasperDesign designMaestro = JRXmlLoader.load(is);
            JasperReport reportMaestro = JasperCompileManager.compileReport(designMaestro);



            SIJRBeanDataSource sijrbds = new SIJRBeanDataSource(ubicaciones);
            Hashtable ubsdeps = htUbicacionesDepartamentosIds;

            byte[] bytearr = JasperRunManager.runReportToPdf(reportMaestro, ht, sijrbds);

            return bytearr;

        } catch (Exception exception) {
            throw exception;
        }


    }

    public byte[] reportMod347(Propietario propietario, Date fechaDesde, Date fechaHasta) throws Exception{
        try {
            FlexReport fr = getFlexReportDesdeNombre("REPORT MOD 347");
            Hashtable ht = new Hashtable();

            if ((propietario==null) || (fechaDesde==null) || (fechaHasta==null)){
                throw new Exception("Datos Insuficientes");

            }else{
                if (fechaDesde.getTime()>fechaHasta.getTime()){
                    throw new Exception("Rango de fechas inválido");

                }
            }

            String uuid = propietario.getId().toString().replace("-", "");

            ht.put("propietarioId", uuid);
            ht.put("fechaDesde", fechaDesde);
            ht.put("fechaHasta", fechaHasta);
            byte[] bb = produceReport(fr, ht, new ArrayList(), Cubatest1DB.getConnection());
            return bb;
        } catch (Exception exception) {
            throw new Exception("No se pudo producir la previsualización del Mod. 347", exception);
        }
    }

    public byte[] reportListadoFianzas(List<Fianza> ff) throws Exception{
        return null;
    }

    public byte[] reportListadoAumentos(List<ContratoInquilino> ccii, Date fechaDesde, Date fechaHasta){
        ReportListadoAumentosIPC r = new ReportListadoAumentosIPC(ccii, fechaDesde, fechaHasta);
        byte[] bb = r.getReportAsByteArray();
        return bb;
    }

    public byte[] realizaImprimiblesContratoInquilino(Boolean[] bools, ContratoInquilino ci) throws Exception{

            byte[] bb = AsistenteImpresionContrato.realizaImpresion(bools, ci);
            return bb;

    }

    public byte[] realizaImpresionDocumentoFotografico(CarpetaDocumentosFotograficos cdf) throws Exception {
        List l_iiss = new ArrayList();

        cdf = AppBeans.get(DataManager.class).reload(cdf, "carpetaDocumentosFotograficos-view");
        if (cdf == null) {
            throw new Exception("El contratoInquilino no tiene carpeta de documentos fotográficos asociada.");
        }
        List<FotoThumbnail> ffth = cdf.getFotosThumbnail();
        List<FotoDocumentoFotografico> ffdf = cdf.getFotos();
        String pathMaestro = "ReportDocumentoFotografico.jrxml";

        Object  s = Resources.getResource("/com/company/test1/service/accessory/" + pathMaestro).getContent();
        JasperDesign designMaestro = JRXmlLoader.load((InputStream)s);



        try {
            JasperReport reportMaestro = JasperCompileManager.compileReport(designMaestro);
            Hashtable pamsReport = new Hashtable();

            for (int i = 0; i * 4 < ffdf.size(); i++) {

                pamsReport.clear();
//                AppBeans.get(JasperReportService.class).turnFileIntoJRRenderableObject("Logo.jpg");
//                JRRenderable jrrLogo = ReportingUtilities.obtenerJRRenderablePorEntornoOPropietario(c.getDepartamento().getPropietarioEfectivo(), SIApplication.getCurrent().getEntornosPreseleccionados(), "LOGO", SIApplication.getCurrent().getCurrentProcess().getSessionLayer(), SIApplication.getCurrent().getCurrentProcess().getSessionLayerExtDocs());
                JRRenderable jrrLogo = (JRRenderable) AppBeans.get(JasperReportService.class).turnFileIntoJRRenderableObject("LogoGuadalest.jpg");
                pamsReport.put("P_IMAGEN", jrrLogo);

                for (int j = 0; (j < 4) && (4*i+ j < ffdf.size()); j++) {
//                    FotoThumbnail fotoThumbnail = ffth.get(4*i+ j);
                    FotoDocumentoFotografico fdf = ffdf.get(4*i+ j);
//                    byte[] bb = fotoThumbnail.getRepresentacionSerial();
                    byte[] bb = fdf.getRepresentacionSerial();
                    if (bb==null){
                        FotosThumbnailExt fthext = AppBeans.get(ColeccionArchivosAdjuntosService.class).getFotoDocumentoFotograficoExt(fdf);
                        bb = fthext.getRepresentacionSerial();
                    }
//                    BufferedImage bim = javax.imageio.ImageIO.read(new ByteArrayInputStream(fotoThumbnail.getRepresentacionSerialExtDoc(SIApplication.getCurrent().getCurrentProcess().getSessionLayerExtDocs())));
//                    bim = ImageUtils.ensureImageInPortraitMode(bim);
                    JRRenderable jrr = JRImageRenderer.getInstance(bb);
                    pamsReport.put("IMAGEN_" + (j+1), jrr);
                }

                byte[] bytearr = JasperRunManager.runReportToPdf(reportMaestro, pamsReport, new SIJRBeanDataSource(Arrays.asList("")));
                ByteArrayInputStream bais = new ByteArrayInputStream(bytearr);
                l_iiss.add(bais);
            }

            //concatenacion
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfUtils.concatPdfs(l_iiss, baos, true);
            return baos.toByteArray();

        } catch (Exception exc) {
            throw new Exception("Falló al realizar la impresión de Documentos Fotográficos: " + exc.getMessage());
        }
    }

    public byte[] realizaImpresionAnexo(Anexo a) throws Exception{
        List<ParametroValorAnexo> ppvv = a.getParametrosValores();
        List<HelperInyeccionPlantilla> iipp = new ArrayList();
        for (int i = 0; i < ppvv.size(); i++) {
            ParametroValorAnexo get = ppvv.get(i);
            HelperInyeccionPlantilla hi = new HelperInyeccionPlantilla();
            hi.setTitulo(get.getNombreParametro());
            hi.setValor(get.getValor());
            iipp.add(hi);
        }
        try {
            List<ContratoInquilino> l = Arrays.asList(new ContratoInquilino[]{a.getContratoInquilino()});
            byte[] pdf = realizaPrevisualizacionesNotificacionesContratos(a.getContenido(), l, iipp);
            return pdf;
        } catch (Exception exception) {
            throw new Exception("No se pudo completar la impresion del Anexo");
        }
    }

    private byte[] realizaPrevisualizacionesNotificacionesContratos(String contenido, List<ContratoInquilino> cc, List<HelperInyeccionPlantilla> hhii)
            throws Exception{


        if (cc.isEmpty()) return null;




        Hashtable objetosNotificacion = new Hashtable();

        Hashtable hti = new Hashtable();
        for (int i = 0; i < hhii.size(); i++) {
            HelperInyeccionPlantilla helperInyeccion = hhii.get(i);
            objetosNotificacion.put(helperInyeccion.getTitulo(), helperInyeccion.getValor());
        }
        List<InputStream> inputStreams = new ArrayList<InputStream>();
        for (int i = 0; i < cc.size(); i++) {
            ContratoInquilino c = cc.get(i);

            objetosNotificacion.put("contr", c);
            objetosNotificacion.put("admin", c.getDepartamento().getPropietarioEfectivo().getPersona());
            objetosNotificacion.put("prop", c.getDepartamento().getPropietarioEfectivo().getPersona());
            objetosNotificacion.put("diradmin", c.getDepartamento().getPropietarioEfectivo().getPersona().direccionDesdeNombre(NombreTipoDireccion.DOMICILIO_ADMINISTRADOR.getId()));
            objetosNotificacion.put("dirinqui",c.getInquilino().direccionDesdeNombre(NombreTipoDireccion.DOMICILIO_INQUILINO.getId()));
            objetosNotificacion.put("inqui",c.getInquilino());

            String contenidoImplmentado = notificacionService.implementaContenido(contenido, objetosNotificacion, true);


//            ArchivoAdjunto aacabecera = null;
//            if (nc instanceof NotificacionContratoInquilino){
//                entornoParaCabecera = ((NotificacionContratoInquilino)nc).getContratoInquilino().getDepartamento().getEntornos().get(0);
//                RecursoEntornoPropietario reCabecera = RecursoEntornoPropietario.obtenerRecursoEntornoPropietario(entornoParaCabecera, RecursoEntornoPropietario.NCABECERA_DOCS);
//
//                if (reCabecera != null){
//                    aacabecera = reCabecera.getArchivoAdjunto();
//                }
//            }
//            ArchivoAdjunto aaestampa = SIApplication.getCurrent().getUsuarioActivo().getPersona().getImagenFirma();
//
//            if (!nc.getImplementado()){
//
//            }

            byte[] bb = notificacionService.implementaVersionPdfVersionFlexReport(contenidoImplmentado);


            ByteArrayInputStream bais = new ByteArrayInputStream(bb);
            inputStreams.add(bais);

        }

        ByteArrayOutputStream concatenated = new ByteArrayOutputStream();
        PdfUtils.concatPdfs(inputStreams, concatenated, false);
        byte[] bb = concatenated.toByteArray();
        return bb;
    }

    private byte[] realizaPrevisualizacionesNotificacionesContratos(Plantilla p, List<ContratoInquilino> cc, List<HelperInyeccionPlantilla> hhii)
            throws Exception{


        if (cc.isEmpty()) return null;



        if (p==null) return null;
        Hashtable objetosNotificacion = new Hashtable();

        Hashtable hti = new Hashtable();
        for (int i = 0; i < hhii.size(); i++) {
            HelperInyeccionPlantilla helperInyeccion = hhii.get(i);
            objetosNotificacion.put(helperInyeccion.getTitulo(), helperInyeccion.getValor());
        }
        List<InputStream> inputStreams = new ArrayList<InputStream>();
        for (int i = 0; i < cc.size(); i++) {
            ContratoInquilino c = cc.get(i);

            objetosNotificacion.put("contr", c);
            objetosNotificacion.put("admin", c.getDepartamento().getPropietarioEfectivo().getPersona());
            objetosNotificacion.put("prop", c.getDepartamento().getPropietarioEfectivo().getPersona());
            objetosNotificacion.put("diradmin", c.getDepartamento().getPropietarioEfectivo().getPersona().direccionDesdeNombre(NombreTipoDireccion.DOMICILIO_ADMINISTRADOR.getId()));
            objetosNotificacion.put("dirinqui",c.getInquilino().direccionDesdeNombre(NombreTipoDireccion.DOMICILIO_INQUILINO.getId()));
            objetosNotificacion.put("inqui",c.getInquilino());
            NotificacionContratoInquilino nc = new NotificacionContratoInquilino();
            nc.setPlantilla(p);
            nc.setContratoInquilino(c);
            nc  = (NotificacionContratoInquilino) notificacionService.implementaContenido(nc, objetosNotificacion, true);


//            ArchivoAdjunto aacabecera = null;
//            if (nc instanceof NotificacionContratoInquilino){
//                entornoParaCabecera = ((NotificacionContratoInquilino)nc).getContratoInquilino().getDepartamento().getEntornos().get(0);
//                RecursoEntornoPropietario reCabecera = RecursoEntornoPropietario.obtenerRecursoEntornoPropietario(entornoParaCabecera, RecursoEntornoPropietario.NCABECERA_DOCS);
//
//                if (reCabecera != null){
//                    aacabecera = reCabecera.getArchivoAdjunto();
//                }
//            }
//            ArchivoAdjunto aaestampa = SIApplication.getCurrent().getUsuarioActivo().getPersona().getImagenFirma();
//
//            if (!nc.getImplementado()){
//
//            }

            notificacionService.implementaVersionPdfVersionFlexReport(nc);

            byte[] bb = nc.getVersionPdf();
            ByteArrayInputStream bais = new ByteArrayInputStream(bb);
            inputStreams.add(bais);

        }

        ByteArrayOutputStream concatenated = new ByteArrayOutputStream();
        PdfUtils.concatPdfs(inputStreams, concatenated, false);
        byte[] bb = concatenated.toByteArray();
        return bb;
    }

//    private byte[] realizaPrevisualizacionesNotificacionContenidoManual(String s, List<ContratoInquilino> cc, List<HelperInyeccionPlantilla> hhii)
//            throws Exception{
//
//
//        if (cc.isEmpty()) return null;
//
//
//
//
//        Hashtable objetosNotificacion = new Hashtable();
//
//        Hashtable hti = new Hashtable();
//        for (int i = 0; i < hhii.size(); i++) {
//            HelperInyeccionPlantilla helperInyeccion = hhii.get(i);
//            objetosNotificacion.put(helperInyeccion.getTitulo(), helperInyeccion.getValor());
//        }
//        List<InputStream> inputStreams = new ArrayList<InputStream>();
//        for (int i = 0; i < cc.size(); i++) {
//            ContratoInquilino c = cc.get(i);
//
//            objetosNotificacion.put("contr", c);
//            objetosNotificacion.put("admin", c.getDepartamento().getPropietarioEfectivo().getPersona());
//            objetosNotificacion.put("prop", c.getDepartamento().getPropietarioEfectivo().getPersona());
//            objetosNotificacion.put("diradmin", c.getDepartamento().getPropietarioEfectivo().getPersona().direccionDesdeNombre(NombreTipoDireccion.DOMICILIO_ADMINISTRADOR.getId()));
//            objetosNotificacion.put("dirinqui",c.getInquilino().direccionDesdeNombre(NombreTipoDireccion.DOMICILIO_INQUILINO.getId()));
//            objetosNotificacion.put("inqui",c.getInquilino());
//            NotificacionContratoInquilino nc = new NotificacionContratoInquilino();
//
//            nc.setContratoInquilino(c);
//            nc  = (NotificacionContratoInquilino) notificacionService.implementaContenido(nc, objetosNotificacion, true);
//
//
////            ArchivoAdjunto aacabecera = null;
////            if (nc instanceof NotificacionContratoInquilino){
////                entornoParaCabecera = ((NotificacionContratoInquilino)nc).getContratoInquilino().getDepartamento().getEntornos().get(0);
////                RecursoEntornoPropietario reCabecera = RecursoEntornoPropietario.obtenerRecursoEntornoPropietario(entornoParaCabecera, RecursoEntornoPropietario.NCABECERA_DOCS);
////
////                if (reCabecera != null){
////                    aacabecera = reCabecera.getArchivoAdjunto();
////                }
////            }
////            ArchivoAdjunto aaestampa = SIApplication.getCurrent().getUsuarioActivo().getPersona().getImagenFirma();
////
////            if (!nc.getImplementado()){
////
////            }
//
//            notificacionService.implementaVersionPdfVersionFlexReport(nc);
//
//            byte[] bb = nc.getVersionPdf();
//            ByteArrayInputStream bais = new ByteArrayInputStream(bb);
//            inputStreams.add(bais);
//
//        }
//
//        ByteArrayOutputStream concatenated = new ByteArrayOutputStream();
//        PdfUtils.concatPdfs(inputStreams, concatenated, false);
//        byte[] bb = concatenated.toByteArray();
//        return bb;
//    }

    public byte[] realizaInformeIva(Date fechaDesde, Date fechaHasta, Propietario propietario, List<Departamento> departamentos, boolean anadirInfoTrimestral, boolean anadirInfoGlobal) throws Exception{
        List<Departamento> departamentosSeleccionados = departamentos;

//        List<Departamento> departamentosSeleccionados = tud.getElementosSeleccionadosDelTipo(Departamento.class);
        Hashtable<String,String> htUbicacionesDepartamentosIds = new Hashtable<String, String>();
        List<Ubicacion> ubicaciones = new ArrayList<Ubicacion>();
        String todos_departamentos = "";
        //MJOntaje del list que servira como base para el datasource y
        //el hashtable que me contendra los departamentos ids en csv's por ubicacion para el query del dataset incluido
        //en el report

        for (int i = 0; i < departamentosSeleccionados.size(); i++) {
            Departamento dep = departamentosSeleccionados.get(i);
            Ubicacion ub = dep.getUbicacion();
            if (ubicaciones.indexOf(ub) == -1) {
                ubicaciones.add(ub);
            }
//            ArrayListUtils.anadeSiNoPresenteEnLista(ubicaciones, ub);
            String dids = htUbicacionesDepartamentosIds.get(ub.getId().toString().replace("-",""));
            if (dids == null) {
                dids = new String("");
                htUbicacionesDepartamentosIds.put(ub.getId().toString().replace("-",""), dids);
            }
            if (dids.trim().length() == 0) {
                dids += "." + dep.getId().toString().replace("-","") + ".";
            } else {
                dids += "," + "." + dep.getId().toString().replace("-","") + ".";
            }
            htUbicacionesDepartamentosIds.put(ub.getId().toString().replace("-",""), dids);
            if (todos_departamentos.length() == 0) {
                todos_departamentos += "." + dep.getId().toString().replace("-","") + ".";
            } else {
                todos_departamentos += "," + "." + dep.getId().toString().replace("-","") + ".";
            }
        }

//        ubicaciones = ArrayListUtils.sortList(ubicaciones, new Ubicacion.ComparadorUbicacionesPorId());
            Collections.sort(ubicaciones, Ubicacion.comparatorRm2id);
            java.util.Date fechaInicial = fechaDesde;
            java.util.Date fechaFinal = fechaHasta;

            if ((fechaInicial==null)||(fechaFinal==null)){
                throw new Exception("Ninguna de las fechas puede ser nula");
            }

            if (fechaInicial.after(fechaFinal)==true){

                throw new Exception("Las fecha inicial no puede ser posterior a la fecha final");
            }

            Calendar calFechaInicial = Calendar.getInstance();
            calFechaInicial.setTime(fechaInicial);

            Calendar calFechaFinal = Calendar.getInstance();
            calFechaFinal.setTime(fechaFinal);
            if (calFechaInicial.get(Calendar.YEAR) != calFechaFinal.get(Calendar.YEAR)){

                throw new Exception("Las fechas han de ser del mismo año");
            }


            Persona persona = propietario.getPersona();

            Calendar calPrimeroDeAnno = Calendar.getInstance();
            calPrimeroDeAnno.setTime(fechaFinal);
            calPrimeroDeAnno.set(calFechaInicial.get(Calendar.YEAR),0, 1);

            JRRenderable jrr = (JRRenderable) turnFileIntoJRRenderableObject("LogoGuadalest.jpg");
//        JRRenderable jrr = new RentamasterDocsLoader(sl, sled).getJRRenderableDeRecursoEntornoPropietario(null, e.getId(), "LOGO");
            Hashtable ht = new Hashtable();
            ht.put("ht_ubicaciones_departamentos", htUbicacionesDepartamentosIds);
            ht.put("fechaInicial", fechaInicial);
            ht.put("fechaFinal", fechaFinal);
//        ht.put("SL",SIApplication.getCurrent().getCurrentProcess().getSessionLayer());
//        ht.put("SLED",SIApplication.getCurrent().getCurrentProcess().getSessionLayerExtDocs());
            ht.put("propietario", persona.getNombreCompleto());
            if (jrr!=null){
                ht.put("P_IMAGEN", jrr);
            }
            ht.put("P_INFORME_REALIZADO_POR", "Pendiente");
            ht.put("P_FECHAHORA", new java.util.Date());

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            int year = calFechaInicial.get(Calendar.YEAR);

            java.util.Date fechaInicioAnno = null;
            java.util.Date fechaFinAnno = null;
            try {
                fechaInicioAnno = sdf.parse("01/01/" + year);
                fechaFinAnno = sdf.parse("31/12/" + year);
            } catch (ParseException parseException) {

                throw new Exception("No se pudo realizar el report", parseException);
            }

            ht.put("fechaInicioAnno", fechaInicioAnno);
            ht.put("fechaFinAnno", fechaFinAnno);

            ht.put("todos_departamentos", todos_departamentos);


            boolean imprimirTrimestre = anadirInfoTrimestral;
            boolean imprimirTotalesGlobal = anadirInfoGlobal;

            ht.put("imprimirTrimestre", imprimirTrimestre);
            ht.put("imprimirTotalesGlobal", imprimirTotalesGlobal);

            ht.put("dbconnection", Cubatest1DB.getConnection());

            FlexReport fr = null;
            byte[] bb = null;
            try {
                fr = getFlexReportDesdeNombre("INFORME IVA");
                //no lo puedo hacer por el productor normal porque este report combina un DataSource entrante y un SQL posterior
                //bb = Productor.produceReport(fr, ht, ubicaciones, SIApplication.getCurrent().getCurrentProcess().getSessionLayer(), SIApplication.getCurrent().getCurrentProcess().getSessionLayerExtDocs());
                String jrxml = fr.getContenidoJrxml();
                InputStream is = new ByteArrayInputStream(jrxml.getBytes());
                JasperDesign designMaestro = JRXmlLoader.load(is);
                JasperReport reportMaestro = JasperCompileManager.compileReport(designMaestro);

                //quitando los dashes de los ids de las ubicaciones
                ArrayList newubicaciones = new ArrayList();
                for (int i = 0; i < ubicaciones.size(); i++) {
                    Ubicacion u = ubicaciones.get(i);
                    HlpUbicacionInformeIva hlp = new HlpUbicacionInformeIva(u);
                    newubicaciones.add(hlp);
                }

                SIJRBeanDataSource sijrbds = new SIJRBeanDataSource(newubicaciones);
                Hashtable ubsdeps = htUbicacionesDepartamentosIds;

                byte[] bytearr = JasperRunManager.runReportToPdf(reportMaestro, ht, sijrbds);

                return bytearr;
            } catch (Exception exception) {
                throw exception;
            }

    }

    public byte[] realizaReportRealizacionPago(RealizacionPago rp) throws Exception{
        ReportRealizacionPago rrp = new ReportRealizacionPago(rp);
        return rrp.getReportAsByteArray();
    }


}