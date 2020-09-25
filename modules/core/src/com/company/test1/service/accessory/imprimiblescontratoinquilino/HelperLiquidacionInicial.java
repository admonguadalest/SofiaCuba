package com.company.test1.service.accessory.imprimiblescontratoinquilino;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import com.company.test1.entity.contratosinquilinos.LiquidacionSuscripcion;

import java.text.DecimalFormat;


/**
 *
 * @author Xavier
 */
public class HelperLiquidacionInicial {


    LiquidacionSuscripcion li;
    DecimalFormat df = new DecimalFormat("#,##0.00");

    public HelperLiquidacionInicial(LiquidacionSuscripcion li) {
        this.li = li;
    }


    public String getFianzaContrato(){

        if (li.getFianzaContrato() == null) return df.format(0.0);

        return df.format(li.getFianzaContrato());
    }

    public String getGarantiasEnEfectivo(){

        if (li.getGarantiasEnEfectivo() == null) return df.format(0.0);

        return df.format(li.getGarantiasEnEfectivo());
    }

    public String getOtrosConceptos(){

        if (li.getOtrosConceptos() == null) return df.format(0.0);

        return df.format(li.getOtrosConceptos());
    }


    public String getTotalInicio(){
        Double total = 0.0;

        if (li.getFianzaContrato() != null){
            total += li.getFianzaContrato();
        }

        if (li.getGarantiasEnEfectivo() != null) {
            total += li.getGarantiasEnEfectivo();

        }

        if (li.getOtrosConceptos() != null){
            total += li.getOtrosConceptos();

        }

        return df.format(total);
    }


    public String getRecibosACuenta(){

        if (li.getRecibosACuenta() == null) return "";
        if (li.getRecibosACuenta() == 0.0) return "";

        return df.format(li.getRecibosACuenta());
    }

    public String getCantidadesEntregadasEnReserva(){

        if (li.getCantidadesEntregadasEnReserva() == null) return "";
        if (li.getCantidadesEntregadasEnReserva() == 0.0) return "";

        return df.format(-li.getCantidadesEntregadasEnReserva());
    }

    public String getGastosContrato(){

        if (li.getGastosContrato() == null) return "";
        if (li.getGastosContrato() == 0.0) return "";

        return df.format(li.getGastosContrato());
    }

    public String getTotalLiquidacion(){

        if (li.getTotalLiquidacion() == null) return df.format(0.0);

        return df.format(li.getTotalLiquidacion());
    }

    public String getDetalle(){

        if (li.getDetalle() == null) return "";
        return li.getDetalle();
    }

    public String getReservaNombre(){
        if (getCantidadesEntregadasEnReserva().equals("")) return "";

        return "RESERVA";
    }

}