package com.company.test1.service.accessory;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */




import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.company.test1.entity.contratosinquilinos.ContratoInquilino;
import com.company.test1.entity.recibos.Concepto;
import com.company.test1.entity.recibos.ConceptoRecibo;
import com.company.test1.entity.recibos.RegistroIndiceReferencia;
import com.company.test1.service.IncrementosService;
import com.company.test1.service.RecibosService;
import com.haulmont.cuba.core.global.AppBeans;
import org.apache.commons.lang.time.DateUtils;

/**
 *
 * @author Carlos Conti
 */
public class HlpListadoAumentosIPC2 {

    ConceptoRecibo conceptoRecibo = null;
    boolean previsualizacion = false;


    public HlpListadoAumentosIPC2(ConceptoRecibo cr, boolean previsualizacion) {
        this.conceptoRecibo = cr;
        this.previsualizacion = previsualizacion;

    }

    public Double getBaseRenta() throws Exception{
        ContratoInquilino c = conceptoRecibo.getProgramacionRecibo().getContratoInquilino();
//        ConceptoBean renta = ConceptoBean.getConceptoDesdeAbreviacion("RENT", c.getDepartamento().getEntornos().get(0), sessionLayer);
        Concepto renta = AppBeans.get(RecibosService.class).getConceptoDesdeAbreviacion("RENT");
        double vrenta = AppBeans.get(RecibosService.class).getTotalesConceptoVigente(conceptoRecibo.getProgramacionRecibo(), renta);
        return vrenta;
    }

    public Double getBaseIncrementosRenta() throws Exception{
        ContratoInquilino c = conceptoRecibo.getProgramacionRecibo().getContratoInquilino();
        Concepto irta = AppBeans.get(RecibosService.class).getConceptoDesdeAbreviacion("IRTA");
        double virenta = AppBeans.get(RecibosService.class).getTotalesConceptoVigente(conceptoRecibo.getProgramacionRecibo(), irta);
        return virenta;
    }

    public Double getBaseAumento() throws Exception{
        double renta = getBaseRenta();
        double irenta = getBaseIncrementosRenta();
        return renta + irenta;
    }

    public Double getPorcentajeIncremento() throws Exception{
        double rentatotal = getBaseAumento();
        double perc = conceptoRecibo.getImporte() / rentatotal;
        return perc;
    }

    public Double getImporteAumento() throws Exception{
        double perc = getPorcentajeIncremento();
        double renta = getBaseAumento();
        double importe = perc * renta;
        return importe;
    }

    public Double getImporteAumentado() throws Exception{
        double renta = getBaseAumento();
        double ia = getImporteAumento();
        double iaumentado = renta + ia;
        return iaumentado;
    }

    public String getMesAnyoAplicacionIPC(){
        ContratoInquilino c = conceptoRecibo.getProgramacionRecibo().getContratoInquilino();
        int mesesEntreRevision = c.getPeriodoActualizacionIPC();
        Date fechaProxRevision = null;
        try{
            fechaProxRevision = new SimpleDateFormat("MMyyyy").parse(c.getMesAnyoAplicacionIPC());
        }catch(Exception exc){

        }
        Date fechaAnterior = fechaProxRevision;
        if (!previsualizacion){
            fechaAnterior = DateUtils.addMonths(fechaProxRevision, -mesesEntreRevision);
        }

        String far = new SimpleDateFormat("MMyyyy").format(fechaAnterior);
        return far;


    }

    public double getIndiceIPCAplicado() throws Exception{
        ContratoInquilino c = conceptoRecibo.getProgramacionRecibo().getContratoInquilino();
        int mesesEntreRevision = c.getPeriodoActualizacionIPC();
        Date fechaProxRevision = null;
        try{
            fechaProxRevision = new SimpleDateFormat("MMyyyy").parse(c.getMesAnyoAplicacionIPC());
        }catch(Exception exc){

        }
        Date fechaAnterior = DateUtils.addMonths(fechaProxRevision, -mesesEntreRevision);
        Calendar cal = Calendar.getInstance();
        cal.setTime(fechaAnterior);
        Integer mes = cal.get(Calendar.MONTH);
        Integer anno = cal.get(Calendar.YEAR);

        if (previsualizacion){
            int add = mesesEntreRevision / 12;
            anno += add;
        }

        RegistroIndiceReferencia ipc = AppBeans.get(IncrementosService.class).getRegistroForValues(mes, anno, "IPC");
        return ipc.getValor();
    }

    public double getIndiceIPCAnterior() throws Exception{
        ContratoInquilino c = conceptoRecibo.getProgramacionRecibo().getContratoInquilino();
        int mesesEntreRevision = c.getPeriodoActualizacionIPC();
        Date fechaProxRevision = null;
        try{
            fechaProxRevision = new SimpleDateFormat("MMyyyy").parse(c.getMesAnyoAplicacionIPC());
        }catch(Exception exc){

        }
        Date fechaAnterior = DateUtils.addMonths(fechaProxRevision, -mesesEntreRevision * 2);
        Calendar cal = Calendar.getInstance();
        cal.setTime(fechaAnterior);
        Integer mes = cal.get(Calendar.MONTH);
        Integer anno = cal.get(Calendar.YEAR);

        if (previsualizacion){
            int add = mesesEntreRevision / 12;
            anno += add;
        }

        RegistroIndiceReferencia ipc = AppBeans.get(IncrementosService.class).getRegistroForValues(mes, anno, "IPC");
        return ipc.getValor();
    }

    public String getMesAnyoAplicacionIPCAnterior(){
        ContratoInquilino c = conceptoRecibo.getProgramacionRecibo().getContratoInquilino();
        int mesesEntreRevision = c.getPeriodoActualizacionIPC();
        Date fechaProxRevision = null;
        try{
            fechaProxRevision = new SimpleDateFormat("MMyyyy").parse(c.getMesAnyoAplicacionIPC());
        }catch(Exception exc){

        }
        if (previsualizacion){
            fechaProxRevision = DateUtils.addMonths(fechaProxRevision, mesesEntreRevision);
        }
        Date fechaAnterior = DateUtils.addMonths(fechaProxRevision, -mesesEntreRevision * 2);
        String far = new SimpleDateFormat("MMyyyy").format(fechaAnterior);
        return far;


    }






}
