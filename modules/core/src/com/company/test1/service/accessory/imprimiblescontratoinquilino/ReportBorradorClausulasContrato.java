package com.company.test1.service.accessory.imprimiblescontratoinquilino;



import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import com.company.test1.entity.contratosinquilinos.ContratoInquilino;
import com.company.test1.entity.contratosinquilinos.ImplementacionModelo;
import com.company.test1.entity.contratosinquilinos.OverrideClausula;
import com.company.test1.entity.contratosinquilinos.ParametroValor;
import com.company.test1.entity.enums.EstadoContratoInquilinoEnum;
import com.company.test1.entity.modeloscontratosinquilinos.Clausula;
import com.company.test1.entity.modeloscontratosinquilinos.ModeloContrato;
import com.company.test1.entity.modeloscontratosinquilinos.Seccion;
import com.company.test1.entity.modeloscontratosinquilinos.VersionClausula;
import com.company.test1.service.accessory.SIJRBeanDataSource;
import com.google.common.io.Resources;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.global.AppBeans;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;



public class ReportBorradorClausulasContrato{

    String titulo = "";

    String pathMaestroContratoInquilino = "SeccionTablaSecciones.jrxml";
    String pathSubreportContratoInquilino = "SeccionTablaImplementacionesClausulas.jrxml";


    String pathMaestroContratoPropietario = "SeccionTablaSecciones.jrxml";
    String pathSubreportContratoPropietario = "SeccionTablaImplementacionesClausulas.jrxml";

    String pathMaestro;
    String pathSubreport;



    List dataSource1 = null;
    List dataSource2 = null;

    ContratoInquilino contratoInquilino = null;
//    ContratoPropietario contratoPropietario = null;

    Hashtable parameters = new Hashtable();

    public ReportBorradorClausulasContrato(Object o) {


        if (o instanceof ContratoInquilino) {
            this.contratoInquilino = (ContratoInquilino) o;
        }
//        }else if (o instanceof ContratoPropietario){
//            this.contratoPropietario = (ContratoPropietario) o;
//        }

        asignarPlantillaSegunTipoDeContrato();
    }

    public byte[] getReportAsByteArray() throws Exception{

        Object  s = Resources.getResource("/com/company/test1/service/accessory/" + pathMaestro).getContent();
        JasperDesign designMaestro = JRXmlLoader.load((InputStream)s);
        JasperReport reportMaestro = JasperCompileManager.compileReport(designMaestro);

        s = Resources.getResource("/com/company/test1/service/accessory/" + pathSubreport).getContent();
        JasperDesign designSubreport = JRXmlLoader.load((InputStream)s);
        JasperReport reportSubreport = JasperCompileManager.compileReport(designSubreport);

        this.dataSource1 = this.getSeccionesIncluidas();
        SIJRBeanDataSource sijr1 = new SIJRBeanDataSource(this.dataSource1);
        //parametros
        this.setParameters(new Hashtable<String,Object>());
        this.getParameters().put("SR_IMPLEMENTACIONES_CLAUSULAS", reportSubreport);

        this.getParameters().put("PM_REPORTING_UTILITIES", new ReportingUtilities());

        if (contratoInquilino != null) {

            this.getParameters().put("PM_CONTRATO", this.contratoInquilino);
            if (EstadoContratoInquilinoEnum.compare(this.contratoInquilino.getEstadoContrato(), EstadoContratoInquilinoEnum.AUTORIZADO) >= 0) {
                this.getParameters().put("ES_BORRADOR", false);
            } else {
                this.getParameters().put("ES_BORRADOR", true);
            }
        }
//        } else {
//
//            this.getParameters().put("PM_CONTRATO", this.contratoPropietario);
//            if (this.contratoPropietario.getEstadoNumero()>= ContratoPropietario.getEstadoNumeroContratoPropietarioDeString(ContratoPropietario.EC_AUTORIZADO)) {
//                this.getParameters().put("ES_BORRADOR", false);
//            } else {
//                this.getParameters().put("ES_BORRADOR", true);
//            }
//        }

        byte[] bytearr = JasperRunManager.runReportToPdf(reportMaestro, this.getParameters(),sijr1);
        return bytearr;
    }




    /*
     * Metodos de apoyo
     */
    public List getSeccionesIncluidas() throws Exception{
        List l = new ArrayList();
        Integer contadorClausulas = new Integer(1);
        ModeloContrato mc = getImplementacionModelo().getModeloContrato();
        mc = AppBeans.get(Persistence.class).getEntityManager().reload(mc, "modeloContrato-view");
        getImplementacionModelo().setModeloContrato(mc);
        List<Seccion> secciones = new ArrayList(getImplementacionModelo().getModeloContrato().getSecciones());
        Collections.sort(secciones, Seccion.comparadorOrdenacion);

        for (int i = 0; i < secciones.size(); i++) {
            Seccion s = secciones.get(i);
            int index = getImplementacionModelo().getSeccionesDescartadas().indexOf( s);
            if (index==-1){
                ImplementacionSeccion is = new ImplementacionSeccion();
                is.setNombreSeccion(s.getNombre());
                //incluyendo las clausulas pertinentes

                List iicc = getImplementacionesClausulasParaSeccion(getImplementacionModelo(), s,contadorClausulas);
                contadorClausulas += s.getClausulas().size();
                is.setImplementacionesClausulas(iicc);
                l.add(is);
            }

        }
        return l;
    }

    public Hashtable getParameters() {
        return parameters;
    }

    public void setParameters(Hashtable parameters) {
        this.parameters = parameters;
    }


    private ImplementacionModelo getImplementacionModelo(){
        if (contratoInquilino != null) {
            return contratoInquilino.getImplementacionModelo();
        }
//        }else if(contratoPropietario != null){
//            return contratoPropietario.getImplementacionModelo();
//        }

        return null;
    }

    private void asignarPlantillaSegunTipoDeContrato(){

        if (contratoInquilino != null) {

            pathMaestro = pathMaestroContratoInquilino;
            pathSubreport = pathSubreportContratoInquilino;
        }
//        } else if (contratoPropietario != null) {
//
//            pathMaestro = pathMaestroContratoPropietario;
//            pathSubreport = pathSubreportContratoPropietario;
//        }
    }

    public static List getImplementacionesClausulasParaSeccion(ImplementacionModelo im, Seccion s, Integer contadorClausulas) throws Exception{
        List l = new ArrayList();



        for (int j = 0; j < s.getClausulas().size(); j++) {
            Clausula c = s.getClausulas().get(j);
            VersionClausula vc = Clausula.getVersionClausulaAplicada(im, c);
            String txt = vc.getTextoVersion();
            for (int k = 0; k < im.getParametrosValores().size(); k++) {
                ParametroValor pv = im.getParametrosValores().get(k);
                String valor = "";
                if (pv.getValor()==null){
                    pv.setValor("");
                }
                if (pv.getValor().trim().length()>0){
                    String resStr = "@[" + pv.getNombreParametro() + "]";
                    txt = txt.replace("@["+ pv.getNombreParametro() +"]", pv.getValor());
                }

            }

            ImplementacionClausula ic = new ImplementacionClausula();
            ic.setOrdinalClausula(new Integer(contadorClausulas).toString());
            ic.setTextoClausula(txt);
            l.add(ic);
            contadorClausulas++;
        }
        return l;
    }





}