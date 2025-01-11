package com.company.test1.service.accessory;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import java.text.SimpleDateFormat;
import java.util.*;

import com.company.test1.service.JasperReportService;
import com.company.test1.service.JasperReportServiceBean;
import com.haulmont.cuba.core.global.AppBeans;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JRDesignParameter;
import net.sf.jasperreports.engine.design.JasperDesign;


/**
 *
 * @author Xavier
 */
public class ReportDinamico {

    /**
     * @param args the command line arguments
     */



    String tituloReport;
    List<String> columnHeaders;
    List<List<String>> rows;
    List<Integer> anchosDeColumna;
    Hashtable<String,Object> camposDeBusqueda;
    Hashtable<String,Object> camposFooter;

    Integer numeroColumnas = 2;

    public ReportDinamico(String tituloReport,List<String> columnHeaders,List<List<String>> rows, List<Integer> anchosDeColumna,Hashtable<String,Object> camposDeBusqueda) {

        this.tituloReport = tituloReport;
        this.columnHeaders = columnHeaders;
        this.rows = rows;
        this.anchosDeColumna = anchosDeColumna;
        this.camposDeBusqueda = camposDeBusqueda;
    }

    public void setCamposFooter(Hashtable<String,Object> camposFooter){
        this.camposFooter = camposFooter;
    }

    public byte[] runReport() throws Exception {

        comprobarAnchurasCeldas(columnHeaders,anchosDeColumna);

        JasperDesign jasperReportDesign = new JasperDesign();
        jasperReportDesign.setName("Plantilla_Report");


        String nombreUsuario = "INFORMACION NO DISPONIBLE. CONSULTAR ADMINISTRADOR.";
        String hora = new SimpleDateFormat("HH:mm").format(new Date());
        String fecha = new SimpleDateFormat("dd/MM/yyyy").format(new Date());


        JRDesignParameter jrdLogo = new JRDesignParameter();
        jrdLogo.setName("P_LOGO");
        jrdLogo.setValueClass(net.sf.jasperreports.engine.JRRenderable.class);
        jrdLogo.setValueClassName(net.sf.jasperreports.engine.JRRenderable.class.getName());
        jasperReportDesign.addParameter(jrdLogo);

        JRRenderable jrr = null;
        try{
            jrr = (JRRenderable) AppBeans.get(JasperReportService.class).turnFileIntoJRRenderableObject("LogoGuadalest.jpg");
        }catch(Exception exc){

        }


        double altoImagen = 30.0;
        double anchoImagen = jrr.getDimension().getWidth() * (30.0 / jrr.getDimension().getHeight());


        DynamicReportBuilder reportBuilder = new DynamicReportBuilder(jasperReportDesign, columnHeaders.size(),anchosDeColumna);
        reportBuilder.setCamposFooter(this.camposFooter);
        reportBuilder.setNumColumns(numeroColumnas);
        reportBuilder.addTitleSection(tituloReport,nombreUsuario,hora,fecha,camposDeBusqueda,anchoImagen,altoImagen);
        reportBuilder.addPageHeaderSection(anchoImagen,altoImagen);

        reportBuilder.addDynamicColumns();
        reportBuilder.addPageFooterSection();

        JasperReport jasperReport = JasperCompileManager.compileReport(jasperReportDesign);

        Map<String, Object> params = new HashMap<String, Object>();
        if (jrr !=null){
            params.put("P_LOGO", jrr);
        }


        DynamicColumnDataSource pdfDataSource = new DynamicColumnDataSource(columnHeaders, rows);


        byte[] bytearr = JasperRunManager.runReportToPdf(jasperReport, params, pdfDataSource);
        return bytearr;
    }

    private void comprobarAnchurasCeldas(List<String> columnHeaders,List<Integer> anchosDeColumna) throws Exception {

        if (anchosDeColumna==null) return; // Los anchos son por defecto

        if (columnHeaders.size()<anchosDeColumna.size()){
            throw new JRException("El número de columnas (" + columnHeaders.size() + ") y el de las anchuras de columna (" + anchosDeColumna.size() + ") no coincide");
        }

        if (columnHeaders.size() > anchosDeColumna.size()){

            for (int i = columnHeaders.size()- 1; i >= anchosDeColumna.size(); i--) {
                columnHeaders.remove(i);

            }
        }

        int total = 0;

        for (int i = 0; i < anchosDeColumna.size(); i++) {
            Integer anchoColumna = anchosDeColumna.get(i);
            total+= anchoColumna;
        }

        //SUMO MÁRGENES
        total+= (anchosDeColumna.size()-1)*DynamicReportBuilder.SPACE_BETWEEN_COLS;

        if (total > DynamicReportBuilder.TOTAL_PAGE_WIDTH - DynamicReportBuilder.MARGIN){

            total-= (anchosDeColumna.size()-1)*DynamicReportBuilder.SPACE_BETWEEN_COLS;

            throw new JRException("Ancho de las columnas es excesivo: \n Suman " + total + " y solo podrán sumar " + (DynamicReportBuilder.TOTAL_PAGE_WIDTH - DynamicReportBuilder.MARGIN));
        }
    }


    public void setNumeroColumnas(Integer numeroColumnas) {
        this.numeroColumnas = numeroColumnas;
    }
}