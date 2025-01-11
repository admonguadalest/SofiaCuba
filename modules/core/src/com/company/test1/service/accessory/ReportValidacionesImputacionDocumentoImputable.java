package com.company.test1.service.accessory;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

import com.company.test1.entity.enums.DocumentoImputableTipoEnum;
import com.company.test1.entity.validaciones.ValidacionImputacionDocumentoImputable;
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
public class ReportValidacionesImputacionDocumentoImputable {


    List<ValidacionImputacionDocumentoImputable> listaVIDI;
    String pathReport = "reportvalidacionesdocimputables.jrxml";

    DocumentoImputableTipoEnum tipo;
    Date fechaInicial;
    Date fechaFinal;

    public ReportValidacionesImputacionDocumentoImputable(List<ValidacionImputacionDocumentoImputable> listaVIDI, DocumentoImputableTipoEnum tipo, Date fechaInicial, Date fechaFinal) {

        this.listaVIDI = listaVIDI;
        this.tipo=tipo;
        this.fechaInicial=fechaInicial;
        this.fechaFinal=fechaFinal;
    }


    public byte[] getReportAsByteArray()  throws Exception{


            Object  s = Resources.getResource("/com/company/test1/service/accessory/" + pathReport).getContent();
//            String pathMaestro = SIApplication.getCurrent().getSession().getService().getBaseDirectory() + this.pathReport;
            JasperDesign designMaestro = JRXmlLoader.load((InputStream) s);
            JasperReport reportMaestro = JasperCompileManager.compileReport(designMaestro);

            ArrayList datasources = new ArrayList();

            for (int i = 0; i < listaVIDI.size(); i++) {
                ValidacionImputacionDocumentoImputable vidi = listaVIDI.get(i);
                HelperReportValidacionesImputacionDocumentoImputable hlpVIDI = new HelperReportValidacionesImputacionDocumentoImputable(vidi);
                datasources.add(hlpVIDI);

            }

            Collections.sort(datasources, new Comparator<HelperReportValidacionesImputacionDocumentoImputable>() {


                @Override
                public int compare(HelperReportValidacionesImputacionDocumentoImputable h1, HelperReportValidacionesImputacionDocumentoImputable h2) {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        Date d1 = sdf.parse(h1.getFechaEmision());
                        Date d2 = sdf.parse(h2.getFechaEmision());
                        return d1.compareTo(d2);
                    }catch(Exception exc){
                        return 0;
                    }
                }
            });

            Hashtable pams = new Hashtable();
            SimpleDateFormat sdfFecha = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm");

            pams.put("P_FECHA", sdfFecha.format(new Date()));
            pams.put("P_HORA", sdfHora.format(new Date()));

            pams.put("P_FECHAINICIO", sdfFecha.format(fechaInicial));
            pams.put("P_FECHAFINAL", sdfFecha.format(fechaFinal));

            String tipoDeDocumento ="";
            String tipoDeDocumentoSingular ="";

            if (tipo == DocumentoImputableTipoEnum.FACTURA){
                tipoDeDocumento = "Facturas";
                tipoDeDocumentoSingular = "Nº Factura";
            }else if(tipo == DocumentoImputableTipoEnum.PRESUPUESTO){
                tipoDeDocumento = "Presupuestos";
                tipoDeDocumentoSingular = "Nº Presupuesto";
            }

            pams.put("P_TIPODOCUMENTO", tipoDeDocumento);
            pams.put("P_TIPODOCUMENTOSINGULAR", tipoDeDocumentoSingular);

            String nombre = "PENDIENTE";
            pams.put("P_INFORME_REALIZADO_POR", nombre);

            JRRenderable jrr = (JRRenderable) AppBeans.get(JasperReportService.class).turnFileIntoJRRenderableObject("LogoGuadalest.jpg");
            pams.put("P_IMAGEN",jrr);

            SIJRBeanDataSource mainDs = new SIJRBeanDataSource(datasources);

            byte[] bb = JasperRunManager.runReportToPdf(reportMaestro, pams, mainDs);
            return bb;





    }
}