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
import java.util.Date;
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

    Date fechaInicial = null;
    Date fechaFinal = null;

    public HlpFinca(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public HlpFinca(Ubicacion ubicacion, Date fechaInicial, Date fechaFinal) {

        this.ubicacion = ubicacion;
        this.fechaFinal = fechaFinal;
        this.fechaInicial = fechaInicial;
    }



    public void setRecibosService(RecibosService recibosService) {
        this.recibosService = recibosService;
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

    private boolean bancarioAdministracion(Recibo r){
        if (r.getOrdenanteRemesa()==null){
            return false;
        }else{
            if (r.getOrdenanteRemesa().getRemesa().getDefinicionRemesa().getTipoGiro() == DefinicionRemesaTipoGiroEnum.BANCARIA){
                return true;
            }else{
                return false;
            }
        }
    }


    public void anadirReciboATotalesUbicacion(Recibo r){

        if (bancarioAdministracion(r)==false) {
            if ((fechaInicial == null) || (fechaFinal == null)) {
                double totalCobrado = AppBeans.get(RecibosService.class).getTotalIngresadoAdministracion(r);
                totalCobrado += AppBeans.get(RecibosService.class).getTotalIngresadoBancario(r);
                totalPendiente += r.getTotalReciboPostCCAA() - (totalCobrado
                        + AppBeans.get(RecibosService.class).getTotalCobranzas(r)
                        + AppBeans.get(RecibosService.class).getTotalCompensado(r));

            } else {
                double totalCobrado = AppBeans.get(RecibosService.class).getTotalIngresadoAdministracion(r, fechaInicial, fechaFinal);
                totalCobrado += AppBeans.get(RecibosService.class).getTotalIngresadoBancario(r, fechaInicial, fechaFinal);
                totalPendiente += r.getTotalReciboPostCCAA() - (totalCobrado
                        + AppBeans.get(RecibosService.class).getTotalCobranzas(r, fechaInicial, fechaFinal)
                        + AppBeans.get(RecibosService.class).getTotalCompensado(r, fechaInicial, fechaFinal));

            }
        }else{
            if ((fechaInicial==null)||(fechaFinal==null)) {
                double totalCobrado = AppBeans.get(RecibosService.class).getTotalIngresadoAdministracion(r);
                totalCobrado += AppBeans.get(RecibosService.class).getTotalIngresadoBancario(r);
                totalDevuelto += r.getTotalReciboPostCCAA() - (totalCobrado
                        + AppBeans.get(RecibosService.class).getTotalCobranzas(r)
                        -AppBeans.get(RecibosService.class).getTotalDevuelto(r)
                        +AppBeans.get(RecibosService.class).getTotalCompensado(r));

            }else{
                double totalCobrado = AppBeans.get(RecibosService.class).getTotalIngresadoAdministracion(r, fechaInicial, fechaFinal);
                totalCobrado += AppBeans.get(RecibosService.class).getTotalIngresadoBancario(r, fechaInicial, fechaFinal);
                totalDevuelto += r.getTotalReciboPostCCAA() - (totalCobrado
                        + AppBeans.get(RecibosService.class).getTotalCobranzas(r, fechaInicial, fechaFinal)
                        -AppBeans.get(RecibosService.class).getTotalDevuelto(r, fechaInicial, fechaFinal)
                        +AppBeans.get(RecibosService.class).getTotalCompensado(r, fechaInicial, fechaFinal));

            }
        }



    }
}