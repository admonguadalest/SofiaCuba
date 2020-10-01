package com.company.test1.service.accessory;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import java.io.File;
import java.io.InputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import com.company.test1.entity.ordenespago.OrdenPago;
import com.company.test1.entity.ordenespago.RealizacionPago;
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
public class ReportRealizacionPago {

    RealizacionPago realizacionPago;


    String pathMaestro = "realizacionPago.jrxml";

    public ReportRealizacionPago(RealizacionPago realizacionPago){

        this.realizacionPago = realizacionPago;
    }

    public byte[] getReportAsByteArray() {
        try{
            Object  s = Resources.getResource("/com/company/test1/service/accessory/" + this.pathMaestro).getContent();
//            String pathMaestro = SIApplication.getCurrent().getSession().getService().getBaseDirectory() + this.pathMaestro;
            JasperDesign designMaestro = JRXmlLoader.load((InputStream)s);
            JasperReport reportMaestro = JasperCompileManager.compileReport(designMaestro);


            Hashtable pams = new Hashtable();


            pams.put("P_FECHA", new SimpleDateFormat("dd/MM/YYYY").format(new Date()));
            pams.put("P_HORA", new SimpleDateFormat("HH/mm").format(new Date()));

//            String nombre = SIApplication.getCurrent().getUsuarioActivo().getPersona().getNombreCompleto();
            pams.put("P_INFORME_REALIZADO_POR", "PENDIENTE");

//            JRRenderable jrr = ReportingUtilities.obtenerJRRenderablePorEntornoOPropietario(null, SIApplication.getCurrent().getEntornosPreseleccionados(), "LOGO", SIApplication.getCurrent().getCurrentProcess().getSessionLayer(), SIApplication.getCurrent().getCurrentProcess().getSessionLayerExtDocs());
            JRRenderable jrr = (JRRenderable) AppBeans.get(JasperReportService.class).turnFileIntoJRRenderableObject("LogoGuadalest.jpg");

            pams.put("P_IMAGEN",jrr);


            pams.put("P_CUENTA_DEBITORA", realizacionPago.getCuentaBancaria().getVersionIBAN());
            pams.put("P_ENTORNO", "");
            pams.put("P_IDENTIFICADOR", realizacionPago.getIdentificador());
            pams.put("P_FECHA_CREACION", new Date());//pendiente hacer fetch del createTs
            pams.put("P_FECHA_VALOR", new SimpleDateFormat("dd/MM//yyyy").format(realizacionPago.getFechaValor()));
            pams.put("P_IMPORTE", NumberFormat.getCurrencyInstance().format(realizacionPago.getImporteTotal()));


            List<HlpOrdenPago> alHelpers = new ArrayList();

            List<OrdenPago> oopp = realizacionPago.getOrdenesPago();
//            Collections.sort(oopp, OrdenPago.getComparadorOrdenPagoPorDato());

            for (int i = 0; i < oopp.size(); i++) {
                OrdenPago op = oopp.get(i);
                HlpOrdenPago hlpOrdenPago = new HlpOrdenPago(op);
                alHelpers.add(hlpOrdenPago);
            }


            SIJRBeanDataSource mainDs = new SIJRBeanDataSource(alHelpers);

            byte[] bytearr = JasperRunManager.runReportToPdf(reportMaestro, pams, mainDs);
            return bytearr;

        }catch(Exception exc){
            int y = 2;
        }
        return null;
    }
}
