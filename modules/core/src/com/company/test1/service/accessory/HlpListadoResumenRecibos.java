package com.company.test1.service.accessory;


import com.company.test1.entity.CuentaBancaria;
import com.company.test1.entity.Persona;
import com.company.test1.entity.departamentos.Departamento;
import com.company.test1.entity.enums.recibos.DefinicionRemesaTipoGiroEnum;
import com.company.test1.entity.recibos.ImplementacionConcepto;
import com.company.test1.entity.recibos.Recibo;
import com.company.test1.entity.recibos.accesorios.ImplementacionConceptoOrdenacionComparator;
import com.company.test1.service.RecibosService;
import com.company.test1.service.RecibosServiceBean;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 *
 * @author Carlos Conti
 */
public class HlpListadoResumenRecibos implements Comparable{

    Recibo recibo = null;
    Double[] importes = new Double[15];
    String[] conceptos = new String[15];


    static class ComparadorHlpListadoResumenRecibos implements Comparator {

        @Override
        public int compare(Object o1, Object o2) {
            HlpListadoResumenRecibos hlp1 = (HlpListadoResumenRecibos) o1;
            HlpListadoResumenRecibos hlp2 = (HlpListadoResumenRecibos) o2;

            Departamento dep1 = hlp1.getRecibo().getUtilitarioContratoInquilino().getDepartamento();
            Departamento dep2 = hlp2.getRecibo().getUtilitarioContratoInquilino().getDepartamento();
            //pendiente solucionarlo, el id en cuba no aporta ninguna informacion de ordenacion
            return dep1.getRm2id()- dep2.getRm2id();
        }
    }


    public static Comparator getComparadorHlpListadoResumenRecibos(){
        Comparator icComparator = new ComparadorHlpListadoResumenRecibos();
        return icComparator;
    }

    public HlpListadoResumenRecibos(Recibo r) {
        this.recibo = r;
        //poblando la informacion de recibo
        List<ImplementacionConcepto> iicc = recibo.getImplementacionesConceptos();

        iicc = AppBeans.get(RecibosService.class).getVersionAgregadaPorConceptos(iicc);
        Collections.sort(iicc, new ImplementacionConceptoOrdenacionComparator());

        for (int i = 0; i < iicc.size(); i++) {
            ImplementacionConcepto implementacionConcepto = iicc.get(i);
            String conc = implementacionConcepto.getConcepto().getAbreviacion();
            double importe = implementacionConcepto.getImporte();
            importes[i] = importe;
            conceptos[i] = conc;
        }
        for (int i = iicc.size(); i < 15;i++){
            importes[i] = 0.0;
            conceptos[i] = "";
        }
    }

    public String getDireccionInquilino(){

        return recibo.getUtilitarioContratoInquilino().getDepartamento().getNombreDescriptivoCompleto();
    }
    public String getNombreInquilino(){
        Persona p = null;
        if(recibo.getUtilitarioInquilino()!=null){
            return recibo.getUtilitarioInquilino().getNombreCompleto();
        }
        if(recibo.getUtilitarioContratoInquilino()!=null){
            return recibo.getUtilitarioContratoInquilino().getInquilino().getNombreCompleto();
        }

        return "NO DISPONIBLE";
    }

    public String getFormaDeCobro(){
        String s = "";
        //temporalmente

        DefinicionRemesaTipoGiroEnum tipoDeCobro = recibo.getUtilitarioContratoInquilino().getProgramacionRecibo().getDefinicionRemesa().getTipoGiro();

        if(tipoDeCobro == DefinicionRemesaTipoGiroEnum.BANCARIA){

            CuentaBancaria cb = recibo.getUtilitarioContratoInquilino().getProgramacionRecibo().getCuentaBancariaPagador();
            if (cb!=null){
                s = cb.getVersionIBAN();
            }else{
                //si no tengo cuenta bancaria pagador las he de cargar
                //no lo hago de forma generica pues en la Sofia de CUBA la cta del pagador se ha de poner
                Persona inquilino = recibo.getUtilitarioContratoInquilino().getInquilino();
                inquilino = AppBeans.get(DataManager.class).reload(inquilino, "persona-view");
                List<CuentaBancaria> ccbb = inquilino.getCuentasBancarias();
                if (ccbb.size()>0){
                    cb = ccbb.get(0);
                    s = cb.getVersionIBAN();
                }
            }
        }
        if(tipoDeCobro == DefinicionRemesaTipoGiroEnum.ADMINISTRACION){
            s = "Administración";
        }
        if(tipoDeCobro == DefinicionRemesaTipoGiroEnum.AUTOFACTURA){
            s = "Autofactura";
        }
        return s;
    }

    public Double getTotal(){
        return recibo.getTotalReciboPostCCAA();
    }

    public Double getIva(){
        double iva = recibo.getTotalReciboPostCCAA() - recibo.getTotalRecibo();
        return iva;
    }

    public Double getImporte1(){
        return importes[0];
    }

    public String getC1(){
        return conceptos[0];
    }

    public Double getImporte2(){
        return importes[1];
    }

    public String getC2(){
        return conceptos[1];
    }

    public Double getImporte3(){
        return importes[2];
    }

    public String getC3(){
        return conceptos[2];
    }

    public Double getImporte4(){
        return importes[3];
    }

    public String getC4(){
        return conceptos[3];
    }

    public Double getImporte5(){
        return importes[4];
    }

    public String getC5(){
        return conceptos[4];
    }

    public Double getImporte6(){
        return importes[5];
    }

    public String getC6(){
        return conceptos[5];
    }

    public Double getImporte7(){
        return importes[6];
    }

    public String getC7(){
        return conceptos[6];
    }

    public Double getImporte8(){
        return importes[7];
    }

    public String getC8(){
        return conceptos[7];
    }

    public String getNumRecibo(){
//        return recibo.getNumRecibo() + " / " +  recibo.getProgramacionRecibo().getContratoInquilino().getDepartamento().getUbicacion().getId();
        return recibo.getNumRecibo();
    }

    @Override
    public int compareTo(Object o){
        HlpListadoResumenRecibos hlp = (HlpListadoResumenRecibos) o;
        Departamento dr = hlp.recibo.getUtilitarioContratoInquilino().getProgramacionRecibo().getContratoInquilino().getDepartamento();
        Departamento thr = this.recibo.getUtilitarioContratoInquilino().getProgramacionRecibo().getContratoInquilino().getDepartamento();
        return thr.getRm2id().compareTo(dr.getRm2id());//pendiente subsanar
    }

    public Recibo getRecibo() {
        return recibo;
    }


}
