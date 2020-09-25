package com.company.test1.entity.notificaciones;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import com.company.test1.entity.recibos.ConceptoRecibo;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author Carlos Conti
 */
public class HelperNotificacionesAumentos implements Serializable {

    List<ConceptoRecibo> conceptosReciboAumentos = null;

    public HelperNotificacionesAumentos(List<ConceptoRecibo> cra) {
        conceptosReciboAumentos = cra;
    }

    public String getTextoExplicativoAumentos(){
        String s = "";
        NumberFormat df = NumberFormat.getCurrencyInstance(Locale.GERMANY);
        for (int i = 0; i < conceptosReciboAumentos.size(); i++) {
            ConceptoRecibo conceptoRecibo = conceptosReciboAumentos.get(i);
            s += conceptoRecibo.getConcepto().getTituloConcepto() + " " + df.format(conceptoRecibo.getImporte()) + "<br>";

        }
        return s;
    }

    public String getTextoCantidadIncremento(){
        double d = 0.0;
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.GERMANY);
        for (int i = 0; i < conceptosReciboAumentos.size(); i++) {
            ConceptoRecibo conceptoRecibo = conceptosReciboAumentos.get(i);
            d+= conceptoRecibo.getImporte();

        }
        String s = nf.format(d);
        return s;
    }



}
