package com.company.test1.service.accessory;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

import com.company.test1.entity.contratosinquilinos.ContratoInquilino;
import com.company.test1.entity.recibos.ConceptoRecibo;
import com.company.test1.service.JasperReportService;
import com.google.common.io.Resources;
import com.haulmont.cuba.core.global.AppBeans;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;



/**
 *
 * @author Carlos Conti
 */
public class ReportListadoAumentosIPC {

    String pathMaestro = "listadoaumentosipc.jrxml";
    String pathMaestro_Historico = "listadoaumentosipc_historico.jrxml";
    String pathSubreport = "listadoaumentosipc2.jrxml";

    List<ContratoInquilino> contratos = null;
    List<ConceptoRecibo> conceptosRecibo = null;
    Date fechaDesde = null;
    Date fechaHasta = null;
    Date fechaIncremento = null;
    Date fechaRevision = null;

    Hashtable pams = new Hashtable();
    //instancia dpa llamada desde metodos reflect. ThreadLocal no funciona en ese contexto




    public ReportListadoAumentosIPC(List<ContratoInquilino> contratos, Date fechaDesde, Date fechaHasta) {

        this.contratos = contratos;
        this.fechaDesde = fechaDesde;
        this.fechaHasta = fechaHasta;


    }


    public ReportListadoAumentosIPC(List<ConceptoRecibo> ccrr, Date fechaIncremento, Date fechaRevision, boolean previsualizacion) {

        this.conceptosRecibo = ccrr;
        this.fechaRevision = fechaRevision;
        this.fechaIncremento = fechaIncremento;
    }

    public byte[] getReportAsByteArray() {
        try{
            String pathMaestro = this.pathMaestro;
            if (contratos!=null){
                pathMaestro = pathMaestro_Historico;
            }
            Object  s = Resources.getResource("/com/company/test1/service/accessory/" + pathMaestro).getContent();
            JasperDesign designMaestro = JRXmlLoader.load((InputStream)s);
            JasperReport reportMaestro = JasperCompileManager.compileReport(designMaestro);

            s = Resources.getResource("/com/company/test1/service/accessory/" + pathSubreport).getContent();
            JasperDesign designSubreport = JRXmlLoader.load((InputStream)s);
            JasperReport reportSubreport = JasperCompileManager.compileReport(designSubreport);

            Hashtable pams = new Hashtable();
            pams.put("P_SUBREPORT", reportSubreport);
            if (fechaRevision!=null){
                pams.put("P_FECHAREVISION",fechaRevision);
            }

            if (fechaIncremento!=null){
                pams.put("P_FECHAINCREMENTO",fechaIncremento);
            }

            if (fechaDesde!=null){
                pams.put("P_FECHADESDE", new SimpleDateFormat("dd/MM/yyyy").format(fechaDesde));
            }

            if (fechaHasta!=null){
                pams.put("P_FECHAHASTA",new SimpleDateFormat("dd/MM/yyyy").format(fechaHasta));
            }

            String nombre = "[USUARIO ACTIVO PENDIENTE]";
            pams.put("P_INFORME_REALIZADO_POR", nombre);

            SimpleDateFormat sdfFecha = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm");

            pams.put("P_FECHA", sdfFecha.format(new Date()));
            pams.put("P_HORA", sdfHora.format(new Date()));

            JasperReportService jasperReportService = AppBeans.get(JasperReportService.class);
            JRRenderable jrr = (JRRenderable)jasperReportService.turnFileIntoJRRenderableObject("LogoGuadalest.jpg");
            pams.put("P_IMAGEN",jrr);


            List<HlpListadoAumentosIPC> detalle = new ArrayList<HlpListadoAumentosIPC>();

            //Modo Lectura BBDD
            if (contratos!=null){
                for (int i = 0; i < contratos.size(); i++) {
                    ContratoInquilino contratoInquilino = contratos.get(i);
                    HlpListadoAumentosIPC hlp = new HlpListadoAumentosIPC(contratoInquilino, fechaDesde, fechaHasta);
                    detalle.add(hlp);
                }
            }
            //Modo previsualizacion
            if (conceptosRecibo != null){
                for (int i = 0; i < conceptosRecibo.size(); i++) {
                    ConceptoRecibo cr = conceptosRecibo.get(i);
                    ContratoInquilino c = cr.getProgramacionRecibo().getContratoInquilino();
                    HlpListadoAumentosIPC hlp = new HlpListadoAumentosIPC(c,Arrays.asList(cr),fechaDesde,fechaHasta);
                    detalle.add(hlp);
                }
            }

            SIJRBeanDataSource mainds = new SIJRBeanDataSource(detalle);

            byte[] bytearr = JasperRunManager.runReportToPdf(reportMaestro, pams, mainds);
            return bytearr;

        }catch(Exception exc){
            int y = 2;
        }
        return null;

    }
}