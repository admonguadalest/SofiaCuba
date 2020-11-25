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

import com.company.test1.entity.ordenescobro.OrdenCobro;
import com.company.test1.entity.ordenescobro.RealizacionCobro;
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
public class ReportRealizacionCobro {

    RealizacionCobro realizacionCobro;


    String pathMaestro = "realizacionCobro.jrxml";

    public ReportRealizacionCobro(RealizacionCobro realizacionCobro){

        this.realizacionCobro = realizacionCobro;
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

            String nombre = "PENDIENTE";
            pams.put("P_INFORME_REALIZADO_POR", nombre);

//            JRRenderable jrr = ReportingUtilities.obtenerJRRenderablePorEntornoOPropietario(null, SIApplication.getCurrent().getEntornosPreseleccionados(), "LOGO", SIApplication.getCurrent().getCurrentProcess().getSessionLayer(), SIApplication.getCurrent().getCurrentProcess().getSessionLayerExtDocs());
            JRRenderable jrr = (JRRenderable) AppBeans.get(JasperReportService.class).turnFileIntoJRRenderableObject("LogoGuadalest.jpg");
            pams.put("P_IMAGEN",jrr);



            pams.put("P_CUENTA_DEBITORA", realizacionCobro.getCuentaBancaria().getVersionIBAN());
            pams.put("P_ENTORNO", "N/D");
            pams.put("P_IDENTIFICADOR", realizacionCobro.getIdentificador());
            pams.put("P_FECHA_CREACION", new SimpleDateFormat("dd/MM//yyyy").format(realizacionCobro.getFechaValor()));
            pams.put("P_FECHA_VALOR", new SimpleDateFormat("dd/MM//yyyy").format(realizacionCobro.getFechaValor()));
            pams.put("P_IMPORTE", NumberFormat.getCurrencyInstance(Locale.GERMANY).format(realizacionCobro.getImporteTotal()));


            List<HlpOrdenCobro> alHelpers = new ArrayList();
            List<OrdenCobro> oocc = realizacionCobro.getOrdenesCobro();
            Collections.sort(oocc, OrdenCobro.comparadorOrdenCobroPorDato);

            for (int i = 0; i < oocc.size(); i++) {
                OrdenCobro oc = oocc.get(i);
                HlpOrdenCobro hlpOrdenCobro = new HlpOrdenCobro(oc);
                alHelpers.add(hlpOrdenCobro);
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