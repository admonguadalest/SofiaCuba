package com.company.test1.service.accessory;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import com.company.test1.entity.CuentaBancaria;
import com.company.test1.entity.ordenespago.OrdenPago;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;

import java.text.NumberFormat;


/**
 *
 * @author Xavier
 */
public class HlpOrdenPago {

    OrdenPago ordenPago;

    public HlpOrdenPago(OrdenPago ordenPago){
        this.ordenPago = ordenPago;
    }

    public String getTipoAbreviado(){
        return ordenPago.getNombreTipo();
    }

    public String getDato(){
        return ordenPago.getDato();
    }

    public String getImporteEfectivo(){
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        return nf.format(ordenPago.getImporteEfectivo());
    }

    public String getCuentaDestino(){

        CuentaBancaria cb =  ordenPago.getCuentaBancariaOrdenPago();
        cb = (CuentaBancaria) AppBeans.get(DataManager.class).reload(cb, "cuentaBancaria-view");
        return cb.getVersionIBAN();
    }
}