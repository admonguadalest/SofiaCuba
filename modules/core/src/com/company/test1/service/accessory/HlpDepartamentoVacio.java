package com.company.test1.service.accessory;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import com.company.test1.entity.departamentos.Departamento;
import com.company.test1.entity.recibos.Recibo;
import com.company.test1.service.RecibosService;
import com.haulmont.cuba.core.global.AppBeans;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 *
 * @author Carlos Conti
 */
public class HlpDepartamentoVacio {

    Departamento departamento = null;


    public HlpDepartamentoVacio(Departamento d) {
        departamento = d;

    }

    public String getProrrateo(){ return "";}

    public String getNombreDescriptivoCompleto(){
        return departamento.getNombreDescriptivoCompleto();

    }

    public String getDescripcionTipoViviendaLocal(){
        if (departamento.getViviendaLocal()){
            return "Vivienda";
        }else{
            return "Local";
        }
    }

    public String getVacioDesde() throws Exception{
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Recibo r = AppBeans.get(RecibosService.class).getUltimoReciboGirado(departamento);
        if (r==null) return "N/D";
        return df.format(r.getFechaEmision());
    }

    public String getMontanteUltimoRecibo() throws Exception{
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.GERMANY);
        Recibo r = AppBeans.get(RecibosService.class).getUltimoReciboGirado(departamento);
        if (r==null) return "";
        Double d = r.getTotalReciboPostCCAA();

        String montanteUltimoRecibo = nf.format(d);

        Double prorrateo = AppBeans.get(RecibosService.class).getProrrateoUltimoContratoDepartamento(departamento);

        if (prorrateo != null){

            NumberFormat percentFormat = NumberFormat.getPercentInstance();
            String tipoNombre = percentFormat.format(prorrateo);

            montanteUltimoRecibo += " - " +  tipoNombre;

        }

        return montanteUltimoRecibo;

    }

    public String getDepartamento(){
        Object id = departamento.getId().toString();
//        String depID = Integer.toString(id);

//        return depID;
        return id.toString();
    }

    public String getUbicacion(){
        Object id = departamento.getUbicacion().getId();
//        String ubID = Integer.toString(id);

//        return ubID;
        return id.toString();
    }

}