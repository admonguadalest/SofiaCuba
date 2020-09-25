package com.company.test1.service.accessory;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import com.company.test1.entity.contratosinquilinos.Fianza;

import java.text.NumberFormat;
import java.util.Locale;


/**
 *
 * @author Xavier
 */
public class HlpFianza {

    Fianza fianza;

    public HlpFianza(Fianza fianza){
        this.fianza = fianza;
    }


    public String getDepartamento(){
        return fianza.getContratoInquilino().getDepartamento().getPisoPuerta();
    }

    public String getInquilino(){
        return fianza.getContratoInquilino().getInquilino().getNombreCompleto();
    }

    public String getGarantiaEstado(){
        return fianza.getTipoGarantiaNombre();
    }

    public String getGarantiaImporte(){
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.GERMANY);
        if (fianza.getFianzaComplementaria() == null) return "-";


        return nf.format(fianza.getFianzaComplementaria());
    }

    public String getFianzaEstado(){
        return fianza.getEstadoFianzaNombre();
    }

    public String getFianzaImporte(){
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.GERMANY);
        if (fianza.getFianzaLegal() == null) return "-";


        return nf.format(fianza.getFianzaLegal());
    }
}