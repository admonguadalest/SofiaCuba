/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company.test1.service.accessory;



import java.io.File;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;


import com.company.test1.entity.coeficientes.Coeficiente;
import com.company.test1.entity.coeficientes.TipoCoeficiente;
import com.company.test1.entity.coeficientes.UbicacionCoeficiente;
import com.company.test1.entity.contratosinquilinos.ContratoInquilino;
import com.company.test1.entity.departamentos.Departamento;
import com.company.test1.entity.departamentos.Ubicacion;
import com.company.test1.entity.recibos.Concepto;
import com.company.test1.entity.recibos.ConceptoRecibo;
import com.company.test1.service.ContratosService;
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
public class ReportUbicacionListadoIncrementosGenerales {


    Ubicacion ubicacion = null;
    Hashtable<Departamento, ConceptoRecibo[]> conceptosRecibo=null;
    Concepto concepto;
    TipoCoeficiente tipoCoeficienteAplicado = null;
    Integer mesesAtrasos;
    Date fechaRepercusion;
    Double importeRepercutir;


    String pathReport = "previsualizacionIncrementosGeneralesYObras.jrxml";

    public ReportUbicacionListadoIncrementosGenerales(

            Ubicacion ubicacion,
            Hashtable<Departamento,ConceptoRecibo[]> conceptosRecibo,
            Concepto concepto,
            TipoCoeficiente tipoCoeficiente,
            Integer mesesAtrasos,
            Date fechaRepercusion,
            Double importeRepercusion
    ){
        this.ubicacion = ubicacion;

        this.conceptosRecibo = conceptosRecibo;
        this.concepto = concepto;
        this.tipoCoeficienteAplicado = tipoCoeficiente;
        this.mesesAtrasos = mesesAtrasos;
        this.fechaRepercusion = fechaRepercusion;
        this.importeRepercutir = importeRepercusion;

    }


    public byte[] getReportAsByteArray(){

        try {
//            String basePath = SIApplication.getCurrent().getSession().getService().getBaseDirectory().getAbsolutePath();
//
//            String pathMaestro = basePath + this.pathReport;
//            JasperDesign designMaestro = JRXmlLoader.load(new File(pathMaestro));
//            JasperReport reportMaestro = JasperCompileManager.compileReport(designMaestro);

            Object  s = Resources.getResource("/com/company/test1/service/accessory/" + this.pathReport).getContent();
            JasperDesign designMaestro = JRXmlLoader.load((InputStream)s);
            JasperReport reportMaestro = JasperCompileManager.compileReport(designMaestro);

            List<Departamento> deptos = ubicacion.getDepartamentos();
            ArrayList l = new ArrayList();
            Double sumaCoeficientes = 0.0;
            Double totalImporteAnual = 0.0;
            Double totalImporteAumento = 0.0;
            Double totalImporteAtrasos = 0.0;

            Double totalCoeficiente = totalValorCoeficienteUbicacion(ubicacion, tipoCoeficienteAplicado);

            for (int i = 0; i < deptos.size(); i++) {
                Departamento departamento = deptos.get(i);
                ConceptoRecibo[] cr = conceptosRecibo.get(departamento);
                int estado;
                Coeficiente c = coeficienteParaDepartamento(departamento, tipoCoeficienteAplicado);
                if (c==null){
                    throw new Exception("No se hallo coeficiente para departamento:" + departamento.getNombreDescriptivoCompleto());
                }
                ContratoInquilino ci = AppBeans.get(ContratosService.class).devuelveContratoVigenteParaDepartamento(departamento);
                boolean tieneContratoVigente = ci!=null;
                if (!tieneContratoVigente){
                    estado= HlpConceptoRecibo.ESTADO_VACIO;
                }else{
                    if (cr==null){
                        estado = HlpConceptoRecibo.ESTADO_NO_APLICA;
                    }else{
                        estado = HlpConceptoRecibo.ESTADO_NORMAL;
                        sumaCoeficientes += c.getValor();
                        totalImporteAnual += cr[0].getImporte() * 12;
                        totalImporteAumento += cr[0].getImporte();
                        if (cr.length==2){
                            totalImporteAtrasos += cr[1].getImporte();
                        }
                    }
                }


                HlpConceptoRecibo hlpCR;
                try {
                    if (cr != null) {
                        hlpCR = new HlpConceptoRecibo(cr[0], estado, (i + 1), c, mesesAtrasos);
                    } else {
                        hlpCR = new HlpConceptoRecibo(null, estado, (i + 1), c, mesesAtrasos);
                    }

                    l.add(hlpCR);
                } catch (Exception exception) {
                    int y = 2;
                }

            }

            SIJRBeanDataSource mainDs = new SIJRBeanDataSource(l);

            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            DateFormat dfHora = new SimpleDateFormat("hh:MM");
            NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.GERMANY);





            Hashtable pams = new Hashtable();
            pams.put("tituloConcepto",concepto.getTituloConcepto() + "(" + concepto.getAbreviacion() + ")");
            pams.put("fechaRepercusion",df.format(fechaRepercusion));
            pams.put("ubicacion",ubicacion.getNombre());
            pams.put("totalCoeficiente",totalCoeficiente.toString());
            pams.put("importeRepercutir",nf.format(importeRepercutir));
            pams.put("sumaCoeficientes", sumaCoeficientes.toString());
            pams.put("totalImporteAnual",nf.format(totalImporteAnual));
            pams.put("totalImporteAumento",nf.format(totalImporteAumento));
            pams.put("totalImporteAtrasos",nf.format(totalImporteAtrasos));
            pams.put("P_FECHA",df.format(new Date()));
            pams.put("P_HORA",dfHora.format(new Date()));

            String nombre = "PENDIENTE AIMPLEMENTAR";
            pams.put("P_INFORME_REALIZADO_POR", nombre);

//            JRRenderable jrr = ReportingUtilities.obtenerJRRenderablePorEntornoOPropietario(null, SIApplication.getCurrent().getEntornosPreseleccionados(), RecursoEntornoPropietario.NCABECERA_DOCS, SIApplication.getCurrent().getCurrentProcess().getSessionLayer(), SIApplication.getCurrent().getCurrentProcess().getSessionLayerExtDocs());
            JRRenderable jrr = (JRRenderable) AppBeans.get(JasperReportService.class).turnFileIntoJRRenderableObject("LogoGuadalest.jpg");
            pams.put("P_IMAGEN",jrr);


            byte[] bytearr = JasperRunManager.runReportToPdf(reportMaestro, pams,mainDs);
            return bytearr;


        } catch (Exception jRException) {
            int y = 2;
        }
        return null;

    }

    private Double totalValorCoeficienteUbicacion(Ubicacion u, TipoCoeficiente tc) throws Exception{
        List<UbicacionCoeficiente> uucc = u.getUbicacionesCoeficientes();
        for (int i = 0; i < uucc.size(); i++) {
            UbicacionCoeficiente ubicacionCoeficiente =  uucc.get(i);
            if (ubicacionCoeficiente.getTipoCoeficiente().getId().compareTo(tc.getId())==0){
                return ubicacionCoeficiente.getTotalTipoCoeficiente();
            }
        }
        throw new Exception("Total Coeficiente Ubicacion no Definifido");
    }

    private Coeficiente coeficienteParaDepartamento(Departamento d, TipoCoeficiente tc) throws Exception{
        List<Coeficiente> cc = d.getCoeficientes();
        for (int i = 0; i < cc.size(); i++) {
            Coeficiente c =  cc.get(i);
            if (c.getTipoCoeficiente().getId().compareTo(tc.getId())==0){
                return c;
            }
        }
        return null;
    }




}
