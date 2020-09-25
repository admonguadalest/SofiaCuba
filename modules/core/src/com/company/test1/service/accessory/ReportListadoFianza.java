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

import com.company.test1.entity.contratosinquilinos.Fianza;
import com.company.test1.entity.departamentos.Ubicacion;
import com.company.test1.service.JasperReportService;
import com.google.common.io.Resources;
import com.haulmont.cuba.core.global.AppBeans;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;



/**
 *
 * @author Xavier
 */
public class ReportListadoFianza {


    List<Fianza> fianzas;

    String pathMaestro = "reportFianza.jrxml";
    String pathSubreport = "reportFianza2.jrxml";


    Double noIngresadaEnAdmon = 0.0;
    Double fianzaAdmon = 0.0;
    Double fianzaCamara = 0.0;
    Double solicitadaDevolucion = 0.0;
    Double fianzaDevuelta = 0.0;

    Double garantiaAval = 0.0;
    Double garantiaEfectivo = 0.0;

    String finalTitulo = "";



    public ReportListadoFianza(List<Fianza> fianzas, String finalTitulo){

        this.fianzas = fianzas;
        this.finalTitulo = finalTitulo;

    }

    public byte[] getReportAsByteArray() {

        try {


            Object  s = Resources.getResource("/com/company/test1/service/accessory/" + this.pathMaestro).getContent();
            JasperDesign designMaestro = JRXmlLoader.load((InputStream)s);
            JasperReport reportMaestro = JasperCompileManager.compileReport(designMaestro);

            s = Resources.getResource("/com/company/test1/service/accessory/" + this.pathSubreport).getContent();
            JasperDesign designSubreport = JRXmlLoader.load((InputStream)s);
            JasperReport reportSubreport = JasperCompileManager.compileReport(designSubreport);

            Hashtable pams = new Hashtable();
            pams.put("subreport", reportSubreport);

            SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm");
            SimpleDateFormat sdfFecha = new SimpleDateFormat("dd/MM/yyyy");

            pams.put("P_HORA", sdfHora.format(new Date()));
            pams.put("P_FECHA", sdfFecha.format(new Date()));

            String nombre = "PENDIENTE";
            pams.put("P_INFORME_REALIZADO_POR", nombre);


//            JRRenderable jrr = ReportingUtilities.obtenerJRRenderablePorEntornoOPropietario(null, SIApplication.getCurrent().getEntornosPreseleccionados(), RecursoEntornoPropietario.NCABECERA_DOCS, SIApplication.getCurrent().getCurrentProcess().getSessionLayer(), SIApplication.getCurrent().getCurrentProcess().getSessionLayerExtDocs());
            JRRenderable jrr = (JRRenderable) AppBeans.get(JasperReportService.class).turnFileIntoJRRenderableObject("NCABECERA_DOCS.svg");
            pams.put("P_IMAGEN",jrr);

//            String camposDeBusqueda = getCamposDeBusqueda(criteriaFianzas);
//            if (camposDeBusqueda.length()>0){
//                pams.put("P_CAMPOS_DE_BUSQUEDA",camposDeBusqueda);
//            }
            pams.put("P_CAMPOS_DE_BUSQUEDA", "(PENDIENTE CONCILIAR)");

            TreeMap<Ubicacion, List<Fianza>> tmUbicacionFianzas = doTreeMapp();
            List<Ubicacion> uu = collectionToList(tmUbicacionFianzas.keySet());

            List<HlpUbicacion> listaHelpers = new ArrayList<HlpUbicacion>();

            for (int i = 0; i < uu.size(); i++) {
                Ubicacion ub = uu.get(i);
                HlpUbicacion hlpUbicacion = new HlpUbicacion(ub,tmUbicacionFianzas.get(ub));
                hlpUbicacion.doParteFianzas();

                listaHelpers.add(hlpUbicacion);
                anadirTotalesFincaATotalFinal(hlpUbicacion);
            }





            pams.put("P_GARANTIA_AVAL", garantiaAval);
            pams.put("P_GARANTIA_EFECTIVO", garantiaEfectivo);

            pams.put("P_NO_INGRESADA_EN_ADMON", noIngresadaEnAdmon);
            pams.put("P_FIANZA_ADMON", fianzaAdmon);
            pams.put("P_FIANZA_CAMARA", fianzaCamara);
            pams.put("P_SOLICITADA_DEVOLUCION", solicitadaDevolucion);
            pams.put("P_FIANZA_DEVUELTA", fianzaDevuelta);


            pams.put("P_FINAL_TITULO", finalTitulo);


            JRDataSource mainds = new SIJRBeanDataSource(listaHelpers);

            byte[] bytearr = JasperRunManager.runReportToPdf(reportMaestro, pams, mainds);
            return bytearr;

        } catch (Exception ex) {
            int x=2;
        }

        return null;
    }

    public TreeMap<Ubicacion, List<Fianza>> doTreeMapp() {

        TreeMap<Ubicacion, List<Fianza>> tm = new TreeMap<Ubicacion, List<Fianza>>();

        for (int i = 0; i < fianzas.size(); i++) {
            Fianza f = fianzas.get(i);

            Ubicacion ub = f.getContratoInquilino().getDepartamento().getUbicacion();

            List<Fianza> ff = tm.get(ub);
            if (ff==null){
                ff = new ArrayList<Fianza>();
                tm.put(ub, ff);

            }

            if (ff.indexOf(f)==-1) ff.add(f);
        }

        return tm;
    }

    private void anadirTotalesFincaATotalFinal(HlpUbicacion hlpUbicacion) {


        garantiaAval += hlpUbicacion.garantiaAval;
        garantiaEfectivo += hlpUbicacion.garantiaEfectivo;

        noIngresadaEnAdmon += hlpUbicacion.noIngresadaEnAdmon;
        fianzaAdmon += hlpUbicacion.fianzaAdmon;
        fianzaCamara += hlpUbicacion.fianzaCamara;
        solicitadaDevolucion += hlpUbicacion.solicitadaDevolucion;
        fianzaDevuelta += hlpUbicacion.fianzaDevuelta;

    }

    public static List iteratorToList(Iterator iterator){
        if (iterator == null) return new ArrayList();

        List lista = new ArrayList();

        while(iterator.hasNext()){
            Object o = iterator.next();
            lista.add(o);
        }

        return lista;
    }


    public static List collectionToList(Collection c){
        if (c == null) return new ArrayList();
        return iteratorToList(c.iterator());
    }

//    private String getCamposDeBusqueda(Projection_Fianzas criteriaFianzas){
//
////        String s = "";
////
////        if (criteriaFianzas.getTiposDeFianza().length() > 0){
////            s += "<p> <b>Estado de la Fianza: </b>" +  criteriaFianzas.getTiposDeFianza() + "</p>";
////
////        }
////
////        if (criteriaFianzas.getTiposDeGarantia().length() > 0){
////            s += "<p> <b>Tipo de Garantía: </b>" +  criteriaFianzas.getTiposDeGarantia() + "</p>";
////
////        }
////
////        return s;
//        return "(PENDIENTE CONCILIAR)";
//    }
}