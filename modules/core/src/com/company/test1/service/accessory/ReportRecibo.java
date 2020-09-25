package com.company.test1.service.accessory;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import com.company.test1.entity.Direccion;
import com.company.test1.entity.conceptosadicionales.RegistroAplicacionConceptoAdicional;
import com.company.test1.entity.departamentos.Departamento;
import com.company.test1.entity.recibos.ImplementacionConcepto;
import com.company.test1.entity.recibos.Recibo;
import com.company.test1.entity.recibos.accesorios.ImplementacionConceptoOrdenacionComparator;
import com.company.test1.service.JasperReportService;
import com.google.common.io.Resources;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.AppBeans;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;




/**
 *
 * @author Carlos Conti
 */
public class ReportRecibo {



    SIJRBeanDataSource sijr = new SIJRBeanDataSource(new ArrayList());
//    String pathMaestro = "\\jasperreports\\listadorecibos\\recibosIndividualizados.jrxml";
    // String pathMaestro = "/jasperreports/listadorecibos/recibosIndividualizados.jrxml";
    String pathMaestro = "reciboPrevisualizacion.jrxml";

    Recibo recibo = null;

    public ReportRecibo(Recibo recibo){


        this.recibo = recibo;

    }

    public byte[] getReportAsByteArray() throws Exception{
        try{



            Object  s = Resources.getResource("/com/company/test1/service/accessory/" + this.pathMaestro).getContent();
            JasperDesign designMaestro = JRXmlLoader.load((InputStream)s);
            JasperReport reportMaestro = JasperCompileManager.compileReport(designMaestro);

            Hashtable parameters = new Hashtable<String,Object>();
            parameters.put("P_TITULO", "");
            Helper h = new Helper(this.recibo);
            parameters.put("helperBean", h);
            parameters.put("nombreEntidad", "VALOR NOMBRE ENTIDAD");

            JRRenderable jrr = (JRRenderable) AppBeans.get(JasperReportService.class).turnFileIntoJRRenderableObject("Recibo.svg");
            parameters.put("FONDO_RECIBO",jrr);

            JRDataSource mainds = new SIJRBeanDataSource(Arrays.asList(h));

            byte[] bytearr = JasperRunManager.runReportToPdf(reportMaestro, parameters,mainds);
            return bytearr;
        }catch(Exception exc){
            exc.printStackTrace();
            throw exc;
        }

    }

    public class Helper{

        Recibo r = null;

        String did = null;
        String numRecibo = null;
        Double totalRecibo = null;
        Date fechaEmision = null;
        String poblacionExpedicion = null;
        String direccionEntornoNombre = null;
        String vencimiento = null;
        String referenciaCatastral = null;
        String nifDniInquilino = null;
        String dirprop1 = null;
        String dirprop2 = null;
        String nifDniPropietario = null;
        String nombreCompleto = null;
        String nombreCompletoPropietario = null;
        Recibo recibo = null;
        String nombre50 = null;
        String dirinquilino1 = null;
        String dirinquilino2 = null;
        Double IRPF = null;
        Double IVA = null;
        Double totalReciboPostCCAA = null;
        String cuentaIban = null;

        List<ImplementacionConcepto> iccAgregados = null;



        Helper(Recibo r){
            this.r = r;
            this.recibo = r;

            Departamento d = r.getUtilitarioContratoInquilino().getDepartamento();

            Transaction t = AppBeans.get(Persistence.class).createTransaction();
            this.r = AppBeans.get(Persistence.class).getEntityManager().reload(this.r, "recibo-report-view");
            t.close();

            this.did = d.getId().toString();
            this.numRecibo = r.getNumRecibo();
            this.totalRecibo = r.getTotalRecibo();
            this.fechaEmision = r.getFechaEmision();
            this.poblacionExpedicion = r.getUtilitarioContratoInquilino().getLugarRealizacion();
            this.direccionEntornoNombre = "PENDIENTE";
            if (r.getUtilitarioContratoInquilino().getFechaVencimientoPrevisto()!=null){
                this.vencimiento = new SimpleDateFormat("dd/MM/yyyy").format(r.getUtilitarioContratoInquilino().getFechaVencimientoPrevisto());
            }else{
                this.vencimiento = "";
            }

            String rc = "";
            if (d.getReferenciaCatastral()!=null){
                rc = d.getReferenciaCatastral();
            }else{
                rc = d.getUbicacion().getInformacionCatastral();
            }
            this.referenciaCatastral = rc;
            this.nifDniInquilino = r.getUtilitarioContratoInquilino().getInquilino().getNifDni();
            Direccion dirPropietario = d.getPropietarioEfectivo().getPersona().getDirecciones().get(0);//pendiente ampliar a tipologias de direccion
            this.dirprop1 = dirPropietario.getNombreVia() + " " + dirPropietario.getNumeroVia() + " " + dirPropietario.getPiso() + " " + dirPropietario.getPuerta();
            this.dirprop2 = dirPropietario.getCodigoPostal() + " " + dirPropietario.getPoblacion() + " (" + dirPropietario.getProvincia() + "-" + dirPropietario.getPais() + ")";
            this.nifDniPropietario = d.getPropietarioEfectivo().getPersona().getNifDni();
            this.nombreCompleto = r.getUtilitarioContratoInquilino().getInquilino().getNombreCompleto();
            this.nombreCompletoPropietario = d.getPropietarioEfectivo().getPersona().getNombreCompleto();
            this.nombre50 = "LUGAR DE PAGO";//pendiente solventar

            Direccion dirInquilino = r.getUtilitarioContratoInquilino().getInquilino().getDirecciones().get(0);//pendiente ampliar a tipologias de direccion
            this.dirinquilino1 = dirInquilino.getNombreVia() + " " + dirInquilino.getNumeroVia() + " " + dirInquilino.getPiso() + " " + dirInquilino.getPuerta();
            this.dirinquilino2 = dirInquilino.getCodigoPostal() + " " + dirInquilino.getPoblacion() + " (" + dirInquilino.getProvincia() + "-" + dirInquilino.getPais() + ")";

            this.IRPF = 0.0;
            this.IVA = 0.0;
            List<ImplementacionConcepto> iicc = recibo.getImplementacionesConceptos();
            for (int i = 0; i < iicc.size(); i++) {
                ImplementacionConcepto ic =  iicc.get(i);
                List<RegistroAplicacionConceptoAdicional> racal = ic.getRegistroAplicacionesConceptosAdicionales();
                for (int j = 0; j < racal.size(); j++) {
                    RegistroAplicacionConceptoAdicional raca =  racal.get(j);
                    if (raca.getConceptoAdicional().getAbreviacion().compareTo("IVA")==0){
                        this.IVA += raca.getImporteAplicado();
                    }
                    if (raca.getConceptoAdicional().getAbreviacion().compareTo("IRPF")==0){
                        this.IRPF += raca.getImporteAplicado();
                    }

                }

            }


            this.totalReciboPostCCAA = r.getTotalReciboPostCCAA();
            this.cuentaIban = "TEXTO CUENTA IBAN";//pendinte

            getImplementacionesConceptosAgregadas();

        }



        public List<ImplementacionConcepto> getImplementacionesConceptosAgregadas(){

            if (iccAgregados!=null) return iccAgregados;

            List<ImplementacionConcepto> iicc = r.getImplementacionesConceptos();
            iccAgregados =  new ArrayList<ImplementacionConcepto>();

            boolean yaEstabaIncluido;

            for (int i = 0; i < iicc.size(); i++) {
                ImplementacionConcepto implementacionConcepto = iicc.get(i);
                yaEstabaIncluido = false;

                for (int j = 0; j < iccAgregados.size(); j++) {
                    ImplementacionConcepto ic = iccAgregados.get(j);
                    if (implementacionConcepto.getConcepto().getId() == ic.getConcepto().getId()){
                        yaEstabaIncluido=true;
                        ic.setImporte(ic.getImporte() + implementacionConcepto.getImporte());
                        ic.setImportePostCCAA(ic.getImportePostCCAA() + implementacionConcepto.getImportePostCCAA());
                        break;
                    }
                }

                if (yaEstabaIncluido==false){
                    ImplementacionConcepto ic = new ImplementacionConcepto();
                    ic.setOverrideConcepto(implementacionConcepto.getConcepto());
                    ic.setImporte(implementacionConcepto.getImporte());
                    ic.setImportePostCCAA(implementacionConcepto.getImportePostCCAA());
                    iccAgregados.add(ic);
                }
            }

            Collections.sort(iccAgregados,new ImplementacionConceptoOrdenacionComparator());
            return iccAgregados;
        }

        public Recibo getR() {
            return r;
        }

        public String getDid() {
            return did;
        }

        public String getNumRecibo() {
            return numRecibo;
        }

        public Double getTotalRecibo() {
            return totalRecibo;
        }

        public Date getFechaEmision() {
            return fechaEmision;
        }

        public String getPoblacionExpedicion() {
            return poblacionExpedicion;
        }

        public String getDireccionEntornoNombre() {
            return direccionEntornoNombre;
        }

        public String getVencimiento() {
            return vencimiento;
        }

        public String getReferenciaCatastral() {
            return referenciaCatastral;
        }

        public String getNifDniInquilino() {
            return nifDniInquilino;
        }

        public String getDirprop1() {
            return dirprop1;
        }

        public String getDirprop2() {
            return dirprop2;
        }

        public String getNifDniPropietario() {
            return nifDniPropietario;
        }

        public String getNombreCompleto() {
            return nombreCompleto;
        }

        public String getNombreCompletoPropietario() {
            return nombreCompletoPropietario;
        }

        public Recibo getRecibo() {
            return recibo;
        }

        public String getNombre50() {
            return nombre50;
        }

        public String getDirinquilino1() {
            return dirinquilino1;
        }

        public String getDirinquilino2() {
            return dirinquilino2;
        }

        public Double getIRPF() {
            return IRPF;
        }

        public Double getIVA() {
            return IVA;
        }

        public Double getTotalReciboPostCCAA() {
            return totalReciboPostCCAA;
        }

        public String getCuentaIban() {
            return cuentaIban;
        }
    }
}