package com.company.test1.service.accessory;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import com.company.test1.entity.CuentaBancaria;
import com.company.test1.entity.ordenescobro.OrdenCobro;
import com.company.test1.service.OrdenCobroService;
import com.haulmont.cuba.core.global.AppBeans;

import java.text.NumberFormat;


/**
 *
 * @author Xavier
 */
public class HlpOrdenCobro {

    OrdenCobro ordenCobro;

    public HlpOrdenCobro(OrdenCobro ordenCobro){
        this.ordenCobro = ordenCobro;
    }


    public String getTipoAbreviado(){
        return "OCR";
    }

    public String getDato(){
        return ordenCobro.getDato();
    }

    public String getImporteEfectivo(){

        NumberFormat nf = NumberFormat.getCurrencyInstance();
        return nf.format(ordenCobro.getImporte());
    }

    public String getCuentaDestino(){

        CuentaBancaria cb = AppBeans.get(OrdenCobroService.class).getCuentaBancariaDebitora(ordenCobro);

        if (cb == null) return "";

        return cb.getVersionIBAN() /*+ "-"+ cb.getVersionCuentaBancariaCompleta()*/;
    }

}