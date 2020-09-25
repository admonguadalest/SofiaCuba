package com.company.test1.service.accessory;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import com.company.test1.entity.coeficientes.Coeficiente;
import com.company.test1.entity.recibos.ConceptoRecibo;
import com.company.test1.service.RecibosService;
import com.haulmont.cuba.core.global.AppBeans;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;


/**
 *
 * @author Xavier
 */
public class HlpConceptoRecibo {


    public static final int ESTADO_NORMAL = 0;
    public static final int ESTADO_VACIO = 1;
    public static final int ESTADO_NO_APLICA = 2;

    ConceptoRecibo conceptoRecibo = null;
    int estado = ESTADO_NORMAL;

    String id = "";
    String titular = "";
    String fechaContrato = "";

    String importeAnual = "";
    String importeActual = "";
    String coeficiente = "";
    String importeAumento = "";
    String importeRecibo = "";
    String mesesAtraso = "";
    String importeAtrasos = "";
    String pisoPuerta = "";



    public HlpConceptoRecibo(ConceptoRecibo cr, int estado, int contador, Coeficiente coef, Integer mesesAtrasos)
            throws Exception{
        this.conceptoRecibo = cr;
        this.estado = estado;

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.GERMANY);
        if (estado == ESTADO_NORMAL){
            id = new Integer(contador).toString();
            titular = cr.getProgramacionRecibo().getContratoInquilino().getInquilino().getNombreCompleto();
            fechaContrato = df.format(cr.getProgramacionRecibo().getContratoInquilino().getFechaRealizacion());

            Double totalesConcepto = AppBeans.get(RecibosService.class).getTotalesConceptoVigente(cr.getProgramacionRecibo(), cr.getConcepto());

            importeActual = nf.format(totalesConcepto);
            coeficiente = coef.getValor().toString();
            Double dblImporteAnual = cr.getImporte() * 12;
            importeAnual = nf.format(dblImporteAnual);
            importeAumento = nf.format(cr.getImporte());
            importeRecibo = nf.format(totalesConcepto + cr.getImporte());
            if (mesesAtrasos!=null){
                mesesAtraso = mesesAtrasos.toString();
                importeAtrasos = nf.format(mesesAtrasos * cr.getImporte());
            }else{
                mesesAtraso="";
                importeAtrasos = "0.00";
            }


        }else if(estado==ESTADO_VACIO){
            titular = "DEPTO.VACIO";
        }else if(estado==ESTADO_NO_APLICA){
            titular = "NO APLICADO";
        }
        pisoPuerta = cr.getProgramacionRecibo().getContratoInquilino().getDepartamento().getAbreviacionPisoPuerta();
    }

    /*--------------------------------------------------------------------------*/

    public String getCoeficiente() {
        return coeficiente;
    }

    public ConceptoRecibo getConceptoRecibo() {
        return conceptoRecibo;
    }

    public int getEstado() {
        return estado;
    }

    public String getFechaContrato() {
        return fechaContrato;
    }

    public String getId() {
        return id;
    }

    public String getPisoPuerta() {
        return pisoPuerta;
    }



    public String getImporteActual() {
        return importeActual;
    }

    public String getImporteAnual() {
        return importeAnual;
    }

    public String getImporteAtrasos() {
        return importeAtrasos;
    }

    public String getImporteAumento() {
        return importeAumento;
    }

    public String getImporteRecibo() {
        return importeRecibo;
    }

    public String getMesesAtraso() {
        return mesesAtraso;
    }

    public String getTitular() {
        return titular;
    }


}