package com.company.test1.service.accessory;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import com.company.test1.entity.Persona;
import com.company.test1.entity.departamentos.Departamento;
import com.company.test1.entity.departamentos.Ubicacion;
import com.company.test1.entity.enums.recibos.DefinicionRemesaTipoGiroEnum;
import com.company.test1.entity.enums.recibos.ReciboCobradoModoIngreso;
import com.company.test1.entity.recibos.Recibo;
import com.company.test1.entity.recibos.ReciboCobrado;
import com.company.test1.service.RecibosService;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Security;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 *
 * @author Carlos Conti
 */
public class HlpRecibo {


    Recibo recibo;


    SecurityContext securityContext = null;


    public HlpRecibo(){}

    public HlpRecibo(Recibo recibo, SecurityContext sc) {

        this.recibo = recibo;
        this.securityContext = sc;
        AppContext.setSecurityContext(sc);

    }

    public  TreeMap construyeEstructurasReportsDesdeListadoRecibos(List<Recibo> rr, SecurityContext sc){


        TreeMap<Ubicacion,TreeMap> pams = new TreeMap();

        for (int i = 0; i < rr.size(); i++) {
            Recibo r = rr.get(i);

            Ubicacion ub = r.getUtilitarioContratoInquilino().getDepartamento().getUbicacion();

            TreeMap<String,Object> ubicacionesObjetos = pams.get(ub);
            if (ubicacionesObjetos == null){
                ubicacionesObjetos = new TreeMap<String,Object>();

                ubicacionesObjetos.put("PERSONAS_RECIBOS", new TreeMap<Persona,List<HlpRecibo>>());
                ubicacionesObjetos.put("PERSONAS", new ArrayList<Persona>());
                HlpFinca hlpFnew = new HlpFinca(ub);

                ubicacionesObjetos.put("HELPER_FINCA", new HlpFinca(ub));


                pams.put(ub, ubicacionesObjetos);
            }

            HlpFinca hlpFinca = (HlpFinca) ubicacionesObjetos.get("HELPER_FINCA");
            hlpFinca.anadirReciboATotalesUbicacion(r);


            Persona inquilino = r.getUtilitarioContratoInquilino().getInquilino();


            TreeMap<Persona,List<HlpRecibo>> personasRecibo = (TreeMap<Persona,List<HlpRecibo>>) ubicacionesObjetos.get("PERSONAS_RECIBOS");
            List<HlpRecibo> listaRecibos = personasRecibo.get(inquilino);

            if (listaRecibos == null){
                List<Persona> pp = (List<Persona>) ubicacionesObjetos.get("PERSONAS");
                pp.add(inquilino);

                listaRecibos = new ArrayList<HlpRecibo>();
                personasRecibo.put(inquilino, listaRecibos);

            }

            listaRecibos.add(new HlpRecibo(r, sc));
        }

        return pams;
    }







    public String getFechaDevuelto() throws Exception{
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//            Date fechaD = AppBeans.get(RecibosService.class).getFechaDevuelto(recibo);
//            if (fechaD == null) {
//                return "";
//            }
            AppContext.setSecurityContext(securityContext);
            Date fechaD = AppBeans.get(RecibosService.class).getFechaDevuelto(recibo);

            if (fechaD==null){
                return "";
            }

            String fechaDevueltoNombre = sdf.format(fechaD);

            return fechaDevueltoNombre;
        }catch(Exception exc){

            exc.printStackTrace();
            return "na/err";
        }

    }



    public String getFechaRecibo() {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String fechaReciboNombre = sdf.format(recibo.getFechaEmision());


        return fechaReciboNombre;
    }

    public String getDevuelto() {
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.GERMANY);
        if (this.recibo.getOrdenanteRemesa()!=null) {
            if (this.recibo.getOrdenanteRemesa().getRemesa().getDefinicionRemesa().getTipoGiro() == DefinicionRemesaTipoGiroEnum.BANCARIA) {
//            double devuelto = recibosService.getTotalDevuelto(recibo); //pendiente no consigo usar recibosService aqui, me da una exception
                //dice que no hay security context bound al thread que ejecuta los subreports
                //toca hacerlo manual
                AppContext.setSecurityContext(securityContext);
                double devuelto = AppBeans.get(RecibosService.class).getTotalDevuelto(recibo);


                String devueltoNombre = nf.format(devuelto);
                return devueltoNombre;
            } else {
                return "";
            }
        }else{
            return nf.format(0.0);
        }



    }

    public String getPendiente() {
        if (this.recibo.getOrdenanteRemesa()!=null) {
            if (this.recibo.getOrdenanteRemesa().getRemesa().getDefinicionRemesa().getTipoGiro() == DefinicionRemesaTipoGiroEnum.ADMINISTRACION) {
                double devuelto = AppBeans.get(RecibosService.class).getTotalPendiente(recibo);
                NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.GERMANY);
                String devueltoNombre = nf.format(devuelto);
                return devueltoNombre;
            } else {
                return "";
            }
        }else{
            double devuelto = AppBeans.get(RecibosService.class).getTotalPendiente(recibo);
            NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.GERMANY);
            String devueltoNombre = nf.format(devuelto);
            return devueltoNombre;
        }
    }

    public String getNombreDepartamento(){
        Departamento d = recibo.getUtilitarioContratoInquilino().getDepartamento();
        return d.getPiso() + " " + d.getPuerta();
    }

    public String getNombreTitularRecibo(){
        return recibo.getUtilitarioContratoInquilino().getInquilino().getNombreCompleto();
    }


    public Recibo getRecibo() {
        return recibo;
    }

    public String getTipoGasto(){
        if (this.recibo.getOrdenanteRemesa()!=null) {
            if (this.recibo.getOrdenanteRemesa().getRemesa().getDefinicionRemesa().getTipoGiro() == DefinicionRemesaTipoGiroEnum.BANCARIA)
                return "BANCARIO";

            return "ADMIN.";
        }else{
            return "ADMIN.";
        }
    }



}