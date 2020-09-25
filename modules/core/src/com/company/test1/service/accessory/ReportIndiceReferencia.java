package com.company.test1.service.accessory;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import java.io.File;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import com.company.test1.entity.recibos.ConceptoRecibo;
import com.company.test1.service.JasperReportService;
import com.google.common.io.Resources;
import com.haulmont.cuba.core.global.AppBeans;
import net.sf.jasperreports.engine.JRRenderable;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;


/**
 *
 * @author Xavier
 */
public class ReportIndiceReferencia {

    String pathReport = "previsualizacionIncrementosIndiceReferencia.jrxml";

    List<ConceptoRecibo> conceptosRecibo;
    Integer mes;
    Integer anno;
    Integer mesesAtrasos;

    Date fechaAplicacionIncremento;


    public ReportIndiceReferencia(List<ConceptoRecibo> conceptosRecibo, Integer mesesAtrasos, Integer mes, Integer anno, Date fechaAplicacionIncremento){

        this.conceptosRecibo = conceptosRecibo;
        this.mes = mes;
        this.anno = anno;
        this.mesesAtrasos = mesesAtrasos;
        this.fechaAplicacionIncremento = fechaAplicacionIncremento;
    }



    public byte[] getReportAsByteArray(){

        try {
//            String pathMaestro = SIApplication.getCurrent().getSession().getService().getBaseDirectory() + this.pathReport;
//            JasperDesign designMaestro = JRXmlLoader.load(new File(pathMaestro));
//            JasperReport reportMaestro = JasperCompileManager.compileReport(designMaestro);

            Object  s = Resources.getResource("/com/company/test1/service/accessory/" + this.pathReport).getContent();
            JasperDesign designMaestro = JRXmlLoader.load((InputStream)s);
            JasperReport reportMaestro = JasperCompileManager.compileReport(designMaestro);

            ArrayList al = new ArrayList();
            for (int i = 0; i < conceptosRecibo.size(); i++) {
                ConceptoRecibo conceptoRecibo = conceptosRecibo.get(i);
                HlpIndiceReferencia hlpir = new HlpIndiceReferencia(conceptoRecibo,mesesAtrasos, mes, anno);
                al.add(hlpir);
            }

            SIJRBeanDataSource mainDs = new SIJRBeanDataSource(al);

            Hashtable pams = new Hashtable();

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MONTH, mes-1);
            cal.set(Calendar.YEAR,anno);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            DateFormat df_ = new SimpleDateFormat("dd-MM-yyyy");
            DateFormat df = new SimpleDateFormat("MMyyyy");
            DateFormat dfh = new SimpleDateFormat("hh:mm:ss");
            pams.put("fechaRevision", df.format(cal.getTime()));
            pams.put("fechaIncremento", df_.format(fechaAplicacionIncremento));
            pams.put("P_FECHA",df.format(new Date()));
            pams.put("P_HORA",dfh.format(new Date()));

            String nombre = "PENDIENTE DESARROLLO";
            pams.put("P_INFORME_REALIZADO_POR", nombre);


            //ncabECERA DOCS
//            JRRenderable jrr = ReportingUtilities.obtenerJRRenderablePorEntornoOPropietario(null, SIApplication.getCurrent().getEntornosPreseleccionados(), RecursoEntornoPropietario.NCABECERA_DOCS, SIApplication.getCurrent().getCurrentProcess().getSessionLayer(), SIApplication.getCurrent().getCurrentProcess().getSessionLayerExtDocs());
            JRRenderable jrr = (JRRenderable)AppBeans.get(JasperReportService.class).turnFileIntoJRRenderableObject("LogoGuadalest.jpg");
            pams.put("P_IMAGEN",jrr);


            byte[] bb = JasperRunManager.runReportToPdf(reportMaestro, pams, mainDs);
            return bb;

        }catch(Exception ex){
            int x=2;
        }

        return null;
    }

}
