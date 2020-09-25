package com.company.test1.service.accessory;



import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import com.company.test1.entity.departamentos.Ubicacion;
import com.company.test1.entity.recibos.Concepto;
import com.company.test1.entity.recibos.DefinicionRemesa;
import com.company.test1.entity.recibos.Remesa;

import com.company.test1.service.JasperReportService;
import com.company.test1.service.JasperReportServiceBean;
import com.company.test1.service.NumberUtilsService;
import com.company.test1.service.NumberUtilsServiceBean;
import com.google.common.io.Resources;
import com.haulmont.cuba.core.global.AppBeans;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import com.company.test1.entity.recibos.accesorios.ConceptoOrdenacionComparator;
import org.docx4j.wml.Numbering;


/**
 *
 * @author Carlos Conti
 */
public class ReportListadoResumenRecibos {



    List<HlpListadoResumenRecibos> helpers = new ArrayList<HlpListadoResumenRecibos>();
    SIJRBeanDataSource sijr = new SIJRBeanDataSource(new ArrayList());
    String pathSubreport = "listadoResumenDeRecibosA2.jrxml";
    String pathMaestro = "listadoResumenDeRecibos.jrxml";
    String pathFinal = "listadoResumenDeRecibosFinal.jrxml";

    ArrayList alPams = new ArrayList();
    ArrayList alRecibos = new ArrayList();
    ArrayList alFincas = new ArrayList();

    TreeMap tmTotales = null;
    String titulo;

    Hashtable<Concepto, Double> listaTotalesConceptos = new Hashtable<Concepto, Double>();


    JasperReportServiceBean jasperReportService;



    public void setJasperReportService(JasperReportServiceBean jasperReportService) {
        this.jasperReportService = jasperReportService;
    }

    public ReportListadoResumenRecibos(List<Remesa> remesas){

        HlpListadoResumenRecibos2.MapsReport htr = new HlpListadoResumenRecibos2().construyeEstructurasReportsDesdeListadoRemesas(remesas);


        this.tmTotales = htr.totalesAgregado;

        TreeMap pamsFincas = htr.getPams();
        TreeMap recibosFincas = htr.getRecibosFincas();

        List<Ubicacion> uu = new ArrayList(pamsFincas.keySet());
        for (int i = 0; i < uu.size(); i++) {
            Ubicacion u = uu.get(i);
            TreeMap pams = (TreeMap) pamsFincas.get(u);
            ArrayList recibos = (ArrayList) recibosFincas.get(u);

            alFincas.add(u);
            alPams.add(pams);

            Hashtable<Concepto, Double> listaTotalesConceptosUbicacion = (Hashtable<Concepto, Double>) pams.get("P_LISTATOTALESCONCEPTOSUBICACION");
            actualizaHashtableConceptos(listaTotalesConceptosUbicacion);

            alRecibos.add(new SIJRBeanDataSource(recibos));
        }

        this.titulo = "Listado de Resumen de Recibos Remesados";
    }

    public byte[] getReportAsByteArray() {

        List l_ii = new ArrayList();
        try{

            String pathMaestro = this.pathMaestro;
            Object  s = Resources.getResource("/com/company/test1/service/accessory/" + this.pathMaestro).getContent();
            JasperDesign designMaestro = JRXmlLoader.load((InputStream)s);
            JasperReport reportMaestro = JasperCompileManager.compileReport(designMaestro);

            String pathSubreport = this.pathSubreport;
            s = Resources.getResource("/com/company/test1/service/accessory/" + this.pathSubreport).getContent();
            JasperDesign designSubreport = JRXmlLoader.load((InputStream)s);
            JasperReport reportSubreport = JasperCompileManager.compileReport(designSubreport);

            Hashtable parameters = new Hashtable<String,Object>();
            parameters.put("P_TITULO", titulo);
            parameters.put("P_ARR_PAMS", alPams);
            parameters.put("P_ARR_RBOS", alRecibos);
            parameters.put("P_SUBREPORT", reportSubreport);
            parameters.putAll(tmTotales);

            String nombre = "PENDIENTE COBRAR USUARIO ACTIVO";
            parameters.put("P_INFORME_REALIZADO_POR", nombre);

            SimpleDateFormat sdfFecha = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm");

            parameters.put("P_FECHA", sdfFecha.format(new Date()));
            parameters.put("P_HORA", sdfHora.format(new Date()));

            JRRenderable jrr = jasperReportService.turnFileIntoJRRenderable("LogoGuadalest.jpg");
            parameters.put("P_IMAGEN",jrr);

            JRDataSource mainds = new SIJRBeanDataSource(alFincas);

            byte[] bytearr = JasperRunManager.runReportToPdf(reportMaestro, parameters,mainds);
            l_ii.add(new ByteArrayInputStream(bytearr));

            String pathFinal = this.pathFinal;
            s = Resources.getResource("/com/company/test1/service/accessory/" + this.pathFinal).getContent();
            JasperDesign designFinal = JRXmlLoader.load(((InputStream)s));
            JasperReport reportFinal = JasperCompileManager.compileReport(designFinal);

            Hashtable parametersFinal = new Hashtable<String,Object>();
            parametersFinal.put("P_TITULO", titulo);
            parametersFinal.put("P_IMAGEN",jrr);

            anadirTotalesConceptosRecibo(parametersFinal);

            parametersFinal.put("P_NUMRECIBOS", parameters.get("P_NUMRECIBOS"));
            parametersFinal.put("P_TOTALESBASE", parameters.get("P_TOTALESBASE"));
            parametersFinal.put("P_TOTALESIVA", parameters.get("P_TOTALESIVA"));
            parametersFinal.put("P_TOTALESIRPF", parameters.get("P_TOTALESIRPF"));
            parametersFinal.put("P_TOTALES", parameters.get("P_TOTALES"));

            //pendiente resolver


            bytearr = JasperRunManager.runReportToPdf(reportFinal, parametersFinal,new SIJRBeanDataSource(Arrays.asList("")));
            l_ii.add(new ByteArrayInputStream(bytearr));

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfUtils.concatPdfs(l_ii, baos, true);

            return baos.toByteArray();

        }catch(Exception exc){
            exc.printStackTrace();
            int y = 2;
            return null;
        }
    }

    private void actualizaHashtableConceptos(Hashtable<Concepto, Double> listaTotalesConceptosUbicacion){

        List<Concepto> cc = new ArrayList(listaTotalesConceptosUbicacion.keySet());
        for (int i = 0; i < cc.size(); i++) {
            Concepto concepto = cc.get(i);

            Double importe = listaTotalesConceptosUbicacion.get(concepto);
            importe = AppBeans.get(NumberUtilsService.class).roundTo2Decimals(importe);

            anadirConceptoATotales(concepto,importe);
        }
    }

    private void anadirConceptoATotales(Concepto concepto, Double cantidad) {

        Double total = listaTotalesConceptos.get(concepto);
        if (total == null) {
            listaTotalesConceptos.put(concepto, cantidad);

        } else {
            listaTotalesConceptos.put(concepto, total + cantidad);

        }
    }

    private void anadirTotalesConceptosRecibo(Hashtable ht) {
        List<Concepto> cc = new ArrayList(listaTotalesConceptos.keySet());
        Collections.sort(cc, new ConceptoOrdenacionComparator());

        for (int i = 0; i < cc.size(); i++) {
            Concepto c = cc.get(i);
            ht.put("P_COD" + (i+1), c.getOrdenacion().toString());
            ht.put("P_DESC" + (i+1), c.getTituloConcepto());

            Double importe = listaTotalesConceptos.get(c);

            if (!c.getAdicionSustraccion()){
                importe = importe * (-1);
            }

            importe = AppBeans.get(NumberUtilsService.class).roundTo2Decimals(importe); // Redondeamos la suma por si acaso
            DecimalFormat df = new DecimalFormat("#,##0.00");

            ht.put("P_IMP" + (i+1), df.format(importe));
        }
    }
}