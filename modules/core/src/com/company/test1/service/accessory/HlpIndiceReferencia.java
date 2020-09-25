package com.company.test1.service.accessory;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import com.company.test1.entity.contratosinquilinos.ContratoInquilino;
import com.company.test1.entity.incrementos.IncrementoIndice;
import com.company.test1.entity.recibos.ConceptoRecibo;
import com.company.test1.service.IncrementosService;
import com.company.test1.service.NumberUtilsService;
import com.haulmont.cuba.core.global.AppBeans;
import com.sun.star.bridge.oleautomation.Decimal;


/**
 *
 * @author Xavier
 */
public class HlpIndiceReferencia {

    String idFinca="";
    String direccion="";
    String departamento="";
    String nombreInquilino="";
    String proximaRevision="";
    String fechaContrato="";
    String renta="";
    String incremento="";
    String baseAumento="";
    String porcentaje="";
    String importeAumento="";
    String importeAumentado="";
    String mesesAtrasos="";
    String importeAtrasos="";
    String indice="";
    String indiceAnterior="";

    ConceptoRecibo cr=null;

    public HlpIndiceReferencia(ConceptoRecibo cr,Integer mesesAtrasos, Integer mes, Integer anno){
        this.cr=cr;

        IncrementoIndice ii = (IncrementoIndice) cr.getIncremento();

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        NumberFormat nf = NumberFormat.getCurrencyInstance();

        ContratoInquilino contratoInquilino = cr.getProgramacionRecibo().getContratoInquilino();

        //calculo incremento
        String nombreTipoIndice = contratoInquilino.getNombreTipoIndiceIncrementos();
        double indiceAnteriorNumero = 0.0;
        double indiceActualNumero = 0.0;
        try{

            indiceAnteriorNumero = ii.getIndiceAnterior();
            indiceActualNumero = ii.getValorIndice();
        }catch(Exception exc){

        }
        double incrementoPorcentual = ii.getValorIndicePorcentual();

        this.baseAumento = nf.format(ii.getValorBase());

        this.cr = cr;
        this.departamento = contratoInquilino.getDepartamento().getPiso() + " " + contratoInquilino.getDepartamento().getPuerta();
        this.direccion = contratoInquilino.getDepartamento().getNombreDescriptivoCompleto();
        this.fechaContrato = df.format(contratoInquilino.getFechaRealizacion());
        this.idFinca = contratoInquilino.getDepartamento().getUbicacion().getRm2id().toString();

        //importeAtrasos
        double importeAtrasosNumero = ii.getImporteAtrasos();
        this.importeAtrasos = nf.format(importeAtrasosNumero);

        double importeAumentoNumero = ii.getImporte();
        this.importeAumentado = nf.format(ii.getValorBase() + importeAumentoNumero);
        this.importeAumento = nf.format(importeAumentoNumero);


        this.indice = new Double(indiceActualNumero).toString();
        this.indiceAnterior = new Double(indiceAnteriorNumero).toString();
        this.mesesAtrasos = mesesAtrasos.toString();
        this.nombreInquilino= cr.getProgramacionRecibo().getContratoInquilino().getInquilino().getNombreCompleto();
        NumberFormat percentInstance = NumberFormat.getPercentInstance();
        percentInstance.setMinimumFractionDigits(3);
        percentInstance.setMaximumFractionDigits(3);

        if (cr.getImporte()==0.0){
            incrementoPorcentual = 0.0;
        }
        this.porcentaje = percentInstance.format(incrementoPorcentual);

        //proximaRevision
        try{
            this.proximaRevision = AppBeans.get(IncrementosService.class).calculaProximoValorMesAnyoAplicacionIPC(contratoInquilino);
        }catch(Exception exc){
            this.proximaRevision = "ERROR";
        }

        double renta = 0.0;
        double incrementos = 0;

        List<ConceptoRecibo> ccrr_actualizables = AppBeans.get(IncrementosService.class).getConceptosReciboActualizablesIPC(contratoInquilino.getProgramacionRecibo());
        for (int i = 0; i < ccrr_actualizables.size(); i++) {
            ConceptoRecibo conceptoRecibo = ccrr_actualizables.get(i);
            if (conceptoRecibo.getConcepto().getAbreviacion().compareTo("RENT")==0){
                renta += conceptoRecibo.getImporte();
            }
            if (conceptoRecibo.getConcepto().getAbreviacion().compareTo("IRTA")==0){
                incrementos += conceptoRecibo.getImporte();
            }
        }
        renta = AppBeans.get(NumberUtilsService.class).roundToNDecimals(renta, 2.0);
        incrementos = AppBeans.get(NumberUtilsService.class).roundToNDecimals(incrementos, 2.0);

        this.renta = nf.format(renta);
        this.incremento = nf.format(incrementos);
    }



    public String getBaseAumento() {
        return baseAumento;
    }

    public String getDepartamento() {
        return departamento;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getFechaContrato() {
        return fechaContrato;
    }

    public String getIdFinca() {
        return idFinca;
    }

    public String getImporteAtrasos() {
        return importeAtrasos;
    }

    public String getImporteAumentado() {
        return importeAumentado;
    }

    public String getImporteAumento() {
        return importeAumento;
    }

    public String getIncremento() {
        return incremento;
    }

    public String getIndice() {
        return indice;
    }

    public String getIndiceAnterior() {
        return indiceAnterior;
    }

    public String getMesesAtrasos() {
        return mesesAtrasos;
    }

    public String getNombreInquilino() {
        return nombreInquilino;
    }

    public String getPorcentaje() {
        return porcentaje;
    }

    public String getProximaRevision() {
        return proximaRevision;
    }

    public String getRenta() {
        return renta;
    }

}