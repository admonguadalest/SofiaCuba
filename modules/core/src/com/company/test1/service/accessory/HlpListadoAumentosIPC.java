package com.company.test1.service.accessory;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import java.text.SimpleDateFormat;
import java.util.*;

import com.company.test1.entity.contratosinquilinos.ContratoInquilino;
import com.company.test1.entity.recibos.ConceptoRecibo;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.AppBeans;
import org.apache.commons.lang.time.DateUtils;


/**
 *
 * @author Carlos Conti
 */
public class HlpListadoAumentosIPC {


    ContratoInquilino contratoInquilino = null;
    Date fechaDesde = null;
    Date fechaHasta = null;
    SIJRBeanDataSource detalleAumentos = null;
    boolean previsualizacion = false;



    public HlpListadoAumentosIPC(ContratoInquilino c, Date fechaInicio, Date fechaFinal) throws Exception {
        this.contratoInquilino = c;
        this.fechaDesde = fechaInicio;
        this.fechaHasta = fechaFinal;


        String sql = "SELECT cr FROM ConceptoRecibo cr"
                + " JOIN cr.programacionRecibo pr "
                + " JOIN pr.contratoInquilino c"
                + " JOIN cr.concepto conc"
                + " WHERE c.id = :cid"
                + " AND cr.fechaValor BETWEEN :fd AND :fh"
                + " AND conc.abreviacion = :abreviacion";
        Hashtable pams = new Hashtable();
        pams.put("fd",fechaDesde);
        pams.put("fh",fechaHasta);
        pams.put("cid",contratoInquilino.getId());
        pams.put("abreviacion","IRTA");


        List<HlpListadoAumentosIPC2> detalle = new ArrayList<HlpListadoAumentosIPC2>();
        Transaction t = AppBeans.get(Persistence.class).createTransaction();
        List<ConceptoRecibo> ccrr = AppBeans.get(Persistence.class).getEntityManager().createQuery(sql).setParameter("fd", fechaDesde).setParameter("fh", fechaHasta)
                .setParameter("cid", contratoInquilino.getId()).setParameter("abreviacion", "IRTA").getResultList();
        for (int i = 0; i < ccrr.size(); i++) {
            ConceptoRecibo conceptoRecibo = ccrr.get(i);
            HlpListadoAumentosIPC2 hlp =new HlpListadoAumentosIPC2(conceptoRecibo, false);
            detalle.add(hlp);
        }
        detalleAumentos = new SIJRBeanDataSource(detalle);
        previsualizacion = false;
    }

    /**
     * Report de previsualizacion
     * @param c
     * @param ccrr
     * @param fechaDesde
     * @param fechaHasta
     */
    public HlpListadoAumentosIPC(ContratoInquilino c, List<ConceptoRecibo> ccrr, Date fechaDesde, Date fechaHasta) {
        this.contratoInquilino = c;
        this.fechaDesde = fechaDesde;
        this.fechaHasta = fechaHasta;


        List<HlpListadoAumentosIPC2> detalle = new ArrayList<HlpListadoAumentosIPC2>();

        for (int i = 0; i < ccrr.size(); i++) {
            ConceptoRecibo conceptoRecibo = ccrr.get(i);
            HlpListadoAumentosIPC2 hlp =new HlpListadoAumentosIPC2(conceptoRecibo, true);
            detalle.add(hlp);
        }
        detalleAumentos = new SIJRBeanDataSource(detalle);
        previsualizacion = true;
    }




    public String getFechaContrato(){
        SimpleDateFormat sdfFecha = new SimpleDateFormat("dd/MM/yyyy");
        String fechaNombre = sdfFecha.format(contratoInquilino.getFechaRealizacion());

        return fechaNombre;
    }




    public String getDireccion(){
        return contratoInquilino.getDepartamento().getNombreDescriptivoCompleto();
    }

    public String getNombreInquilino(){
        return contratoInquilino.getInquilino().getNombreCompleto();
    }

    public String getProximaRevision(){
        Date fechaProxRevision = null;
        int mesesEntreRevision = contratoInquilino.getPeriodoActualizacionIPC();
        if (!previsualizacion){
            return contratoInquilino.getMesAnyoAplicacionIPC();
        }else{
            try{
                fechaProxRevision = new SimpleDateFormat("MMyyyy").parse(contratoInquilino.getMesAnyoAplicacionIPC());
            }catch(Exception exc){
                return "NNDD";
            }
            Date fechaAnterior = DateUtils.addMonths(fechaProxRevision, mesesEntreRevision);
            Calendar cal = Calendar.getInstance();
            cal.setTime(fechaAnterior);
            Integer mes = cal.get(Calendar.MONTH);
            mes++;
            Integer anno = cal.get(Calendar.YEAR);
            String strMes = mes.toString();
            String strAnno = anno.toString();
            if (strMes.length()==1) strMes = "0" + strMes;

            return strMes + strAnno;
        }

    }

    public SIJRBeanDataSource getDetalleAumentos() {
        return detalleAumentos;
    }

    public void setDetalleAumentos(SIJRBeanDataSource detalleAumentos) {
        this.detalleAumentos = detalleAumentos;
    }

}