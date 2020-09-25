package com.company.test1.service.accessory;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import com.company.test1.entity.Persona;
import com.company.test1.entity.departamentos.Departamento;
import com.company.test1.entity.enums.recibos.DefinicionRemesaTipoGiroEnum;
import com.company.test1.entity.recibos.Recibo;
import com.company.test1.service.RecibosService;
import com.haulmont.cuba.core.global.AppBeans;
import net.sf.jasperreports.engine.JasperReport;


/**
 *
 * @author Carlos Conti
 */
public class HlpInquilino {

    Persona inquilino;

    SIJRBeanDataSource dataSourceRecibos;

    Double totalDevuelto = 0.0;
    Double totalPendiente = 0.0;
    JasperReport reportRecibo;

    RecibosService recibosService;

    Departamento departamento = null;

    public void setRecibosService(RecibosService recibosService) {
        this.recibosService = recibosService;
    }


    public HlpInquilino(Persona inquilino,List<HlpRecibo> hlpRecibos,JasperReport reportRecibo) {
        this.inquilino = inquilino;
        this.dataSourceRecibos = new SIJRBeanDataSource(hlpRecibos);
        this.reportRecibo = reportRecibo;
        this.departamento = hlpRecibos.get(0).getRecibo().getUtilitarioContratoInquilino().getDepartamento();
        int y = 2;
    }

    public Departamento getDepartamento(){
        return this.departamento;
    }


    public String getTotalInquilino() {

        double totalFinca = getTotalPendienteNumero() + getTotalDevueltoNumero();

        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.GERMANY);
        String totalFincaNombre = nf.format(totalFinca);

        return totalFincaNombre;
    }



    public SIJRBeanDataSource getDataSourceRecibos() {
        return dataSourceRecibos;
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

    /*--------------------------------------------------------------------*/

    public Double getTotalDevueltoNumero() {
        return totalDevuelto;
    }

    public Double getTotalPendienteNumero() {

        return totalPendiente;
    }

    public JasperReport getReportRecibo() {
        return reportRecibo;
    }

    public void anadirReciboAInquilinoRecibo(Recibo r) {
        if (r.getOrdenanteRemesa()==null){
            totalPendiente +=AppBeans.get(RecibosService.class).getTotalPendiente(r);
            return;
        }
        if (r.getOrdenanteRemesa().getRemesa().getDefinicionRemesa().getTipoGiro()== DefinicionRemesaTipoGiroEnum.BANCARIA){
            totalDevuelto += AppBeans.get(RecibosService.class).getTotalPendiente(r);
        } else {
            totalPendiente += AppBeans.get(RecibosService.class).getTotalPendiente(r);
        }
    }
}