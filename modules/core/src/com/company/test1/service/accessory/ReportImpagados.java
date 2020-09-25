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

import com.company.test1.entity.Persona;
import com.company.test1.entity.departamentos.Ubicacion;
import com.company.test1.entity.extroles.Propietario;
import com.company.test1.entity.recibos.Recibo;
import com.company.test1.service.JasperReportService;
import com.company.test1.service.NumberUtilsService;
import com.company.test1.service.RecibosService;
import com.google.common.io.Resources;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
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
public class ReportImpagados {

    List<Recibo> recibosImpagados = null;

    Propietario propietario = null;
    Date fechaInicial = null;
    Date fechaFinal = null;
    String vigencia;
    String tipoRecibos;

    List<HlpFinca> alFincas = new ArrayList<HlpFinca>();

    String pathFinca = "listadoPendientesYDevueltosFinca.jrxml";
    String pathInquilino = "listadoPendientesYDevueltosInquilinos.jrxml";
    String pathRecibo = "listadoPendientesYDevueltosRecibo.jrxml";

    RecibosService recibosService;

    SecurityContext securityContext = AppContext.getSecurityContext();

    public ReportImpagados(RecibosService rs, Propietario p, List<Recibo> rr,Date fechaInicial,Date fechaFinal,String vigencia, String tipoRecibos) {

        this.recibosService = rs;
        propietario = p;
        this.recibosImpagados = rr;
        this.fechaInicial = fechaInicial;
        this.fechaFinal = fechaFinal;
        this.vigencia = vigencia;
        this.tipoRecibos = tipoRecibos;

        if (this.vigencia == null) this.vigencia = "Todos";
        if (this.tipoRecibos == null) this.tipoRecibos = "Todos";

        JasperReport reportRecibo = null;

        try{
//            String pathReciboNombre = SIApplication.getCurrent().getSession().getService().getBaseDirectory() + this.pathRecibo;
            Object  s = Resources.getResource("/com/company/test1/service/accessory/" + this.pathRecibo).getContent();
            JasperDesign designRecibo = JRXmlLoader.load((InputStream)s);

            reportRecibo = JasperCompileManager.compileReport(designRecibo);
        }catch(Exception ex){
            int x = 2;
        }


        TreeMap pams = new HlpRecibo().construyeEstructurasReportsDesdeListadoRecibos(rr, securityContext);


        List<Ubicacion> uu = new ArrayList(pams.keySet());
        for (int i = 0; i < uu.size(); i++) {

            Ubicacion u = uu.get(i);
            TreeMap<String,Object> ubicacionesObjetos = (TreeMap<String,Object>) pams.get(u);

            List<Persona> pp = (List<Persona>) ubicacionesObjetos.get("PERSONAS");
            List<HlpInquilino> helpersPersona = new ArrayList<HlpInquilino>();

            TreeMap<Persona,List<HlpRecibo>> personasRecibo = (TreeMap<Persona,List<HlpRecibo>>) ubicacionesObjetos.get("PERSONAS_RECIBOS");

            for (int j = 0; j < pp.size(); j++) {
                Persona persona = pp.get(j);

                List<HlpRecibo> listaRecibos = personasRecibo.get(persona);
                Collections.sort(listaRecibos, new Comparator<HlpRecibo>() {
                    @Override
                    public int compare(HlpRecibo hlpRecibo, HlpRecibo t1) {
                        return hlpRecibo.getRecibo().getFechaEmision().compareTo(t1.getRecibo().getFechaEmision());
                    }
                });
                HlpInquilino hlpInquilino = new HlpInquilino(persona,personasRecibo.get(persona),reportRecibo);

                for (int k = 0; k < listaRecibos.size(); k++) {
                    HlpRecibo hlpRecibo = listaRecibos.get(k);
                    Recibo recibo = hlpRecibo.getRecibo();
                    hlpInquilino.anadirReciboAInquilinoRecibo(recibo);
                }

                helpersPersona.add(hlpInquilino);
            }

            Collections.sort(helpersPersona, new Comparator<HlpInquilino>() {
                @Override
                public int compare(HlpInquilino t0, HlpInquilino t1) {
                    return t0.getDepartamento().getRm2id().compareTo(t1.getDepartamento().getRm2id());
                }
            });

            HlpFinca hlpFinca = (HlpFinca) ubicacionesObjetos.get("HELPER_FINCA");
            hlpFinca.asignarHelpers(helpersPersona);
            alFincas.add(hlpFinca);
        }
    }

    public byte[] getReportAsByteArray() {
        try{

//            String pathFincaNombre = SIApplication.getCurrent().getSession().getService().getBaseDirectory() + this.pathFinca;
            Object  s = Resources.getResource("/com/company/test1/service/accessory/" + this.pathFinca).getContent();
            JasperDesign designFinca = JRXmlLoader.load((InputStream)s);

            JasperReport reportFinca = JasperCompileManager.compileReport(designFinca);

//            String pathInquilinoNombre = SIApplication.getCurrent().getSession().getService().getBaseDirectory() + this.pathInquilino;
            s = Resources.getResource("/com/company/test1/service/accessory/" + this.pathInquilino).getContent();
            JasperDesign designInquilino = JRXmlLoader.load((InputStream)s);

            JasperReport reportInquilino = JasperCompileManager.compileReport(designInquilino);


            Hashtable parameters = new Hashtable();
            parameters.put("P_SUBREPORT", reportInquilino);


            double totalDevuelto=0.0;
            double totalPendiente=0.0;
            double totalFinal;

            for (int i = 0; i < alFincas.size(); i++) {
                HlpFinca hlpF = alFincas.get(i);

                totalDevuelto+= hlpF.getTotalDevueltoNumero();
                totalPendiente+= hlpF.getTotalPendienteNumero();
            }

            totalFinal = totalDevuelto + totalPendiente;

            totalDevuelto = AppBeans.get(NumberUtilsService.class).roundToNDecimals(totalDevuelto, 2.0);
            totalPendiente = AppBeans.get(NumberUtilsService.class).roundToNDecimals(totalPendiente, 2.0 );
            totalFinal = AppBeans.get(NumberUtilsService.class).roundToNDecimals(totalFinal, 2.0);

            NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.GERMANY);

            parameters.put("P_TOTAL_DEVUELTO",nf.format(totalDevuelto));
            parameters.put("P_TOTAL_PENDIENTE",nf.format(totalPendiente));
            parameters.put("P_TOTAL_FINAL",nf.format(totalFinal));

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            parameters.put("P_FECHA_INICIAL",sdf.format(fechaInicial));
            parameters.put("P_FECHA_FINAL",sdf.format(fechaFinal));

            String nombre = "PENDIENTE APLICACION";
            parameters.put("P_INFORME_REALIZADO_POR", nombre);

            SimpleDateFormat sdfFecha = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm");

            parameters.put("P_FECHA", sdfFecha.format(new Date()));
            parameters.put("P_HORA", sdfHora.format(new Date()));
            parameters.put("P_VIGENCIA", vigencia);
            parameters.put("P_TIPO_RECIBOS", tipoRecibos);

//            JRRenderable jrr = ReportingUtilities.obtenerJRRenderablePorEntornoOPropietario(propietario, SIApplication.getCurrent().getEntornosPreseleccionados(), "LOGO", SIApplication.getCurrent().getCurrentProcess().getSessionLayer(), SIApplication.getCurrent().getCurrentProcess().getSessionLayerExtDocs());
            JRRenderable jrr = (JRRenderable)AppBeans.get(JasperReportService.class).turnFileIntoJRRenderableObject("LogoGuadalest.jpg");
            parameters.put("P_IMAGEN",jrr);

            SIJRBeanDataSource mainDs = new SIJRBeanDataSource(alFincas);


            byte[] bytearr = JasperRunManager.runReportToPdf(reportFinca, parameters,mainDs);
            return bytearr;

        }catch(Exception exc){
            int y = 2;
            return null;
        }
    }


    public List<Recibo> getRecibosImpagados() {
        return recibosImpagados;
    }

    public void setRecibosImpagados(List<Recibo> recibosImpagados) {
        this.recibosImpagados = recibosImpagados;
    }
}