package com.company.test1.service.accessory;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import com.company.test1.entity.departamentos.Ubicacion;
import com.company.test1.entity.enums.recibos.DefinicionRemesaTipoGiroEnum;
import com.company.test1.entity.recibos.Recibo;
import com.company.test1.service.RecibosService;
import com.haulmont.cuba.core.global.AppBeans;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;


/**
 *
 * @author Carlos Conti
 */
public class HlpFinca {

    Ubicacion ubicacion;
    List<Recibo> recibos;

    Double totalDevuelto = 0.0;
    Double totalPendiente = 0.0;


    SIJRBeanDataSource dataSourceRecibos;

    RecibosService recibosService;

    public void setRecibosService(RecibosService recibosService) {
        this.recibosService = recibosService;
    }

    public HlpFinca(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }


    public String getNombreFinca() {
        return ubicacion.getNombre();
    }



    public String getTotalFinca() {

        double totalFinca = getTotalPendienteNumero() + getTotalDevueltoNumero();

        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.GERMANY);
        String totalFincaNombre = nf.format(totalFinca);

        return totalFincaNombre;
    }



    public SIJRBeanDataSource getDataSourceRecibosFinca() {
        return dataSourceRecibos;
    }

    public void asignarHelpers(List<HlpInquilino> l){
        dataSourceRecibos = new SIJRBeanDataSource(l);
    }



    public String getTotalDevuelto() {
        double devuelto = getTotalDevueltoNumero();

        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.GERMANY);
        String devueltoNombre = nf.format(devuelto);

        return devueltoNombre;
    }



    public String getTotalPendiente() {
        double pendiente = getTotalPendienteNumero();

        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.GERMANY);
        String pendienteNombre = nf.format(pendiente);

        return pendienteNombre;
    }



    public Double getTotalDevueltoNumero() {

        return totalDevuelto;
    }



    public Double getTotalPendienteNumero() {

        return totalPendiente;
    }


    public void anadirReciboATotalesUbicacion(Recibo r){
        if (r.getOrdenanteRemesa()==null){
            totalPendiente += AppBeans.get(RecibosService.class).getTotalPendiente(r);
            return;
        }
        if (r.getOrdenanteRemesa().getRemesa().getDefinicionRemesa().getTipoGiro()== DefinicionRemesaTipoGiroEnum.BANCARIA){
            totalDevuelto += AppBeans.get(RecibosService.class).getTotalPendiente(r);
        } else {
            totalPendiente += AppBeans.get(RecibosService.class).getTotalPendiente(r);
        }
    }
}