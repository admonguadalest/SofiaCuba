package com.company.test1.service.accessory;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */





import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import com.company.test1.entity.departamentos.Departamento;
import com.company.test1.entity.extroles.Propietario;
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
 * @author Carlos Conti
 */
public class ReportVacios {


    //Propietario propietario = null;
    List<Departamento> departamentosVacios = null;



    String pathMaestro = "listadoDeptosVacios.jrxml";

    public ReportVacios(List<Departamento> deptos){


        departamentosVacios = deptos;

    }

    public byte[] getReportAsByteArray() {
        try{


            String pathMaestro = this.pathMaestro;
            Object  s = Resources.getResource("/com/company/test1/service/accessory/" + this.pathMaestro).getContent();
            JasperDesign designMaestro = JRXmlLoader.load((InputStream)s);

            JasperReport reportMaestro = JasperCompileManager.compileReport(designMaestro);

            List<Propietario> pp = new ArrayList<Propietario>();


            ArrayList alHelpers = new ArrayList();
            for (int i = 0; i < departamentosVacios.size(); i++) {
                Departamento departamento = departamentosVacios.get(i);
                alHelpers.add(new HlpDepartamentoVacio(departamento));
                pp.add(departamento.getPropietarioEfectivo());
            }

            Hashtable pams = new Hashtable();

            pams.put("P_FECHA", new SimpleDateFormat("dd/MM/YYYY").format(new Date()));
            pams.put("P_HORA", new SimpleDateFormat("HH/mm").format(new Date()));
            pams.put("P_FECHA2", new SimpleDateFormat("dd/MM/YYYY").format(new Date()));
            pams.put("P_TOTAL_PISOS_VACIOS", ((Integer) departamentosVacios.size()).toString());

            String nombre = "DESARROLLO PENDIENTE";
            pams.put("P_INFORME_REALIZADO_POR", nombre);

            JRRenderable jrr = (JRRenderable) AppBeans.get(JasperReportService.class).turnFileIntoJRRenderableObject("LogoGuadalest.jpg");
            pams.put("P_IMAGEN",jrr);



            SIJRBeanDataSource mainDs = new SIJRBeanDataSource(alHelpers);

            byte[] bytearr = JasperRunManager.runReportToPdf(reportMaestro, pams, mainDs);
            return bytearr;

        }catch(Exception exc){
            int y = 2;
        }
        return null;
    }

}
