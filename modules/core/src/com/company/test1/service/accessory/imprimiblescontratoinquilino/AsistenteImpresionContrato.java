package com.company.test1.service.accessory.imprimiblescontratoinquilino;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */





import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.*;

import com.company.test1.StringUtils;
import com.company.test1.entity.ArchivoAdjunto;
import com.company.test1.entity.ArchivoAdjuntoExt;
import com.company.test1.entity.CuentaBancaria;
import com.company.test1.entity.FotosThumbnailExt;
import com.company.test1.entity.contratosinquilinos.ContratoInquilino;
import com.company.test1.entity.contratosinquilinos.LiquidacionExtincion;
import com.company.test1.entity.contratosinquilinos.LiquidacionSuscripcion;
import com.company.test1.entity.departamentos.CedulaHabitabilidad;
import com.company.test1.entity.departamentos.CertificadoCalificacionEnergetica;
import com.company.test1.entity.departamentos.Departamento;
import com.company.test1.entity.departamentos.Ubicacion;
import com.company.test1.entity.documentosfotograficos.CarpetaDocumentosFotograficos;
import com.company.test1.entity.documentosfotograficos.FotoDocumentoFotografico;
import com.company.test1.entity.documentosfotograficos.FotoThumbnail;
import com.company.test1.entity.enums.UsoContratoEnum;

import com.company.test1.entity.reportsyplantillas.FlexReport;
import com.company.test1.service.ColeccionArchivosAdjuntosService;
import com.company.test1.service.JasperReportService;
import com.company.test1.service.JasperReportServiceBean;
import com.company.test1.service.accessory.PdfUtils;
import com.company.test1.service.accessory.SIJRBeanDataSource;
import com.google.common.io.Resources;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.base.JRVirtualPrintPage;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.apache.commons.lang.time.DateUtils;


/**
 *
 * @author Carlos Conti
 */
public class AsistenteImpresionContrato {

    static String pathReports = "/jasperreports/contratosinquilinos/";

    public static final int CONTRATO = 0;
    public static final int LIQ_INICIAL = 1;
    public static final int CED_HABITABILIDAD = 2;
    public static final int LIQ_FINAL = 3;
    public static final int FORM_DOMICILIACION_BANCARIA = 4;
    public static final int FORM_ENTREGA_LLAVES = 5;
    public static final int DOCTO_FOTOGRAFICO = 6;
    public static final int CERTIFICADO_CALIFICACION_ENERGETICA = 7;

    public static byte[] realizaImpresion(Boolean[] bols, ContratoInquilino ci) throws Exception{
        List l_ii = new ArrayList();
        if (bols[CONTRATO]){
            byte[] bb = realizaImpresionCaratulaYClausulasContrato(ci);
            if (bb!=null){
                l_ii.add(new ByteArrayInputStream(bb));
            }
        }
        if (bols[LIQ_INICIAL]){
            byte[] bb = realizaImpresionLiquidacionInicial(ci);
            if (bb!=null){
                l_ii.add(new ByteArrayInputStream(bb));
            }
        }
        if (bols[CED_HABITABILIDAD]){
            byte[] bb = realizaImpresionCedulaHabitabilidad(ci);
            if (bb!=null){
                l_ii.add(new ByteArrayInputStream(bb));
            }
        }
        if (bols[LIQ_FINAL]){
            byte[] bb = realizaImpresionLiquidacionExtincion(ci);
            if (bb!=null){
                l_ii.add(new ByteArrayInputStream(bb));
            }
        }
        if (bols[FORM_DOMICILIACION_BANCARIA]){
            byte[] bb = realizaImpresionFormDomiciliacionBancaria(ci,true);
            if (bb!=null){
                l_ii.add(new ByteArrayInputStream(bb));
            }
        }
        if (bols[FORM_ENTREGA_LLAVES]){
            byte[] bb = realizaImpresionDetalleEntregaLlaves(ci);
            if (bb!=null){
                l_ii.add(new ByteArrayInputStream(bb));
            }
        }
        if (bols[DOCTO_FOTOGRAFICO]){
            byte[] bb = realizaImpresionDocumentoFotografico(ci);
            if (bb!=null){
                l_ii.add(new ByteArrayInputStream(bb));
            }
        }
        if (bols[CERTIFICADO_CALIFICACION_ENERGETICA]) {
            byte[] bb = realizaImpresionCertificadoCalificacionEnergetica(ci);
            if (bb != null) {
                l_ii.add(new ByteArrayInputStream(bb));
            }
        }

        if (!l_ii.isEmpty()){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfUtils.concatPdfs(l_ii, baos, true);
            byte[] bb = baos.toByteArray();
            return bb;
        }
        return null;
    }

    /*
     * Caso especial para imprimir desde Domiciliazación Bancaria, esta función existe solo para no hacer pública ninguna de las
     * funciones de aquí
     */

    public static byte[] realizaImpresionFormCambioBancaria(ContratoInquilino ci, boolean escribirCuentaBancaria) throws Exception{
        return realizaImpresionFormDomiciliacionBancaria(ci,escribirCuentaBancaria);
    }

    private static byte[] realizaImpresionCaratulaYClausulasContrato(ContratoInquilino contratoInquilino) throws Exception{
        List<InputStream> l_iiss = new ArrayList();

        try {
            CaratulaContratoArrendamiento cca;
            byte[] bb_caratula;
            if (contratoInquilino.getUsoContrato() == UsoContratoEnum.VIVIENDA) {
                //el requerimiento para uqe sea contrato de temporada ya no es la duracion del contrato
                //sino si se marca el uso como temporada
                //long diff = contratoInquilino.getFechaVencimientoPrevisto().getTime() - contratoInquilino.getFechaOcupacion().getTime();

                //to days
                //diff = diff/(1000*60*60*24);
                //if (diff >365.0){
                /*if (true){

                }else{
                    cca = new CaratulaContratoArrendamientoVivienda(contratoInquilino, true);
                }*/
                cca = new CaratulaContratoArrendamientoVivienda(contratoInquilino);
                bb_caratula = ((CaratulaContratoArrendamientoVivienda) cca).getReportAsByteArray();
            } else if (contratoInquilino.getUsoContrato() == UsoContratoEnum.TEMPORADA){
                cca = new CaratulaContratoArrendamientoVivienda(contratoInquilino, true);
                bb_caratula = ((CaratulaContratoArrendamientoVivienda) cca).getReportAsByteArray();
            } else {
                cca = new CaratulaContratoArrendamientoLocalComercial(contratoInquilino);
                bb_caratula = ((CaratulaContratoArrendamientoLocalComercial) cca).getReportAsByteArray();
            }
            ByteArrayInputStream baisCaratula = new ByteArrayInputStream(bb_caratula);
            l_iiss.add(baisCaratula);

            if (contratoInquilino.getImplementacionModelo() != null) {
                ReportBorradorClausulasContrato rbcc = new ReportBorradorClausulasContrato(contratoInquilino);
                byte[] bb_clausulas = rbcc.getReportAsByteArray();
                ByteArrayInputStream baisClausular = new ByteArrayInputStream(bb_clausulas);
                l_iiss.add(baisClausular);
            }

            ByteArrayOutputStream baosReport = new ByteArrayOutputStream();
            PdfUtils.concatPdfs(l_iiss, baosReport, true);

            return baosReport.toByteArray();
        } catch (Exception exc) {
            throw new Exception("Falló al realizar la impresión de la Carátula y Cláusulas: " + exc.getMessage());
        }

    }

    private static byte[] realizaImpresionLiquidacionInicial(ContratoInquilino contratoInquilino) throws Exception{

        try {

            LiquidacionSuscripcion li = contratoInquilino.getLiquidacionSuscripcion();
            if (li == null) {
                throw new Exception("El contratoInquilino no tiene Liquidación Inicial asociada.");
            }

            String pathMaestro = "ReportLiquidacionInicio.jrxml";
            if (li.getEsRenovacion()) {
                pathMaestro = "ReportLiquidacionInicioConRenovacion.jrxml";
            }
            String s = AppBeans.get(JasperReportService.class).getExtFileContent(pathMaestro);
            JasperDesign designMaestro = JRXmlLoader.load(new ByteArrayInputStream(s.getBytes()));
            JasperReport reportMaestro = JasperCompileManager.compileReport(designMaestro);

            Ubicacion u = contratoInquilino.getDepartamento().getUbicacion();
            Departamento d = contratoInquilino.getDepartamento();
            String titulo = "LIQUIDACIÓN CONTRATO " + contratoInquilino.getNumeroContrato() + " " + u.getNombre() + " " + d.getPisoPuerta();
            String ciudadFecha = "Barcelona a " + StringUtils.dateToString(contratoInquilino.getFechaRealizacion());

//            JRRenderable jrr = ReportingUtilities.obtenerJRRenderablePorEntornoOPropietario(contratoInquilino.getDepartamento().getPropietarioEfectivo(), SIApplication.getCurrent().getEntornosPreseleccionados(), "LOGO", SIApplication.getCurrent().getCurrentProcess().getSessionLayer(), SIApplication.getCurrent().getCurrentProcess().getSessionLayerExtDocs());
            JRRenderable jrr = (JRRenderable) AppBeans.get(JasperReportService.class).turnFileIntoJRRenderableObject("LogoGuadalest.jpg");

            String nifPropietario = contratoInquilino.getDepartamento().getPropietarioEfectivo().getPersona().getNifDni();
            if (nifPropietario.compareTo("B75537878")==0){
                jrr = (JRRenderable) AppBeans.get(JasperReportService.class).turnFileIntoJRRenderableObject("cabecera_documentos_grupo_domus.png");
            }

            ArrayList al = new ArrayList();
            al.add(new HelperLiquidacionInicial(li));
            SIJRBeanDataSource sijr1 = new SIJRBeanDataSource(al);

            Hashtable pams = new Hashtable();
            pams.put("TITULO_REPORT", titulo);
            pams.put("CIUDAD_FECHA", ciudadFecha);
            pams.put("P_IMAGEN", jrr);

            byte[] bytearr = JasperRunManager.runReportToPdf(reportMaestro, pams, sijr1);
            return bytearr;

        } catch (Exception exc) {
            throw new Exception("Falló al realizar la impresión de la Liquidación Inicial: " + exc.getMessage());
        }
    }

    private static byte[] realizaImpresionLiquidacionExtincion(ContratoInquilino contratoInquilino) throws Exception{

        try {
            LiquidacionExtincion le = contratoInquilino.getLiquidacionExtincion();
            if (le == null) {
                throw new Exception("El contratoInquilino no tiene Liquidación Extinción asociada.");
            }
            String pathMaestro = "ReportLiquidacionExtincion.jrxml";

            Ubicacion u = contratoInquilino.getDepartamento().getUbicacion();
            Departamento d = contratoInquilino.getDepartamento();
            String titulo = "LIQUIDACION CONTRATO " + contratoInquilino.getNumeroContrato() + " " + u.getNombre() + " " + d.getPisoPuerta();

            JRRenderable jrr = null;
            try{
//                jrr = ReportingUtilities.obtenerJRRenderablePorEntornoOPropietario(contratoInquilino.getDepartamento().getPropietarioEfectivo(), SIApplication.getCurrent().getEntornosPreseleccionados(), "LOGO", SIApplication.getCurrent().getCurrentProcess().getSessionLayer(), SIApplication.getCurrent().getCurrentProcess().getSessionLayerExtDocs());
                jrr = (JRRenderable)AppBeans.get(JasperReportService.class).turnFileIntoJRRenderableObject("LogoGuadalest.jpg");
            }catch(Exception exc){
                jrr = null;
            }

            String ciudadFecha = "Barcelona a " + StringUtils.dateToString(contratoInquilino.getLiquidacionExtincion().getFechaLiquidacion());

            ArrayList al = new ArrayList();
            al.add(le);
            SIJRBeanDataSource sijr1 = new SIJRBeanDataSource(al);

            Hashtable pams = new Hashtable();
            pams.put("TITULO_REPORT", titulo);
            pams.put("CIUDAD_FECHA", ciudadFecha);
            pams.put("NF", new DecimalFormat("#,##0.00"));
            if (jrr!=null){
                pams.put("P_IMAGEN", jrr);
            }
            //guardado provisional durante la transaccion para
            //que la consulta a base de datos de datos actualizados

            //OJO A ESTA LINEA DE CODIGO PARA LA MIGRACION, NO SE SI ES RELEVANTE
//            sl.persistInTransaction(le);

//            pams.put("liquidacionExtincionId", le.getId());

//            FlexReport fr = AppBeans.get(JasperReportService.class).getFlexReportDesdeNombre("LIQUIDACION_EXTINCION_CONTRATO_INQUILINO");
//            FlexReport fr = FlexReport.getFlexReportPorNombre("LIQUIDACION_EXTINCION_CONTRATO_INQUILINO", sl);

            Object  s = Resources.getResource("/com/company/test1/service/accessory/" + pathMaestro).getContent();
            JasperDesign designMaestro = JRXmlLoader.load((InputStream)s);
            JasperReport reportMaestro = JasperCompileManager.compileReport(designMaestro);

            byte[] bytearr = JasperRunManager.runReportToPdf(reportMaestro, pams, sijr1);
            return bytearr;

        } catch (Exception exc) {
            throw new Exception("Falló al realizar la impresión de la Liquidación Extinción: " + exc.getMessage());
        }
    }

    private static byte[] realizaImpresionDocumentoFotografico(ContratoInquilino c) throws Exception {
        List l_iiss = new ArrayList();
        CarpetaDocumentosFotograficos cdf = c.getCarpetaDocumentoFotograficoFirma();
        cdf = AppBeans.get(DataManager.class).reload(cdf, "carpetaDocumentosFotograficos-view");
        if (cdf == null) {
            throw new Exception("El contratoInquilino no tiene carpeta de documentos fotográficos asociada.");
        }
        List<FotoThumbnail> ffth = cdf.getFotosThumbnail();
        List<FotoDocumentoFotografico> ffdf = cdf.getFotos();


        String s = AppBeans.get(JasperReportService.class).getExtFileContent("ReportDocumentoFotografico.jrxml");
        JasperDesign designMaestro = JRXmlLoader.load(new ByteArrayInputStream(s.getBytes()));



        try {
            JasperReport reportMaestro = JasperCompileManager.compileReport(designMaestro);
            Hashtable pamsReport = new Hashtable();

            for (int i = 0; i * 4 < ffdf.size(); i++) {

                pamsReport.clear();
//                AppBeans.get(JasperReportService.class).turnFileIntoJRRenderableObject("Logo.jpg");
//                JRRenderable jrrLogo = ReportingUtilities.obtenerJRRenderablePorEntornoOPropietario(c.getDepartamento().getPropietarioEfectivo(), SIApplication.getCurrent().getEntornosPreseleccionados(), "LOGO", SIApplication.getCurrent().getCurrentProcess().getSessionLayer(), SIApplication.getCurrent().getCurrentProcess().getSessionLayerExtDocs());
                JRRenderable jrrLogo = (JRRenderable) AppBeans.get(JasperReportService.class).turnFileIntoJRRenderableObject("LogoGuadalest.jpg");

                String nifPropietario = c.getDepartamento().getPropietarioEfectivo().getPersona().getNifDni();
                if (nifPropietario.compareTo("B75537878")==0){
                    jrrLogo = (JRRenderable) AppBeans.get(JasperReportService.class).turnFileIntoJRRenderableObject("cabecera_documentos_grupo_domus.png");
                }

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



    private static byte[] realizaImpresionDetalleEntregaLlaves(ContratoInquilino c) throws Exception{

        try{
            String s = AppBeans.get(JasperReportService.class).getExtFileContent("ReportDetalleEntregaLlaves.jrxml");
            JasperDesign designMaestro = JRXmlLoader.load(new ByteArrayInputStream(s.getBytes()));

            JasperReport reportMaestro = JasperCompileManager.compileReport(designMaestro);
            ArrayList al = new ArrayList();al.add(new Object());
            SIJRBeanDataSource sijr1 = new SIJRBeanDataSource(al);

//            JRRenderable jrr = ReportingUtilities.obtenerJRRenderablePorEntornoOPropietario(c.getDepartamento().getPropietarioEfectivo(), SIApplication.getCurrent().getEntornosPreseleccionados(), "LOGO", SIApplication.getCurrent().getCurrentProcess().getSessionLayer(), SIApplication.getCurrent().getCurrentProcess().getSessionLayerExtDocs());

            String logoFileName = "LogoGuadalest.jpg";
            String nifPropietario = c.getDepartamento().getPropietarioEfectivo().getPersona().getNifDni();
            if (nifPropietario.compareTo("B75537878")==0){
                logoFileName = "cabecera_documentos_grupo_domus.png";
            }
            JRRenderable jrr = (JRRenderable) AppBeans.get(JasperReportService.class).turnFileIntoJRRenderableObject(logoFileName);

            Hashtable pams = new Hashtable();

            String ciudadFecha = "Barcelona a " + StringUtils.dateToString(c.getFechaRealizacion());
            pams.put("CIUDAD_FECHA", ciudadFecha);
            pams.put("NUM_CONTRATO", c.getNumeroContrato());
            pams.put("DESCRIPCION_DEPARTAMENTO", c.getDepartamento().getNombreDescriptivoCompleto());
            pams.put("DETALLE_ENTREGA_LLAVES", c.getDetalleEntregaLlaves());
            pams.put("NOMBRE_INQUILINO", c.getInquilino().getNombreCompleto());
            pams.put("P_IMAGEN", jrr);

            byte[] bytearr = JasperRunManager.runReportToPdf(reportMaestro, pams,sijr1);
            return bytearr;

        }catch(Exception exc){
            throw new Exception("Falló al realizar la impresión de Entrega de llaves: " + exc.getMessage());
        }
    }

    private static byte[] realizaImpresionCedulaHabitabilidad(ContratoInquilino c) throws Exception{

        try{
            String pathMaestro = "ReportCH.jrxml";
            Object  s = Resources.getResource("/com/company/test1/service/accessory/" + pathMaestro).getContent();
            JasperDesign designMaestro = JRXmlLoader.load((InputStream)s);
            JasperReport reportMaestro = JasperCompileManager.compileReport(designMaestro);
            ArrayList al = new ArrayList();al.add(new Object());
            SIJRBeanDataSource sijr1 = new SIJRBeanDataSource(al);

            Hashtable pams = new Hashtable();
            Departamento d = c.getDepartamento();
            if (!d.getViviendaLocal()){
                throw new Exception("El departamento es un local y por tanto no tiene Cédula de Habitabilidad");
            }else{

                CedulaHabitabilidad ch = Departamento.getCedulaHabitabilidadMasVigente(d);
                if (ch==null){
                    throw new Exception("El departamento no tiene cedula de habitabilidad");
                }
                Transaction t = AppBeans.get(Persistence.class).createTransaction();
                ch = AppBeans.get(Persistence.class).getEntityManager().reload(ch, "cedulaHabitabilidad-view");
                t.close();
                ArchivoAdjuntoExt aaext = AppBeans.get(ColeccionArchivosAdjuntosService.class).getArchivoAdjuntoExt(ch.getEscaneoCedula());
                byte[] bb = aaext.getRepresentacionSerial();
                bb = Base64.getMimeDecoder().decode(bb);
                bb = Base64.getMimeDecoder().decode(bb);
//                BufferedImage bim = javax.imageio.ImageIO.read(new ByteArrayInputStream(ch.getEscaneoCedula().getRepresentacionSerialExtDoc(SIApplication.getCurrent().getCurrentProcess().getSessionLayerExtDocs())));
                JRRenderable jrr = JRImageRenderer.getInstance(bb);
                pams.put("IMAGEN_CH", jrr);
                byte[] bytearr = JasperRunManager.runReportToPdf(reportMaestro, pams,sijr1);
                return bytearr;
            }


        }catch(Exception exc){
            throw new Exception("Falló al realizar la impresión de Cédula de Habitabilidad: " + exc.getMessage());
        }
    }

    private static byte[] realizaImpresionFormDomiciliacionBancaria(ContratoInquilino ci, boolean escribirCuentaBancaria) throws Exception{

        try{

            String pathMaestro = "ReportFormDomiciliacionBancaria.jrxml";
            String s = AppBeans.get(JasperReportService.class).getExtFileContent(pathMaestro);
            JasperDesign designMaestro = JRXmlLoader.load(new ByteArrayInputStream(s.getBytes()));


            JasperReport reportMaestro = JasperCompileManager.compileReport(designMaestro);
            ArrayList al = new ArrayList();al.add(new Object());
            SIJRBeanDataSource sijr1 = new SIJRBeanDataSource(al);

            JRRenderable jrr = (JRRenderable) AppBeans.get(JasperReportService.class).turnFileIntoJRRenderableObject("LogoGuadalest.jpg");


            String nifPropietario = ci.getDepartamento().getPropietarioEfectivo().getPersona().getNifDni();
            if (nifPropietario.compareTo("B75537878")==0){
                jrr = (JRRenderable) AppBeans.get(JasperReportService.class).turnFileIntoJRRenderableObject("cabecera_documentos_grupo_domus.png");
            }

//            JRRenderable jrr = ReportingUtilities.obtenerJRRenderablePorEntornoOPropietario(ci.getDepartamento().getPropietarioEfectivo(), SIApplication.getCurrent().getEntornosPreseleccionados(), "LOGO", SIApplication.getCurrent().getCurrentProcess().getSessionLayer(), SIApplication.getCurrent().getCurrentProcess().getSessionLayerExtDocs());

            Hashtable pams = new Hashtable();
            CuentaBancaria cb = ci.getProgramacionRecibo().getCuentaBancariaPagador();
            pams.put("NOMBRE_ARRENDADOR", ci.getDepartamento().getPropietarioEfectivo().getPersona().getNombreCompleto());
            pams.put("CIF_ARRENDADOR", ci.getDepartamento().getPropietarioEfectivo().getPersona().getNifDni());
            pams.put("CIUDAD_FECHA", "Barcelona, " + StringUtils.dateToString(new Date()));

            if (escribirCuentaBancaria){
                pams.put("CODIGO_ENTIDAD", cb.getEntidad());
                pams.put("CODIGO_OFICINA", cb.getOficina());
                pams.put("DC", cb.getDigitosControl());
                pams.put("NUMERO_CUENTA", cb.getNumeroCuenta());
            }else{
                pams.put("CODIGO_ENTIDAD", "");
                pams.put("CODIGO_OFICINA", "");
                pams.put("DC", "");
                pams.put("NUMERO_CUENTA", "");
            }

            pams.put("TITULAR_RECIBOS", ci.getInquilino().getNombreCompleto());
            pams.put("TITULAR_CUENTA", ContratoInquilino.getPagadorEfectivo(ci).getNombreCompleto());

            if (cb.getNombreEntidadBancaria()!=null){
                pams.put("NOMBRE_ENTIDAD_BANCARIA", cb.getNombreEntidadBancaria());
            }
            if (cb.getDomicilioEntidadBancaria()!=null){
                pams.put("DOMICILIO_ENTIDAD_BANCARIA", cb.getDomicilioEntidadBancaria());
            }
            pams.put("NOMBRE_DEPARTAMENTO_COMPLETO", ci.getDepartamento().getNombreDescriptivoCompleto());

            pams.put("DNI_TITULAR_CUENTA", ContratoInquilino.getPagadorEfectivo(ci).getNifDni());
            pams.put("NUM_CONTRATO",ci.getNumeroContrato());
            pams.put("P_IMAGEN", jrr);

            byte[] bytearr = JasperRunManager.runReportToPdf(reportMaestro, pams,sijr1);
            return bytearr;

        }catch(Exception exc){
            throw new Exception("Falló al realizar la impresión de Domiciliacoón Bancaria: Ver pestaña de una domicialiación y elegir cuenta bancaria");
        }
    }

    private static byte[] realizaImpresionCertificadoCalificacionEnergetica (ContratoInquilino c) throws Exception{
        Departamento d = c.getDepartamento();
        CertificadoCalificacionEnergetica cce = Departamento.getCerficadoMasVigente(d);
        Transaction t = AppBeans.get(Persistence.class).createTransaction();
        cce = AppBeans.get(Persistence.class).getEntityManager()
                .reload(cce, "certificadoCalificacionEnergetica-view");
        t.close();
        try {
            if ((cce == null) || (cce.getArchivoAdjunto() == null)) {
                return new byte[]{};
            }


            ArchivoAdjunto aa = cce.getArchivoAdjunto();
            ArchivoAdjuntoExt aaext = AppBeans.get(ColeccionArchivosAdjuntosService.class).getArchivoAdjuntoExt(aa);
            byte[] bb = aaext.getRepresentacionSerial();
            bb = Base64.getMimeDecoder().decode(bb);
            bb = Base64.getMimeDecoder().decode(bb);

            return bb;
        } catch (Exception ex) {
            throw new Exception("Falló al realizar la impresión de Certificado Calificación Energética: " + ex.getMessage());
        }
    }
}