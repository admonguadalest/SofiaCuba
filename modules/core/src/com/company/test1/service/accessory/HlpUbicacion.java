package com.company.test1.service.accessory;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import com.company.test1.entity.contratosinquilinos.Fianza;
import com.company.test1.entity.departamentos.Ubicacion;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;



/**
 *
 * @author Xavier
 */
public class HlpUbicacion {

    Ubicacion ub;
    List<Fianza> fianzas;

    List<HlpFianza> helpers = new ArrayList();

    public static Integer TIPO_FIANZA_AVAL = 1;
    public static Integer TIPO_FIANZA_EFECTIVO = 0;
    public static Integer TIPO_FIANZA_POLIZA = 2;

    public static Integer ESTADO_FIANZA_NO_INGRESADA_EN_ADMINISTRACION = 0;
    public static Integer ESTADO_FIANZA_EN_ADMINISTRACION = 1;
    public static Integer ESTADO_FIANZA_EN_CAMARA = 2;
    public static Integer ESTADO_FIANZA_SOLICITADA_DEVOLUCION = 3;
    public static Integer ESTADO_FIANZA_DEVUELTA = 4;


    Double noIngresadaEnAdmon = 0.0;
    Double fianzaAdmon = 0.0;
    Double fianzaCamara = 0.0;
    Double solicitadaDevolucion = 0.0;
    Double fianzaDevuelta = 0.0;

    Double garantiaAval = 0.0;
    Double garantiaEfectivo = 0.0;



    public HlpUbicacion(Ubicacion ub, List<Fianza> fianzas){
        this.ub = ub;
        this.fianzas = fianzas;
    }

    public void doParteFianzas(){

        for (int i = 0; i < fianzas.size(); i++) {
            Fianza fianza = fianzas.get(i);
            HlpFianza hlpFianza = new HlpFianza(fianza);
            helpers.add(hlpFianza);

            anadirFianza(fianza);
        }
    }

    public SIJRBeanDataSource getDatasources(){
        return new SIJRBeanDataSource(helpers);
    }

    private void anadirFianza(Fianza fianza){

        double fianzaLegal = fianza.getFianzaLegal();
        double garantiasAdicionales = fianza.getFianzaComplementaria();

        int tipoGarantia = getTipoGarantia(fianza);

        if (tipoGarantia ==  TIPO_FIANZA_AVAL){
            garantiaAval += garantiasAdicionales;

        }else if(tipoGarantia ==  TIPO_FIANZA_EFECTIVO){
            garantiaEfectivo += garantiasAdicionales;

        }

        int estadoFianza = getEstadoFianza(fianza);

        if (estadoFianza == ESTADO_FIANZA_NO_INGRESADA_EN_ADMINISTRACION){
            this.noIngresadaEnAdmon += fianzaLegal;

        }else if (estadoFianza == ESTADO_FIANZA_EN_ADMINISTRACION){
            this.fianzaAdmon += fianzaLegal;

        }else if (estadoFianza == ESTADO_FIANZA_EN_CAMARA){
            this.fianzaCamara += fianzaLegal;

        }else if (estadoFianza == ESTADO_FIANZA_SOLICITADA_DEVOLUCION){
            this.solicitadaDevolucion += fianzaLegal;

        }else if (estadoFianza == ESTADO_FIANZA_DEVUELTA){
            this.fianzaDevuelta += fianzaLegal;

        }
    }

    public Double getNoIngresadaEnAdmon() {
        return noIngresadaEnAdmon;
    }

    public Double getFianzaAdmon() {
        return fianzaAdmon;
    }

    public Double getFianzaCamara() {
        return fianzaCamara;
    }

    public Double getSolicitadaDevolucion() {
        return solicitadaDevolucion;
    }

    public Double getFianzaDevuelta() {
        return fianzaDevuelta;
    }



    public Double getGarantiaAval() {
        return garantiaAval;
    }

    public Double getGarantiaEfectivo() {
        return garantiaEfectivo;
    }

    public String getFinca(){
        return ub.getNombre();
    }

    public int getEstadoFianza(Fianza f){
        return f.getEstadoFianza().getId();
    }

    public int getTipoGarantia(Fianza f){
        if (f.getEsAvalBancario()){
            return TIPO_FIANZA_AVAL;
        }
        if (f.getTienePolizaAlquiler()){
            return TIPO_FIANZA_POLIZA;
        }
        return  TIPO_FIANZA_EFECTIVO;

    }
}